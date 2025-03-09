package com.example.healthmanagement.Frame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.healthmanagement.DB.User;
import com.example.healthmanagement.DB.userDao;
import com.example.healthmanagement.LoginActivity;
import com.example.healthmanagement.R;
import com.example.healthmanagement.Util.Constant_HM;
import com.example.healthmanagement.Util.MToast;
//登录
public class LoginFragment extends Fragment {
    private EditText name,password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,null);
        initView(view);
        view.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id,pass;
                id = name.getText().toString();
                pass = password.getText().toString();
                if (!id.equals("") && !pass.equals("")){
                    userDao ud = new userDao(getContext());
                    User u = new User();
                    if (ud.findUser(id) != null){
                        u= ud.findUser(id).get(0);
                        if(pass.equals(u.getPassword())){
                            LoginActivity.handler.sendMessage(LoginActivity.handler.obtainMessage(112,u.getName()));
                            MToast.mToast(getContext(),"欢迎"+u.getName());
                            Constant_HM.Target_step = u.getStep();
                            Constant_HM.UNAME = u.getName();
                        }else {
                            password.setText("");
                            MToast.mToast(getContext(),"密码错误");
                        }
                    }else
                        MToast.mToast(getContext(),"请先创建");
                }else
                    MToast.mToast(getContext(),"用户名和密码不能为空");
            }
        });
        view.findViewById(R.id.l1).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MToast.mToast(getContext(),"敬请期待!");
            }
        });
        return view;
    }

    private void initView(View view) {
        name = view.findViewById(R.id.login_name);
        password= view.findViewById(R.id.login_password);
    }
    
}