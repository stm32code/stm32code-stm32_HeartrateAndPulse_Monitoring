package com.example.healthmanagement.Util;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BTDataThread extends Thread{

    private final BluetoothSocket mSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private final Handler handler;

    public BTDataThread(BluetoothSocket mSocket, Handler handler) {
        this.mSocket = mSocket;
        this.handler = handler;
        InputStream iput = null;
        OutputStream oput = null;
        try{
            iput= mSocket.getInputStream();
            oput = mSocket.getOutputStream();
        }catch(IOException ignored){}
        inputStream = iput;
        outputStream = oput;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int bytes_len = 0;
        while(true){
            try{
                bytes_len = inputStream.read(buffer);
                if(bytes_len > 0 ){
                    String recieveData = new String(buffer,0,bytes_len, StandardCharsets.UTF_8);
                    Message message = handler.obtainMessage(Constant_HM.MSG_GET_DATA,recieveData);
                    handler.sendMessage(message);
                }
                try {
                    Thread.sleep(1500);
                } catch (Exception ex) {}

            }catch(IOException ignore){
                handler.sendMessage(handler.obtainMessage(Constant_HM.MSG_ERROR,ignore));
                break;
            }
        }
    }

    public void write(byte[] bytes){
        try {
            outputStream.write(bytes);
        }catch(IOException ignore){}
    }

    public void cancel(){
        try {
            mSocket.close();
        }catch(IOException ignore){}
    }
}
