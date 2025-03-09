package com.example.healthmanagement.Frame;

import static android.app.Activity.RESULT_OK;
import static com.example.healthmanagement.Util.Constant_HM.LOGIN_STATE;
import static com.example.healthmanagement.Util.Constant_HM.Target_step;
import static com.example.healthmanagement.Util.Constant_HM.UNAME;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.healthmanagement.Adapter.ContentItem;
import com.example.healthmanagement.Adapter.MyAdapter;
import com.example.healthmanagement.DB.userDao;
import com.example.healthmanagement.LoginActivity;
import com.example.healthmanagement.R;

import com.example.healthmanagement.Util.Constant_HM;
import com.example.healthmanagement.Util.MToast;

import java.util.ArrayList;


public class MineFragment extends Fragment implements AdapterView.OnItemClickListener {
    private TextView text;
    private ListView lv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, null);
        initView(view);
        return view;
    }

    private void initView(View v) {
        text = (TextView) v.findViewById(R.id.l_text);
        if (!LOGIN_STATE) {
            v.findViewById(R.id.head_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getContext(), LoginActivity.class), 11);
                }
            });
        } else {
            text.setText(UNAME);
        }
        ArrayList<ContentItem> objects = new ArrayList<>();
        lv = v.findViewById(R.id.mine_listview);
        objects.add(0, new ContentItem("步数目标", (int) Target_step + ""));
        lv.setAdapter(new MyAdapter(getContext(), objects));
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = null;
        if (LOGIN_STATE) {
            switch (position) {
                case 0:
                    alertDialog();
                    break;
            }
        } else {
            MToast.mToast(getContext(), "请先登录");
        }
    }

    private void alertDialog() {
        //创建对话构架
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //获取布局
        View view = View.inflate(getContext(), R.layout.updata_mine, null);
        //获取布局控件
        EditText input = view.findViewById(R.id.updata_input_name);
        Button btn1 = view.findViewById(R.id.updata_t);
        Button btn2 = view.findViewById(R.id.updata_f);

        builder.setTitle("设置目标步数").setView(view);
        //创建对话框
        AlertDialog dialog = builder.create();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDao ud = new userDao(getContext());
                int id = ud.findUser(UNAME).get(0).getId();
                if (Integer.parseInt(input.getText().toString()) > 0) {
                    if (ud.updateUser(id, userDao.updataType.ST, input.getText().toString())) {
                        Target_step = Integer.parseInt(input.getText().toString());
                        System.out.println("修改后步数："+Target_step);
                        MToast.mToast(getContext(), "修改成功");
                        MainFragment.handler.sendEmptyMessage(120);
                        ArrayList<ContentItem> objects = new ArrayList<>();
                        objects.add(0, new ContentItem("步数目标", (int) Target_step + ""));
                        lv.setAdapter(new MyAdapter(getContext(), objects));
                    } else
                        MToast.mToast(getContext(), "保存失败！请重试");
                } else {
                    MToast.mToast(getContext(), "不要偷懒哟");
                }
                dialog.dismiss(); // 对话框消失
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 对话框消失
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            assert data != null;
            ArrayList<ContentItem> objects = new ArrayList<>();
            text.setText(data.getStringExtra("result"));
            objects.add(0, new ContentItem("步数目标", (int) Target_step + ""));
            lv.setAdapter(new MyAdapter(getContext(), objects));
        }
    }
}