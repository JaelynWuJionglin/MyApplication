package com.Services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 文件上传
 * Created by Jaelyn on 2015/12/3.
 */
public class UploadThread extends Thread{
    private String fileName;
    private String url;

    public UploadThread(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        String boundary = "---------------------------16629240303524";
        String prefix = "--";
        String end = "\r\n";
        try {
            URL HttpUrl = new URL(url);
            conn = (HttpURLConnection) HttpUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);//允许在服务器读取数据
            conn.setDoInput(true);//允许向服务器写入数据

            conn.setRequestProperty("Content-type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(prefix + boundary + end);
            String name = fileName.substring(fileName.lastIndexOf("/")+1);
            out.writeBytes("Content-Disposition:form-data; " +
                    "name=\"file\"; filename=\""+name+"\""+end);
            out.writeBytes(end);
            FileInputStream fileInputStream = new FileInputStream(new File(fileName));
            byte[] b = new byte[1024*4];
            int len;
            while ((len = fileInputStream.read(b))!=-1){
                out.write(b, 0, len);
            }
            out.writeBytes(end);
            out.writeBytes(prefix + boundary + prefix + end);
            out.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str=reader.readLine())!=null){
                sb.append(str);
            }
            System.out.println("respose"+sb.toString());

            if (out!=null){
                out.close();
            }
            if(reader!=null){
                reader.close();
            }

            conn.disconnect();

        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

