package com.example.healthmanagement.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.Set;

public class BlueTooth {
    private BluetoothAdapter mAdapter;
    private BluetoothDevice mDevice = null;
    public BlueTooth(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    /**
     * 蓝牙是否打开
     * @return true - 打开  false -关闭
     */
    public boolean Bt_isEnabled(){
        return mAdapter.isEnabled();
    }

    /**
     * 蓝牙连接-指定设备
     * @param handler
     */
    public void BT_Connet(Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAdapter = BluetoothAdapter.getDefaultAdapter(); //获取本地配对数据
                out:
                while(true){
                    Set<BluetoothDevice> pairedDevices=mAdapter.getBondedDevices();
                    //已配对设备列表存在
                    if(pairedDevices.size() > 0) {
                        //列表内循环查找
                        for(BluetoothDevice device : pairedDevices) {
                            if(device.getName().equals("HC-05")){
                                Log.i("成功匹配设备", "设备Name：" + device.getName() + "   设备地址：" + device.getAddress());
                                mDevice = mAdapter.getRemoteDevice(device.getAddress()); //要连接的设备的MAC地址
                                BTConnetThread thread = new BTConnetThread(mDevice, mAdapter, handler);
                                thread.start();
                                break out;
                            }
                            else{
                                Log.i("搜寻设备","设备Name："+device.getName()+"   设备地址："+device.getAddress());
                            }
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
