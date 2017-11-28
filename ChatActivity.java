package com.example.ccy.assistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDRESS = "device_address";
    public static final String TOAST = "toast";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private String mConnectedDeviceName = null;
    private String mConnectedDeviceAddress = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;
    private boolean isStart;
    List<String> titles = new ArrayList<>();
    private Toolbar toolbar;
    MenuItem mItem;
    private MyViewPager viewPager;
    private FragmentAdapter adapter;
    private List<Fragment> fragments;
    public static ChatActivity main;
    private MyFragment chatFragment;
    private KeyFragment keyFragment;
    private ToggleFragment toggleFragment;
    private GravityFragment gravityFragment;
    private Dialog dialog;
    private boolean isCharSend = true;
    private boolean isCharRec = true;
    private List<ChatMsgEntity> chatMsg;
    private ViewPagerIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStart = false;
        initView();
        initActionBar();
        main = this;
    }

    @Override
    public void onStart() {

        requestPermission();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {

            if (mChatService == null) setupChat();
        }
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        isCharSend = preferences.getBoolean("chat_isCharSend", false);
        isCharRec = preferences.getBoolean("chat_isCharRec", false);
        super.onStart();
    }

    public List<ChatMsgEntity> getChatMsg() {
        return chatMsg;
    }

    @Override
    public synchronized void onResume() {
        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                mChatService.start();
            }
        }
        super.onResume();
    }

    private void setupChat() {

        mChatService = new BluetoothChatService(this, mHandler);
    }

    @Override
    public synchronized void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {

        super.onStop();

    }


    @Override
    public void onDestroy() {
        if (mChatService != null)
            mChatService.stop();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("chat_isCharSend", isCharSend);
        editor.putBoolean("chat_isCharRec", isCharRec);
        super.onDestroy();
    }

    private void ensureDiscoverable() {

        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        } else {
            Toast.makeText(ChatActivity.this, "设置可见性错误，请重试", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        chatMsg = new ArrayList<>();
        viewPager = (MyViewPager) findViewById(R.id.pager);
        viewPager.setNoScroll(true);
        fragments = new ArrayList<>();
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
        chatFragment = new MyFragment();
        keyFragment = new KeyFragment();
        toggleFragment = new ToggleFragment();
        gravityFragment = new GravityFragment();
        fragments.add(chatFragment);
        fragments.add(keyFragment);
        fragments.add(toggleFragment);
        fragments.add(gravityFragment);
        titles.add("聊天");
        titles.add("键盘");
        titles.add("开关");
        titles.add("重力感应");
        mIndicator.setTabItemTitles(titles);
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        mIndicator.setViewPager(viewPager, 0);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "本设备不支持蓝牙", Toast.LENGTH_LONG).show();
            isStart = false;
            finish();
            return;
        }

    }

    public void sendMsg(String msg) {
        if (!isStart) {
            Intent serverIntent = new Intent(ChatActivity.this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            return;
        }

        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            isStart = false;
            return;
        }

        if (msg.length() > 0) {
            byte[] send;
            if (!isCharSend) {
                msg = msg.replaceAll(" ", "");
                send = getHexBytes(msg);
            } else {
                send = msg.getBytes();
            }
            mChatService.write(send);

        }
    }

    public void sendMsgD(String msg) {

        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            isStart = false;
            return;
        }
        if (msg.length() > 0) {
            byte[] send;
            if (isCharSend)
                send = msg.getBytes();
            else {
                msg = msg.replaceAll(" ", "");
                send = getHexBytes(msg);//16进制
            }
            mChatService.write(send);

        }

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            if (dialog != null) {
                                dialog.dismiss();
                            }
                            String str = getResources().getString(R.string.title_connected_to);
                            toolbar.setTitle(str + mConnectedDeviceName);
                            mItem.setTitle("断开");
                            isStart = true;

                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            toolbar.setTitle(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            toolbar.setTitle("未连接");
                            if (mItem != null)
                                mItem.setTitle("连接");
                            gravityFragment.unregister();
                            isStart = false;
                            break;
                        default:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage;
                    if (isCharSend)
                        writeMessage = new String(writeBuf);
                    else
                        writeMessage = bytesToHexString(writeBuf);
                    ChatMsgEntity msgEntity = new ChatMsgEntity();
                    msgEntity.setName("我" + ":");
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String time = formatter.format(curDate);
                    msgEntity.setDate(time);
                    msgEntity.setText(writeMessage);
                    msgEntity.setMsgType(true);
                    chatMsg.add(msgEntity);
                    chatFragment.displayMsg();
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage;
                    if (isCharRec)
                        readMessage = new String(readBuf, 0, msg.arg1);
                    else
                        readMessage = bytesToHexString(readBuf, msg.arg1);
                    ChatMsgEntity msgEntity1 = new ChatMsgEntity();
                    msgEntity1.setName(mConnectedDeviceName + ":");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm:ss");
                    Date curDate1 = new Date(System.currentTimeMillis());
                    String time1 = formatter1.format(curDate1);
                    msgEntity1.setDate(time1);
                    msgEntity1.setText(readMessage);
                    msgEntity1.setMsgType(false);
                    chatMsg.add(msgEntity1);
                    chatFragment.displayMsg();

                    break;
                case MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    mConnectedDeviceAddress = msg.getData().getString(DEVICE_ADDRESS);
                    Toast.makeText(getApplicationContext(), "成功连接到:"
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    if ("无法连接设备请重试".equals(msg.getData().getString(TOAST))) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:

                if (resultCode == Activity.RESULT_OK) {

                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    gravityFragment.register();
                    mChatService.connect(device);

                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable bluetooth or an error occured
                    isStart = false;
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    private byte[] getHexBytes(String message) {//转化为16进制发送

        if (message.length() % 2 != 0) {
            StringBuffer buffer = new StringBuffer(message);
            buffer.insert(message.length() - 1, '0');
            message = buffer.toString();
        }

        int len = message.length() / 2;
        char[] chars = message.toCharArray();
        String[] hexStr = new String[len];
        byte[] bytes = new byte[len];
        for (int i = 0, j = 0; j < len; i += 2, j++) {
            hexStr[j] = "" + chars[i] + chars[i + 1];
            bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
        }
        return bytes;
    }

    private String bytesToHexString(byte[] bytes) {
        String result = "";
        for (int i = 0; i < bytes.length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xff);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            if (i != 0)
                result += " 0x" + hexString.toUpperCase();
            else
                result += "0x" + hexString.toUpperCase();
        }

        return result;
    }

    private String bytesToHexString(byte[] bytes, int length) {
        String result = "";
        for (int i = 0; i < length; i++) {
            String hexString = Integer.toHexString(bytes[i] & 0xff);
            if (hexString.length() == 1) {
                hexString = '0' + hexString;
            }
            if (i != 0)
                result += " 0x" + hexString.toUpperCase();
            else
                result += "0x" + hexString.toUpperCase();
        }
        return result;
    }

    public void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle("未连接");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.my_toolbar_menu);//填充菜单
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            private TextView rec;
            private TextView send;
            private TextView name;
            private TextView address;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId) {
                    case R.id.action_conn:
                        if (item.getTitle().equals("断开")) {
                            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ChatActivity.this);
                            builder.setTitle("断开连接");
                            builder.setMessage("你想要与“" + mConnectedDeviceName + "”断开连接吗?");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mChatService.stop();
                                    mItem.setTitle("连接");
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.show();

                        } else {
                            Intent serverIntent = new Intent(ChatActivity.this, DeviceListActivity.class);
                            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

                        }


                        break;
                    case R.id.action_discovery:
                        ensureDiscoverable();
                        break;
                    case R.id.action_about: {
                        Intent intent = new Intent(ChatActivity.this, ActivityAbout.class);
                        startActivity(intent);
                    }
                    break;
                    case R.id.action_device:
                        final View layout = View.inflate(ChatActivity.this, R.layout.device_dialog, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                        address = (TextView) layout.findViewById(R.id.device_address);
                        name = (TextView) layout.findViewById(R.id.device_name);
                        send = (TextView) layout.findViewById(R.id.device_send);
                        rec = (TextView) layout.findViewById(R.id.device_rec);
                        if (mConnectedDeviceAddress != null)
                            address.setText(mConnectedDeviceAddress);
                        if (mConnectedDeviceName != null)
                            name.setText(mConnectedDeviceName);
                        send.setText("" + mChatService.getSend());
                        rec.setText("" + mChatService.getRec());
                        builder.setView(layout);
                        builder.setTitle("连接到:" + mConnectedDeviceName);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                        break;
                    case R.id.action_clean:
                        if (!chatFragment.isMsgEmpty()) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ChatActivity.this);
                            builder1.setTitle("清理消息");
                            builder1.setMessage("你想要清除消息内容吗?");
                            builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    chatFragment.clearMsg();
                                }
                            });
                            builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder1.show();
                        }
                        break;
                    case R.id.action_message: {
                        Intent intent = new Intent(ChatActivity.this, MsgActivity.class);
                        startActivity(intent);
                    }
                    break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //写一个menu的资源文件.然后创建就行了.
        getMenuInflater().inflate(R.menu.my_toolbar_menu, menu);
        mItem = menu.findItem(R.id.action_conn);
        return super.onCreateOptionsMenu(menu);
    }

    public static ChatActivity getMain() {
        return main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("蓝牙串口");
            builder.setMessage("你想要退出吗?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void connIng() {
        final View layout = View.inflate(getApplicationContext(), R.layout.conn_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle("连接");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mChatService.stop();
            }
        });
        dialog = builder.show();

    }


    public void setIsCharSend(boolean isCharSend) {
        this.isCharSend = isCharSend;
    }

    public void setIsCharRec(boolean b) {
        isCharRec = b;
    }

    public boolean getIsCharSend() {
        return isCharSend;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//请求权限
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
            10);
//判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
    android.Manifest.permission.READ_CONTACTS)) {
        Toast.makeText(getApplicationContext(), "搜索蓝牙需要，请务必打开", Toast.LENGTH_LONG).show();
    }
}
        }
                }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void startDeviceActivity() {
        Intent serverIntent = new Intent(ChatActivity.this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);

    }

    public boolean getIsStart() {
        return isStart;
    }
}

