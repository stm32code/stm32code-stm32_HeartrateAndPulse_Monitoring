package com.example.healthmanagement.Util;

import com.example.healthmanagement.R;

public class Constant_HM {
    public static double Target_step = 5000.0; //目标步数
    public static boolean is_Target_step = false;  //是否达标
    public static String Label_step_up = "恭喜你完成了目标，给自己一个奖励吧！";
    public static String Label_step_down = "革命尚未成功，同志还需努力！";
    public static boolean is_BT_connect = false; //蓝牙是否连接
    public static String MY_UUID ="00001101-0000-1000-8000-00805F9B34FB";//"00001101-0000-1000-8000-00805F9B34FB";
    public static final int MSG_GET_DATA = 101; //获取到数据
    public static final int MSG_ERROR = 55;   // 错误
    public static final int MSG_CONNET = 200; // 连接到服务器
    public static boolean LOGIN_STATE = false; //登录状态
    public static String UNAME = "点 我 登 录";
}
