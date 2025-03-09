package com.example.healthmanagement.Frame;

import static android.app.Activity.RESULT_OK;

import static com.example.healthmanagement.Util.Constant_HM.Target_step;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthmanagement.R;
import com.example.healthmanagement.SportStepUtil.SportsData;
import com.example.healthmanagement.SportStepUtil.SportsView;
import com.example.healthmanagement.Util.BTConnetThread;
import com.example.healthmanagement.Util.BlueTooth;
import com.example.healthmanagement.Util.Constant_HM;
import com.example.healthmanagement.Util.GetDataDispose;
import com.example.healthmanagement.Util.MToast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Set;


public class MainFragment extends Fragment implements OnChartValueSelectedListener {
    public static final int REQUEST_CODE = 0;
    private SportsView sp;
    private LineChart chart, chart2;
    private TextView label_step,text_step;
    private TextView label_hr;
    private int flags_hr = 0;
    private int data_hr = 0;
    public static Handler handler;
    private BlueTooth bt;
    private GetDataDispose dispose = new GetDataDispose();
    private int connect_F = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);
        Message();
        initView(view);
        initBlueTooth();
        return view;
    }
    /**
     * 信息处理
     */
    private void Message(){
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0: //完成步数目标
                        if (!Constant_HM.is_Target_step) {
                            Constant_HM.is_Target_step = true;
                            MToast.mToast(getContext(), "完成");
                            label_step.setText(Constant_HM.Label_step_up);
                        }
                        break;
                    case 1: //未完成步数目标
                        label_step.setText(Constant_HM.Label_step_down);
                        break;
                    case 120: //更新目标步数
                        text_step.setText((int)Target_step+"");
                        step_update(dispose.getStepNum());
                        break;
                    case Constant_HM.MSG_CONNET: //蓝牙连接
                        MToast.mToast(getContext(), "蓝牙连接成功");
                        sp.setConnected(true);
                        System.out.println("MSG_CONNET:连接成功");
                        break;
                    case Constant_HM.MSG_ERROR: //连接错误
                        System.out.println("MSG_ERROR:" + msg.obj.toString());
                        break;
                    case Constant_HM.MSG_GET_DATA: //接收数据
//                        MToast.mToast(getContext(), msg.obj.toString());
                        System.out.println("接收到数据:" + msg.obj.toString());
                        dispose.DataDispose(msg.obj.toString());
                        AddChartData(dispose.getHeartNum(),
                                dispose.getPulseNum(),
                                dispose.getStepNum()
                        );
                        break;
                }
            }
        };
    }

    /**
     * 初始化蓝牙
     */
    private void initBlueTooth() {
        bt = new BlueTooth(); //一定要在handler赋值之后获取
        if(bt.Bt_isEnabled()){
            Log.d("蓝牙状态","已开启蓝牙");
            if (handler == null){
                Log.d("蓝牙状态","她真的为空");
                Message();
            }
            bt.BT_Connet(handler);
        }else{
            //用于启动蓝牙功能的 Intent
            Intent enableBtIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult (enableBtIntent, REQUEST_CODE);
        }
    }

    private void initView(View view) {

//        addEntry();
//        feedMultiple();
//        chart.clearValues(); //清除数据
        label_step = view.findViewById(R.id.bs_tag);
        label_hr = view.findViewById(R.id.pj_hr);
        text_step = view.findViewById(R.id.bs_targt);
        sp = view.findViewById(R.id.sport);
        chart = view.findViewById(R.id.chart1);
        chart2 = view.findViewById(R.id.chart2);
        step_update(0);
        init_lineChart(chart,"HR");  //心率
        init_lineChart(chart2,"DP"); //血压
        AddChartData(0,0,0);
    }

    //步数数据更新
    private void step_update(int num) {
        SportsData sd = new SportsData();
        sd.step = num;//步数
        sd.distance = (int) ((sd.step / 1300.0) * 1000);//公里
        sd.calories = (int) (sd.distance / 90.0);//卡路里
        Message msg = handler.obtainMessage();
        if ((num / Target_step) * 100 < 100) {
            sd.progress = (int) ((num / Target_step) * 100);//进度
            Constant_HM.is_Target_step = false;
            msg.what = 1;
        } else {
            msg.what = 0;
            sd.progress = 100;
        }
        handler.sendMessage(msg);
        sp.setSportsData(sd);
    }

    private void init_lineChart(LineChart chart,String type) {
//        chart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                feedMultiple();
//            }
//        });
        chart.setOnChartValueSelectedListener(this);
        // 开启文本描述
        chart.getDescription().setEnabled(true);
        // 开启触摸手势
        chart.setTouchEnabled(true);
        // 允许缩放和拖动
        chart.setDragEnabled(true); //拖动
        chart.setScaleEnabled(false); //缩放
        chart.setDrawGridBackground(false);
        // 如果禁用，可以分别在x轴和y轴上进行缩放
        chart.setPinchZoom(true);
        // 设置一个替代背景
        //chart.setBackgroundColor(Color.rgb(255, 255, 255));
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        // 添加空数据
        chart.setData(data);
        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);//标签位置
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        if(type.equals("DP"))
            xl.setLabelCount(2);
        else
            xl.setLabelCount(10);

        xl.setAxisLineColor(Color.rgb(248, 248, 255));
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        //leftAxis.setAxisMaximum(200f); //最大条目
        leftAxis.setAxisMinimum(0f);//最小条目
        leftAxis.setLabelCount(4);//设置最大分为几格
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisLineColor(Color.rgb(248, 248, 255));
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    //添加新数据
    private void addEntry(LineChart chart, String type,float num) {
        if (type.equals("HR") | type.equals("DP")) {
            LineData data = chart.getData();
            YAxis leftAxis = chart.getAxisLeft();
            if (data != null) {
                ILineDataSet set = data.getDataSetByIndex(0);
                if (set == null) {
                    if (type.equals("HR"))
                        set = createSet("HR");
                    else
                        set = createSet("DP");
                    data.addDataSet(set);
                }
                switch (type){
                    case "HR":
                        flags_hr += 1;
//                        float ranY = (float) ((Math.random() * 40) + 30f);
                        float ranY = num;
                        data_hr += (int) ranY;
                        int pj_data = data_hr / flags_hr;
                        if (flags_hr == 60) {
                            data_hr = 0;
                            flags_hr = 0;
                        }
                        if (pj_data > 170) {
                            label_hr.setTextColor(getResources().getColor(R.color.darkred));
                        } else {
                            label_hr.setTextColor(getResources().getColor(R.color.white));
                        }
                        label_hr.setText("平均心率 : " + pj_data + "/分");
                        if (chart.getYChartMax() < ranY +30)
                            leftAxis.setAxisMaximum(ranY+30); //最大条目
                        data.addEntry(new Entry(set.getEntryCount(), ranY), 0);
                        // 限制x可见数目
                        chart.setVisibleXRange(90,90);
                        break;
                    case "DP":
//                        float d1 = (float) ((Math.random() * 10) + 30f);
                        float d1 = num;
                        if (chart.getYChartMax() < d1 +30)
                            leftAxis.setAxisMaximum(d1+30); //最大条目
                        data.addEntry(new Entry(set.getEntryCount(), d1), 0);
                        chart.setVisibleXRange(0,2);
                        break;
                }
                data.notifyDataChanged();
                // 提交数据数据改变，更新图表
                chart.notifyDataSetChanged();
                // 移动到最新条目
                chart.moveViewToX(data.getEntryCount());
            }
        }
    }

    private LineDataSet createSet(String type) {
        if (type.equals("HR") | type.equals("DP")) {
            String label_text;
            if (type.equals("HR")) {
                label_text = "心率";
            } else {
                label_text = "脉搏";
            }
            LineDataSet set = new LineDataSet(null, label_text);

            set.setAxisDependency(YAxis.AxisDependency.LEFT);
            set.setColor(ColorTemplate.getHoloBlue()); //折线颜色
            set.setLineWidth(2f);
            set.setCircleRadius(4f);
            set.setFillAlpha(65);//填充透明度
            set.setFillColor(ColorTemplate.getHoloBlue());
            set.setHighLightColor(Color.rgb(124, 117, 117));//高亮颜色
            switch (type) {
                case "HR":
                    set.setDrawValues(false);
                    set.setDrawCircles(false);
                    set.setDrawFilled(true); //充满
                    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    break;
                case "DP":
                    set.setDrawValues(true);
                    set.setDrawCircles(true);
                    set.setDrawFilled(false); //充满
                    set.setMode(LineDataSet.Mode.LINEAR);
                    break;
            }
            return set;
        } else
            return null;
    }

    /**
     * 为三个图表添加新数据
     * data[0] 为心率 ;data[1] 为脉搏; data[2] 为步数
     * @param data
     */
    private void AddChartData(float... data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        addEntry(chart,"HR",data[0]);
                        addEntry(chart2,"DP",data[1]);
                        step_update((int)data[2]);
                    }
                });
            }
        }).start();
    }
//    //以下为模拟测试使用
//    private Thread thread;
//
//    //连续添加数据
//    private void feedMultiple() {
//
//        if (thread != null)
//            thread.interrupt();
//
//        final Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                addEntry(chart,"HR");
//                addEntry(chart2,"DP");
//            }
//        };
//
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 5; i++) {
//                    step_update(5 * i + 100);
//                    getActivity().runOnUiThread(runnable);
//                    try {
//                        Thread.sleep(750);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        thread.start();
//    }

    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
        Log.i("Entry selected", entry.toString());
    }

    @Override
    public void onNothingSelected() {
        //Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK){
            bt.BT_Connet(handler);
            Log.d("蓝牙状态","打开成功");
            MToast.mToast(getContext(),"蓝牙打开成功");
        }else{
            connect_F++;
            if (connect_F < 3)
                initBlueTooth();
            Log.d("蓝牙状态","打开失败");
            MToast.mToast(getContext(),"蓝牙打开失败");
        }
    }
}