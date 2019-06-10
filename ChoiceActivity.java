package cn.edu.sdut.app5;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ChoiceActivity extends AppCompatActivity {

    Button btnGoListView,btnTestDownload,btnTestWebView;
    Button btnTestDate,btnTestTime,btnTestChron;
    Button btnPhoto;
    ImageView imgPhoto;
    EditText txtDateTime;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ProgressDialog progressDialog;
    Handler handler;
    ProgressBar progressC,progressP;
    Chronometer chronometer;
    ActionBar actionBar;
    long t1=System.currentTimeMillis();
    int countTimer;
    long t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        chronometer= ((Chronometer) findViewById(R.id.jishiqi));
        progressC= ((ProgressBar) findViewById(R.id.proC));
        progressP= ((ProgressBar) findViewById(R.id.proH));
        btnGoListView= ((Button) findViewById(R.id.btnTestListView));
        btnTestDate= ((Button) findViewById(R.id.btnTestDatePicker));
        btnTestTime= ((Button) findViewById(R.id.btnTestTimePicker));
        btnTestDownload= ((Button) findViewById(R.id.btnTestDownload));
        btnTestWebView= ((Button) findViewById(R.id.btnTestWebview));
        btnTestChron= ((Button) findViewById(R.id.btnTestChr));
        btnPhoto= ((Button) findViewById(R.id.btnTakePhoto));
        imgPhoto= ((ImageView) findViewById(R.id.imgPhoto));


        txtDateTime= ((EditText) findViewById(R.id.txtDateTime));
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在下载...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what==100){
                    progressDialog.dismiss();
                    progressC.setVisibility(View.GONE);
                    progressP.setVisibility(View.GONE);
                    Toast.makeText(ChoiceActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    //启动安装
                }else{
                    progressDialog.setProgress(msg.what);
                    progressP.setProgress(msg.what);
                }
            }
        };

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                //启动后，周期执行
                countTimer--;
                if(countTimer>0){
                    btnTestChron.setText(countTimer+ "秒后重新发送");
                }else{
                    btnTestChron.setEnabled(true);
                    btnTestChron.setText("发送验证码");
                    chronometer.stop();
                }
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });

        btnTestChron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送验证码，开始计时
                countTimer=10;
                btnTestChron.setEnabled(false);
                chronometer.start();
            }
        });
        Calendar calendar=Calendar.getInstance();
        datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                txtDateTime.setText(year+"年"+(month+1)+"月"+dayOfMonth+"日");
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txtDateTime.setText(hourOfDay+":"+minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

        btnTestWebView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoiceActivity.this,TestWebViewActivity.class);
                startActivity(intent);
            }
        });
        btnTestDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progressC.setVisibility(View.VISIBLE);
                progressP.setVisibility(View.VISIBLE);
                new DownloadThread().start();
            }
        });
        btnTestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        btnTestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });
        btnGoListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("net.jsjxy.app.LISTVIEW");
                startActivity(intent);

//                Intent intent=new Intent(ChoiceActivity.this,TestListViewActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){ //如果拍照返回
            if(resultCode==RESULT_OK){
                Bundle bundle=data.getExtras();
                Bitmap bitmap= (Bitmap) bundle.get("data");
                imgPhoto.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                ChoiceActivity.this.finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            t2=System.currentTimeMillis();
            if((t2-t1)>1500){
                Toast.makeText(this, "再按一下退出", Toast.LENGTH_SHORT).show();
                t1=t2;
            }else{
                ChoiceActivity.this.finish();
            }
        }

        return true;
    }

    class DownloadThread extends Thread{
        @Override
        public void run() {
            super.run();
            for(int i=1;i<=100;i++){
                Message message=new Message();
                try {
                    Thread.sleep(100);
                }catch (Exception ee){}
                message.what=i;
                handler.sendMessage(message);
            }
        }
    }
}
