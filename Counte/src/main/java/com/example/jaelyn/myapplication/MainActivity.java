package com.example.jaelyn.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    Button button0,button1,button2,button3,button4,button5,button6,button7,button8,button9,buttondian;
    Button  delete,clear,deng,jia,jian,chen,chu;
    EditText editText;
    boolean clear_flag;//清空标示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button0 = (Button)findViewById(R.id.Button0);
        button1 = (Button)findViewById(R.id.Button1);
        button2 = (Button)findViewById(R.id.Button2);
        button3 = (Button)findViewById(R.id.Button3);
        button4 = (Button)findViewById(R.id.Button4);
        button5 = (Button)findViewById(R.id.Button5);
        button6 = (Button)findViewById(R.id.Button6);
        button7 = (Button)findViewById(R.id.Button7);
        button8 = (Button)findViewById(R.id.Button8);
        button9 = (Button)findViewById(R.id.Button9);
        buttondian = (Button)findViewById(R.id.Buttondian);

        delete = (Button)findViewById(R.id.delete);
        clear = (Button)findViewById(R.id.clear);
        deng = (Button)findViewById(R.id.deng);
        jia = (Button)findViewById(R.id.Buttonjia);
        jian = (Button)findViewById(R.id.Buttonjian);
        chen = (Button)findViewById(R.id.Buttonchen);
        chu = (Button)findViewById(R.id.Buttonchu);

        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttondian.setOnClickListener(this);
        delete.setOnClickListener(this);
        clear.setOnClickListener(this);
        deng.setOnClickListener(this);
        jia.setOnClickListener(this);
        jian.setOnClickListener(this);
        chen.setOnClickListener(this);
        chu.setOnClickListener(this);

        editText = (EditText)findViewById(R.id.editText);

    }

    @Override
    public void onClick(View v) {
        String str = editText.getText().toString();
        switch (v.getId()){
            case R.id.Button0:
            case R.id.Button1:
            case R.id.Button2:
            case R.id.Button3:
            case R.id.Button4:
            case R.id.Button5:
            case R.id.Button6:
            case R.id.Button7:
            case R.id.Button8:
            case R.id.Button9:
            case R.id.Buttondian:
                if(clear_flag){
                    clear_flag = false;
                    str = "";
                    editText.setText("");
                }
                editText.setText(str + ((Button)v).getText());
                break;
            case R.id.Buttonjia:
            case R.id.Buttonjian:
            case R.id.Buttonchen:
            case R.id.Buttonchu:
                if(clear_flag){
                    clear_flag = false;
                    str = "";
                    editText.setText("");
                }
                if(!str.equals("")){
                    if((str.substring(str.length()-1,str.length())).equals(" ")){
                        str = str.substring(0,str.length()-3) + " " + ((Button) v).getText() +" ";
                        editText.setText(str);
                    }else{
                        editText.setText(str + " " + ((Button) v).getText() +" ");
                    }
                }

                break;
            case R.id.delete:
                if(clear_flag){
                    clear_flag = false;
                    str = "";
                    editText.setText("");
                }else if(str != null&&!str.equals("")){
                    editText.setText(str.substring(0,str.length()-1));
                }
                break;
            case R.id.clear:
                clear_flag = false;
                str = "";
                editText.setText("");
                break;
            case R.id.deng:
                if(!str.equals("")){
                    String m = str.substring(str.indexOf(" ")+1,str.length());
                    String m1 = m.substring(m.indexOf(" ")+1,m.length());
                   // Log.i("tag" ,m + "---"+m1);
                    if (m1.contains(" ")){
                        editText.setText("错误格式");
                        str = "";
                        clear_flag = true;
                    }else {
                        getResult();
                    }
                }
                break;
        }
    }

    /*
     *运算结果
     */
    private void getResult(){
        String exp = editText.getText().toString();
        if(exp == null){
            return;
        }
        //没有输入运算符
        if(!exp.contains(" ")){
            return;
        }
        if (clear_flag) {
            clear_flag = false;
            return;
        }
        clear_flag = true;
        double ru = 0;
        String s1 = exp.substring(0,exp.indexOf(" ")); //第一个数
        String op = exp.substring(exp.indexOf(" ")+1,exp.indexOf(" ")+2);//运算符
        String s2 = exp.substring(exp.indexOf(" ")+3);//的二个数
        if(!s1.equals("")&&!s2.equals("")){
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);//强制类型转换
            if(op.equals("+")){
                ru = d1 + d2;
            }else if(op.equals("-")){
                ru = d1 - d2;
            }else if(op.equals("*")){
                ru = d1 * d2;
            }else if(op.equals("/")){
                if(d2 == 0){
                    ru = 0;
                }else {
                    ru = d1/d2;
                }
            }

            //结果是否带有小数
            String s = String.valueOf(ru);
           // Log.i("tag",  s.substring(s.indexOf(".")+1,s.length()));
            if((s.substring(s.indexOf(".")+1,s.length())).equals("0")){
                //如果结果带有一位小数且为零，那么它就是一个整数
                int r = (int)ru;
                editText.setText(r + "");
            }else {
                editText.setText(ru + "");
            }
           /*if(!s1.contains(".")&&!s2.contains(".")&&!op.equals("/")){
                //没有小数点,也不是除法运算
                int r = (int)ru;
                editText.setText(r + "");
            }else {
                editText.setText(ru + "");
            }*/
        }else if(!s1.equals("")&&s2.equals("")){
            editText.setText(ru + "");
        }else if(s1.equals("")&&!s2.equals("")){
            //double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);//强制类型转换
            if(op.equals("+")){
                ru = 0 + d2;
            }else if(op.equals("-")){
                ru = 0 - d2;
            }else if(op.equals("*")){
                ru = 0;
            }else if(op.equals("/")){
                ru = 0;
            }
            //是否有点
            if(!s2.contains(".")){
                int r = (int)ru;
                editText.setText(r + "");
            }else {
                editText.setText(ru + "");
            }
        }else if(s1.equals("")&&s2.equals("")){
            editText.setText("");
        }
    }
}
