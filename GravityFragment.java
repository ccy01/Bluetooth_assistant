package com.example.ccy.assistant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 加速度传感器窗口
 * Created by ccy on 2017/5/17.
 */

public class GravityFragment extends Fragment implements View.OnLongClickListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private MySensorListener listener;
    private TextView up;
    private TextView down;
    private TextView right;
    private TextView left;
    private TextView stop;
    private View layout;
    private RadioButton rb_char;
    private RadioButton rb_16;
    private EditText et_msg;
    private TextView start;
    private AlertDialog.Builder builder;
    private CarBean[] carBeans = new CarBean[5];
    private Button tell;
    private String temp = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.acitivy_gravity, container, false);
        initView(view);
        restoreStateFromArguments();
        return view;

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.up:
                v.setTag(0);
                showEditDialog(v);
                break;
            case R.id.down:
                v.setTag(1);

                showEditDialog(v);
                break;
            case R.id.left:
                v.setTag(2);

                showEditDialog(v);
                break;
            case R.id.right:
                v.setTag(3);

                showEditDialog(v);
                break;
            case R.id.stop:
                v.setTag(4);
                showEditDialog(v);
                break;
        }
        return false;
    }

    private class MySensorListener implements SensorEventListener {

        float xValue;
        float yValue;
        float x = 0f;
        float y = 0f;

        @Override
        public void onSensorChanged(SensorEvent event) {
            xValue = event.values[0];//X轴加速度
            yValue = event.values[1];//y轴加速度

            if ((Math.abs(xValue - x) - Math.abs(yValue - y)) > 0.0) {
                if ((xValue - x) > 4) {
                    down.setText("后退");
                    up.setText("");
                    stop.setText("");
                    left.setText("");
                    right.setText("");
                    writeCom(1);
                    x = xValue;
                } else if ((xValue - x) < -4) {
                    up.setText("前进");
                    stop.setText("");
                    down.setText("");
                    left.setText("");
                    right.setText("");
                    writeCom(0);
                    x = xValue;

                }
            } else if ((Math.abs(yValue - y) - Math.abs(xValue - x)) > 0.0) {
                if ((yValue - y) > 4) {
                    right.setText("右转");
                    up.setText("");
                    stop.setText("");
                    down.setText("");
                    left.setText("");
                    writeCom(3);
                    y = yValue;
                } else if ((yValue - y) < -4) {
                    left.setText("左转");
                    down.setText("");
                    up.setText("");
                    stop.setText("");
                    right.setText("");
                    writeCom(2);
                    y = yValue;

                }
            }

        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private void initView(View view) {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);//加速度传感器
        listener = new MySensorListener();
        tell = (Button) view.findViewById(R.id.tell);
        down = (TextView) view.findViewById(R.id.down);
        up = (TextView) view.findViewById(R.id.up);
        left = (TextView) view.findViewById(R.id.left);
        right = (TextView) view.findViewById(R.id.right);
        stop = (TextView) view.findViewById(R.id.stop);
        start = (TextView) view.findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getText().toString().equals("开始感应")) {
                    if (!ChatActivity.getMain().getIsStart()) {
                        ChatActivity.getMain().startDeviceActivity();
                    } else {
                        register();
                    }

                } else if (start.getText().toString().equals("停止感应")) {
                    unregister();
                }
            }
        });
        tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("重力感应说明");
                builder.setMessage("先打开重力感应按钮,将手机向前倾45度为前进(其实可以不前倾那么大角度,自己掌握),将手机后倾是后退,同理左倾向左，右倾向右，还有平放为停止,祝您愉快！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down.setText("");
                up.setText("");
                left.setText("");
                right.setText("");
                writeCom(4);
            }
        });
        up.setOnLongClickListener(this);
        left.setOnLongClickListener(this);
        right.setOnLongClickListener(this);
        down.setOnLongClickListener(this);
        stop.setOnLongClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(listener);//取消监听
    }

    private void showEditDialog(final View v) {

        layout = View.inflate(getContext(), R.layout.gravity_dialog, null);
        rb_char = (RadioButton) layout.findViewById(R.id.rb_key_char);
        rb_16 = (RadioButton) layout.findViewById(R.id.rb_key_16);
        et_msg = (EditText) layout.findViewById(R.id.et_msg);
        final CustomTextWatcher watcher = new CustomTextWatcher(et_msg);
        et_msg.addTextChangedListener(watcher);
        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("按钮编辑");
        builder.setView(layout);
        if (carBeans[(Integer) v.getTag()] != null) {
            CarBean t = carBeans[(Integer) v.getTag()];
            et_msg.setText(t.getEt_msg());
            rb_char.setChecked(t.isRb_char());
            rb_16.setChecked(t.isRb_16());
            watcher.setFormat(t.isRb_16());
        }
        rb_16.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                watcher.setFormat(isChecked);
                et_msg.setText("");
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {//完成一些事情
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CarBean carBean = new CarBean();
                carBean.setEt_msg(et_msg.getText().toString());
                carBean.setRb_char(rb_char.isChecked());
                carBean.setRb_16(rb_16.isChecked());
                carBeans[(Integer) v.getTag()] = null;
                carBeans[(Integer) v.getTag()] = carBean;

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();//关闭窗口
            }
        });
        builder.show();


    }

    private void saveStateToArguments() {
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        for (int i = 0; i < 5; i++) {
            if (carBeans[i] == null) {
                break;
            }
            editor.putString("car_msg" + i, carBeans[i].getEt_msg());
            editor.putBoolean("is_char_check_car" + i, carBeans[i].isRb_char());
            editor.putBoolean("is_16_check_car" + i, carBeans[i].isRb_16());
        }
        editor.apply();

    }

    private void restoreStateFromArguments() {

        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        for (int i = 0; i < 5; i++) {
            CarBean carBean = new CarBean();
            carBean.setEt_msg(preferences.getString("car_msg" + i, ""));
            carBean.setRb_char(preferences.getBoolean("is_char_check_car" + i, false));
            carBean.setRb_16(preferences.getBoolean("is_16_check_car" + i, false));
            carBeans[i] = carBean;

        }

    }

    public void writeCom(int i) {
        if (temp.equals(carBeans[i].getEt_msg())) {
            return;
        }
        if (carBeans[i] != null) {
            if (!carBeans[i].getEt_msg().equals("")) {
                if (carBeans[i].isRb_char()) {
                    ChatActivity.getMain().setIsCharSend(true);
                } else {
                    ChatActivity.getMain().setIsCharSend(false);
                }
                ChatActivity.getMain().sendMsgD(carBeans[i].getEt_msg());
                temp = carBeans[i].getEt_msg();
            }
        }
    }


    public void register() {
        if (sensorManager != null) {
            start.setText("停止感应");
            sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public void unregister() {
        if (sensorManager != null) {
            start.setText("开始感应");
            sensorManager.unregisterListener(listener);
        }
    }

}
