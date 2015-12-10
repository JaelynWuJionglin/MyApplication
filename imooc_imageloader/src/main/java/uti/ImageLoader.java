package uti;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;

import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类
 * Created by Jaelyn on 2015/12/9.
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    private LruCache<String,Bitmap> mLruCache; //图片缓存的核心对象

    private ExecutorService mThreadPool;//线程池
    private static final int DEFULT_THREAD_COUNT = 1;

    private Type mType = Type.LIFO;//队列的调度方式
    public enum Type{
        //先进先出，后进先出
        FIFO,LIFO;
    }

    private LinkedList<Runnable> mTaskQueue;

    private Thread mPoolThread;//后台轮询线程
    private Handler mPoolThreadHandler;

    private Handler mUIHandler; //UI 中的Handler

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;


    public ImageLoader(int threadCount ,Type type) {

        init(threadCount,type);
    }


    /**
    * 初始化操作
    * */
    private void init(int threadCount, Type type) {
        //后台轮询线程
        mPoolThread = new Thread(){
            public void run(){

                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池取出一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();
                        } catch (InterruptedException e) {

                        }
                    }
                };
                //释放一个信号量,保证次初始化完成才调用addTsaks()方法;
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            };
        };

        mPoolThread.start();

        //获取我们应用的最大可以内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;

        mLruCache = new LruCache<String,Bitmap>(cacheMemory){

            protected int sizeOf(String key,Bitmap value){

                return value.getRowBytes()*value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    //单例
    public static ImageLoader getmInstance(int defult,Type type){

        if (mInstance == null){
            synchronized (ImageLoader.class){//synchronized保证在同一时刻最多只有一个线程执行该段代码
                if (mInstance == null){
                    mInstance = new ImageLoader(defult,type);
                }
            }
        }
        return mInstance;
    }
    public static ImageLoader getmInstance(){

        if (mInstance == null){
            synchronized (ImageLoader.class){//synchronized保证在同一时刻最多只有一个线程执行该段代码
                if (mInstance == null){
                    mInstance = new ImageLoader(DEFULT_THREAD_COUNT,Type.LIFO);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据path为imageView设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView){
        imageView.setTag(path);
        if (mUIHandler == null){
            mUIHandler = new Handler(){
              public void handleMessage(Message msg){
                  //获得到图片，为imageView回调设置图片
                  ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                  Bitmap bm = holder.bitmap;
                  ImageView imageView = holder.imageView;
                  String path = holder.path;

                  //将path与getTag存储路径进行比较
                  if(imageView.getTag().toString().equals(path)){
                      imageView.setImageBitmap(bm);
                  }
              };
            };
        }

        //根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);

        if (bm != null){
            Message message = Message.obtain();
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.bitmap = bm;
            holder.path = path;
            holder.imageView = imageView;
            message.obj = holder;
            mUIHandler.sendMessage(message);
            refreashBitmap(path,imageView,bm);
        }else {
            addTsaks( new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //图片压缩
                    //1，获得图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2,压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path,imageSize.width,imageSize.height);
                    //3,把图片加入到缓存
                    addBitmapTolruCache(path,bm);

                    refreashBitmap(path,imageView,bm);

                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    private class ImgBenHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    /**
     * 传回图片
     * */
    private void refreashBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageView;
        holder.path = path;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 把图片加入到LruCache
     * @param bm
     * @param path
     * */
    private void addBitmapTolruCache(String path, Bitmap bm) {

        if(getBitmapFromLruCache(path) == null){
            if(bm!=null){
                mLruCache.put(path,bm);
            }
        }
    }




    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     * */
    private Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {

        //获得图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不把图片加载到内存
        BitmapFactory.decodeFile(path,options);

        options.inSampleSize = caculateInSampleSize(options,width,height);

        //使用获得到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;//可以把图片加载到内存了
        Bitmap bitmap = BitmapFactory.decodeFile(path,options);

        return bitmap;
    }


    /**
     * 根据需求的宽和高一级图片实际的宽和高计算SampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     * */
    private int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth||height > reqHeight){
            int widthRadio = Math.round(width*1.0f/reqWidth);
            int heightRadio = Math.round(height*1.0f/reqHeight);

            inSampleSize = Math.max(widthRadio,heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 根据ImageView获得适当的压缩的高和宽
     * @param imageView
     * @return
     * */
    private ImageSize getImageViewSize(ImageView imageView) {

        ImageSize imagesize = new ImageSize();
        DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        LayoutParams lp = (LayoutParams) imageView.getLayoutParams();

        int width = imageView.getWidth();//获取imageView的实际宽度
        if (width <= 0){
             width = lp.width;//获取imageView在layout中声明的宽度
        }
        if (width <= 0){
            //检查最大值
            //width = imageView.getMaxWidth();
            width = getImageViewFieldValue(imageView,"mMaxWidth");
        }
        if (width <= 0){
            width = displayMetrics.widthPixels;//屏幕宽度
        }

        int height = imageView.getHeight();//获取imageView的实际宽度
        if (height <= 0){
            height = lp.height;//获取imageView在layout中声明的宽度
        }
        if (height <= 0){
            //检查最大值
            //height = imageView.getMaxHeight();
            height =getImageViewFieldValue(imageView,"mMaxHeight");
        }
        if (height <= 0){
            height = displayMetrics.heightPixels;//屏幕宽度
        }

        imagesize.width = width;
        imagesize.height = height;

        return imagesize;
    }


    /**
     * 通过反射获取imageview的某个属性值
     * @param object
     * @param fieldName
     * */
    private static int getImageViewFieldValue(Object object ,String fieldName){

        int value = 0;

        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            int fieldValue = field.getInt(object);
            if(fieldValue > 0&&fieldValue<Integer.MAX_VALUE){
                value = fieldValue;
            }

        } catch (Exception e) {

        }
        return value;
    }


    /**
     * 根据path在缓存中获取bitmap
     * @param key
     **/
    public Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }


    private synchronized void addTsaks(Runnable runnable) {
        mTaskQueue.add(runnable);
        try {
            if(mPoolThreadHandler == null){
                mSemaphorePoolThreadHandler.acquire();
            }
        } catch (InterruptedException e) {
        }
        mPoolThreadHandler.sendEmptyMessage(DEFULT_THREAD_COUNT);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     **/
    public Runnable getTask() {
        if (mType == Type.FIFO){

            return mTaskQueue.removeFirst();
        }else if (mType == Type.LIFO){
            return mTaskQueue.removeLast();
        }
        return null;
    }


    private class ImgBeanHolder{
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    private class ImageSize{
        int width;
        int height;
    }

}
