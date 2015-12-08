package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Dialog
 * */

public class MainActivity extends Activity {
    String [] single_list = {"男","女","女博士","程序员"};
    String [] multi_list = {"篮球","足球","男生","女生"};
    String [] item_list = {"项目经理","策划","测试","美工","程序员"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initEvent();
    }


    /*
     * 初始化点击事件
     */
    private void initEvent() {
        findViewById(R.id.dailog_bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog1();
            }
        });
        findViewById(R.id.dailog_bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog2();
            }
        });
        findViewById(R.id.dailog_bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog3();
            }
        });
        findViewById(R.id.dailog_bt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog4();
            }
        });
        findViewById(R.id.dailog_bt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog5();
            }
        });
    }

    /*
     * 显示确认对话框
     */
    private void showDialog1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认对话框");//设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标
        builder.setMessage("确认对话框提示内容");//设置内容
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"点击了确定按钮!",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"点击了取消按钮!",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();//获取dialog
        dialog.show();//显示对话框
    }

    /*
     * 单选按钮对话框
     */
    private void showDialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择性别");//设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标

        builder.setSingleChoiceItems(single_list, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = single_list[which];
                Toast.makeText(MainActivity.this,"这个人是"+str,Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();//获取dialog
        dialog.show();//显示对话框
    }
    /*
     * 度选按钮对话框
     */
    private void showDialog3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("爱好");//设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标

        builder.setMultiChoiceItems(multi_list, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity.this,"我喜欢上了"+multi_list[which],Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"我不喜欢"+multi_list[which],Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();//获取dialog
        dialog.show();//显示对话框
    }

    /*
     * 列表对话框
     */
    private void showDialog4() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("部门列表");//设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标
        builder.setItems(item_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"我动了"+item_list[which],Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();//获取dialog
        dialog.show();//显示对话框
    }

    /*
     * 自定义对话框
     */
    private void showDialog5() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_layout,null);//获取布局
        Button button = (Button) view.findViewById(R.id.tijiao);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("自定义对话框");//设置标题
        builder.setIcon(R.mipmap.ic_launcher);//设置图标
        builder.setView(view);
        final AlertDialog dialog = builder.create();//获取dialog
        dialog.show();//显示对话框

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  dialog.dismiss();
            }
        });
    }

}
