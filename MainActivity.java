package cn.edu.sdut.app5;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    TextView txtBlue;
    Button btnLogin,btnRegister;
    ToggleButton toggleButton;
    EditText txtUserName,txtUserPassword;
    CheckBox checkBox;
    RadioButton radioAdmin,radioUser;
    Resources resources;
    Spinner spinner;
    String xueyuan[];
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    Handler handler;
    ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        txtUserName.setText("1");
        txtUserPassword.setText("1");
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        resources=getResources();
        xueyuan= resources.getStringArray(R.array.xueyuan);

        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("正在登录...");
        progressDialog.setCancelable(false);

        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //接收线程返回的Msg
                progressDialog.dismiss();
                switch (msg.what){
                    case 1:
                        //登录成功，跳转到功能选择Activity
                        Intent intent=new Intent(MainActivity.this,ChoiceActivity.class);
                        startActivity(intent);
//                        MainActivity.this.finish();
                        break;
                    case 0:
                        Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,xueyuan);

        builder=new AlertDialog.Builder(this);
//        builder.setTitle("提示信息");
//        builder.setMultiChoiceItems(xueyuan,[],null);
//        builder.setSingleChoiceItems(xueyuan, 0, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, xueyuan[which], Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setMessage(" 是否要注册？");
//        builder.setItems(xueyuan, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, xueyuan[which], Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("否",null);
//        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//            }
//        });

        spinner.setAdapter(arrayAdapter);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtBlue.setVisibility(View.GONE);

                String str=spinner.getSelectedItem().toString();
                int pos=spinner.getSelectedItemPosition();
                Toast.makeText(MainActivity.this, xueyuan[pos], Toast.LENGTH_SHORT).show();
                if(toggleButton.isChecked()){

                }
                if(checkBox.isChecked()){
                    //保存用户名
                }else{
                    //清空保存数据
                }
                if(radioAdmin.isChecked()){
                    //管理员用户登录
                }else{
                    //普通用户登录
                }
                //开始登录线程,开启progressDialog
                progressDialog.show();
                new LoginThread().start();
            }
        });

//        spinner.setOnItemClickListener(null);不支持
//        spinner.setOnClickListener(null);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str=xueyuan[position];
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtBlue.setVisibility(View.VISIBLE);
                View view=getLayoutInflater().inflate(R.layout.dialog_layout,null);
                final EditText txtDialogUserName,txtDialogUserPwd;
                txtDialogUserName= ((EditText) view.findViewById(R.id.txtUserName));
                txtDialogUserPwd= ((EditText) view.findViewById(R.id.txtUserPassword));
                builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String xm=txtDialogUserName.getText().toString();
                        String mm=txtDialogUserPwd.getText().toString();
                        //启动注册过程
                        Toast.makeText(MainActivity.this, "注册成功"+xm, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setView(view);
                builder.create().show();

            }
        });

        txtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String str=txtUserName.getText().toString();
                Toast.makeText(MainActivity.this,str,Toast.LENGTH_LONG).show();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        radioUser.setOnCheckedChangeListener(null);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(MainActivity.this, "音乐开始播放", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "音乐停止播放", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.optionmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_about:
                Toast.makeText(this, "这是我的第一个练习", Toast.LENGTH_SHORT).show();
                break;
            case R.id.option_help:

                break;
            case R.id.option_exit:
                MainActivity.this.finish();
                break;
            case android.R.id.home:
                MainActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initView(){
        spinner= ((Spinner) findViewById(R.id.spinnerXueYuan));
        txtUserName=(EditText)findViewById(R.id.txtUserName);
        txtUserPassword=(EditText) findViewById(R.id.txtUserPassword);
        btnLogin= (Button) findViewById(R.id.btnLogin);
        btnRegister= (Button) findViewById(R.id.btnRegister);
        txtBlue=(TextView) findViewById(R.id.txtViewBlue);
        toggleButton=(ToggleButton) findViewById(R.id.toggleBtn);
        checkBox= ((CheckBox) findViewById(R.id.checkRememberMe));
        radioAdmin= ((RadioButton) findViewById(R.id.radioAdmin));
        radioUser= ((RadioButton) findViewById(R.id.radioUser));
    }

    class LoginThread extends Thread{
        @Override
        public void run() {
            super.run();
            String xm=txtUserName.getText().toString();
            String mm=txtUserPassword.getText().toString();
//            Message message=new Message();
            Message message=handler.obtainMessage(2);
            try {
                Thread.sleep(500);
            }catch (Exception ee){}
            if(xm.equals("1") && mm.equals("1")){
                handler.sendEmptyMessage(1);
            }else{
                handler.sendEmptyMessage(0);
            }
//            handler.sendMessage(message);
        }
    }
}
