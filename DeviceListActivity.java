package com.example.ccy.assistant;

import android.app.Activity;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ExpandedMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


public class DeviceListActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mAdapter;
    private ArrayList<Map<String, String>> mPairedDevicesArray;
    private ArrayList<Map<String, String>> mNewDevicesArray;
    private Toolbar toolbar;
    private Dialog dialog;
    private ExpandableListView expandableListView;
    private SimpleExpandableListAdapter simpleExpandableListAdapter;
    private ArrayList<Map<String, String>> groups;
    private ArrayList<ArrayList<Map<String,String>>> childs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);
        initActionBar();
        mAdapter = BluetoothAdapter.getDefaultAdapter();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        childs = new ArrayList<>();
        groups = new ArrayList<>();
        Object localObject1 = new HashMap();
        ((Map) localObject1).put("NAME", "已配对设备");
        Object localObject2 = new HashMap();
        ((Map) localObject2).put("NAME", "可用设备");
        groups.add((Map) localObject1);
        groups.add((Map) localObject2);
        mPairedDevicesArray = new ArrayList<>();
        mNewDevicesArray = new ArrayList<>();
        childs.add(mPairedDevicesArray);
        childs.add(mNewDevicesArray);
        simpleExpandableListAdapter = new SimpleExpandableListAdapter(this, groups, R.layout.groups, new String[]{"NAME"}, new int[]{R.id.text_groups}, childs, R.layout.childs, new String[]{"NAME", "MAC"}, new int[]{R.id.name_child, R.id.mac_child});
        expandableListView.setOnChildClickListener(listener);
        expandableListView.setAdapter(simpleExpandableListAdapter);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);


        Set<BluetoothDevice> pairedDevices = mAdapter.getBondedDevices();//返回一个集合
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                HashMap localHashMap = new HashMap();
                localHashMap.put("NAME", device.getName());
                localHashMap.put("MAC", device.getAddress());
                this.mPairedDevicesArray.add(localHashMap);
            }
        }
        expandableListView.expandGroup(0);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cancelDiscovery();
            this.unregisterReceiver(mReceiver);
        }
    }

    private void doDiscovery() {

        View layout = View.inflate(getApplicationContext(), R.layout.scan_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        builder.setTitle("搜索");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mAdapter.cancelDiscovery();
                if (!expandableListView.isGroupExpanded(1))
                expandableListView.expandGroup(1);
            }
        });
        builder.setCancelable(false);
        dialog = builder.show();

        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();//取消先前正在发现
        }
        mAdapter.startDiscovery();//开始发现
        if (!mNewDevicesArray.isEmpty()) {
            mNewDevicesArray.clear();
        }


    }

    private ExpandableListView.OnChildClickListener listener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            String address = null;
            if (groupPosition == 0) {
                address = (String) ((Map) mPairedDevicesArray.get(childPosition)).get("MAC");
            } else if (groupPosition == 1) {
                address = (String) ((Map) mPairedDevicesArray.get(childPosition)).get("MAC");
            }

            if (!address.equals("")) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                ChatActivity.getMain().connIng();
                finish();
            }
            return true;
        }

    };


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {//有没配对
                    HashMap localHashMap = new HashMap();
                    localHashMap.put("NAME", device.getName());
                    localHashMap.put("MAC", device.getAddress());
                    mNewDevicesArray.add(localHashMap);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (dialog != null)
                    dialog.dismiss();
                if (!expandableListView.isGroupExpanded(1))
                expandableListView.expandGroup(1);
                toolbar.setTitle("选择一个设备连接");
            }

        }

    };

    public void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.list_toolbar);
        toolbar.setTitle("选择一个设备连接");
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.list_toolbar_menu);//填充菜单
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId) {
                    case R.id.action_scan:
                        doDiscovery();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //写一个menu的资源文件.然后创建就行了.
        getMenuInflater().inflate(R.menu.list_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
