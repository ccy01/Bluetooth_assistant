package com.example.ccy.assistant;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.security.cert.CertPathBuilderException;


/**
 * 关于窗口
 * Created by ccy on 2017/5/14.
 */

public class ActivityAbout extends AppCompatActivity {
    private Toolbar toolbar;
    private Button bt;
    private LinearLayout ll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolBar();
        ll = (LinearLayout) findViewById(R.id.ll_ok);
        bt = (Button) findViewById(R.id.bt);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAbout.this);
                builder.setTitle("谢谢");
                builder.setMessage("我会再接再厉的");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAbout.this);
                builder.setTitle("啊");
                builder.setMessage("有什么问题我会继续改正的");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.about_toolbar);
        toolbar.setTitle("蓝牙串口助手");
        toolbar.setSubtitle("作者:ccy");
        setSupportActionBar(toolbar);//弄到这个后面才可以监听
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {


        super.onStop();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
