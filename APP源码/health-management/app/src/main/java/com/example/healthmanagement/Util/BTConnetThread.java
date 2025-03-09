package com.example.healthmanagement.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

/**
 * 作为客户端去连接其它设备
 */
public class BTConnetThread extends Thread{
    private static final UUID My_UUID = UUID.fromString(Constant_HM.MY_UUID);
    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;
    private BluetoothAdapter mAdapter;
    private BTDataThread btDataThread;
    private final Handler mHandler;

    public BTConnetThread(BluetoothDevice mDevice, BluetoothAdapter mAdapter, Handler mHandler) {
        this.mDevice = mDevice;
        this.mAdapter = mAdapter;
        this.mHandler = mHandler;
        BluetoothSocket tt = null;
        try{
            tt = mDevice.createRfcommSocketToServiceRecord(My_UUID);
        }catch(IOException ignored){}
        mSocket = tt;
    }

    public void run(){
        mAdapter.cancelDiscovery(); //停止搜索
        try {
            mSocket.connect();
        }catch(Exception ignored){
            mHandler.sendMessage(mHandler.obtainMessage(Constant_HM.MSG_ERROR,ignored));
            try{
                mSocket.close();
            }catch(IOException ignored1){}
            return;
        }
        startBTDataSocket(mSocket);
    }
    
    private void startBTDataSocket(BluetoothSocket socket){
        mHandler.sendEmptyMessage(Constant_HM.MSG_CONNET);
        btDataThread = new BTDataThread(socket,mHandler);
        btDataThread.start();
    }
    
    public void cancel(){
        try{
            mSocket.close();
        }catch(IOException ignored){}
    }

}
