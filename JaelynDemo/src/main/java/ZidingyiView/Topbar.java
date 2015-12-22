package ZidingyiView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jaelyndemo.R;

/**
 * Created by Jaelyn on 2015/12/16.
 */
public class Topbar extends RelativeLayout {

    private Button leftButtons,rightButtons;
    private TextView tvTitles;

    private int leftTextColors;
    private Drawable leftBackgrounds;
    private String leftTexts;

    private int rightTextColors;
    private Drawable rightBackgrounds;
    private String rightTexts;

    private float titleTextSizes;
    private int titleTextColors;
    private String titleString;

    private topbarClickLstener lstener;
    public interface topbarClickLstener{
        public void leftCilick();
        public void rightCilick();
        public void wiperSwitchCilick(boolean checkState);
    }

    public void setOnTopbarClickLstener(topbarClickLstener lstener){
         this.lstener = lstener;
    }

    private LayoutParams leftParams,rightParams,titleParams,switchParams;

    private WiperSwitch wiperSwitch;
    private String xtar = "swich";

    public Topbar(final Context context, AttributeSet attrs) {
        super(context,attrs);

        //获取自定义属性 (Toolbar文件定义的)
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Toolbars);
        leftTextColors = ta.getColor(R.styleable.Toolbars_leftTextColors,0);
        leftBackgrounds = ta.getDrawable(R.styleable.Toolbars_leftBackgrounds);
        leftTexts = ta.getString(R.styleable.Toolbars_leftTexts);

        rightTextColors = ta.getColor(R.styleable.Toolbars_rightTextColors,0);
        rightBackgrounds = ta.getDrawable(R.styleable.Toolbars_rightBackgrounds);
        rightTexts = ta.getString(R.styleable.Toolbars_rightTexts);

        titleTextColors = ta.getColor(R.styleable.Toolbars_titleTextColors,0);
        titleTextSizes = ta.getDimension(R.styleable.Toolbars_titleTextSizes,0);
        titleString = ta.getString(R.styleable.Toolbars_titleStrings);

        xtar = ta.getString(R.styleable.Toolbars_swichOrTitle);

        ta.recycle();//回收

        leftButtons = new Button(context);
        rightButtons = new Button(context);
        tvTitles = new TextView(context);
        wiperSwitch = new WiperSwitch(context);


        leftButtons.setTextColor(leftTextColors);
        leftButtons.setBackground(leftBackgrounds);
        leftButtons.setText(leftTexts);

        rightButtons.setTextColor(rightTextColors);
        rightButtons.setBackground(rightBackgrounds);
        rightButtons.setText(rightTexts);

        tvTitles.setTextColor(titleTextColors);
        tvTitles.setTextSize(titleTextSizes);
        tvTitles.setText(titleString);
        tvTitles.setGravity(Gravity.CENTER);//设置居中

        setBackgroundColor(0xFF313131);//背景颜色

        leftParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButtons,leftParams);

        rightParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButtons,rightParams);

        if (xtar.equals("title")){
            titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
            addView(tvTitles,titleParams);
        }else if(xtar.equals("swich")){
            switchParams = new LayoutParams(360, LayoutParams.MATCH_PARENT);
            switchParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
            addView(wiperSwitch,switchParams);
        }
       // wiperSwitch.setChecked(lstener.bMsetChecked());//设置初始状态
        leftButtons.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lstener.leftCilick();
            }
        });
        rightButtons.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lstener.rightCilick();
            }
        });
        wiperSwitch.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                lstener.wiperSwitchCilick(checkState);
            }
        });

    }

    public void bMsetChecked(boolean b){
        wiperSwitch.setChecked(b);
    }

    //控制右边的按钮是否显示
    public void setLeftIsvisable(boolean flag){
        if (flag){
            leftButtons.setVisibility(View.VISIBLE);
        }else {
            leftButtons.setVisibility(View.GONE);
        }
    }

}
