package com.example.healthmanagement.Util;

import android.util.Log;

/***
 * 接收数据处理
 */
public class GetDataDispose {
    private int StepNum; //步数
    private int HeartNum; //心率
    private int PulseNum; //脉搏

    public static String WRONGDATA = "";

    /**
     * 数据分割
     * @param Data
     */
    public void DataDispose(String Data){
        try {
            if(Data.length() == 1) {
                WRONGDATA = Data;
            } else {
                String[] d1 = Data.split("-");
                if(WRONGDATA.equals("")) {
                    setHeartNum(Integer.parseInt(d1[0]));
                } else {
                    setHeartNum(Integer.parseInt(WRONGDATA + d1[0]));
                    WRONGDATA = "";
                }

                setPulseNum(Integer.parseInt(d1[1]));
                char[] dc = d1[2].toCharArray();
                int d2 = 0;
                for(int i=0;i<dc.length-1;i++){
                    d2 += Integer.parseInt(String.valueOf(dc[i]))*Math.pow(10, dc.length-i-2);
//            System.out.println("测试:"+d2);
                }
                setStepNum(d2);
            }

        } catch (Exception ex) {
            System.out.println("接收到异常数据:"+Data);
        }

    }

    public int getStepNum() {
        return StepNum;
    }

    public void setStepNum(int stepNum) {
        StepNum = stepNum;
    }

    public int getHeartNum() {
        return HeartNum;
    }

    public void setHeartNum(int heartNum) {
        HeartNum = heartNum;
    }

    public int getPulseNum() {
        return PulseNum;
    }

    public void setPulseNum(int pulseNum) {
        PulseNum = pulseNum;
    }



}
