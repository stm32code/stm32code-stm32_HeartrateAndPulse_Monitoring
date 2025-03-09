package com.example.healthmanagement.Frame;

import static java.lang.Math.random;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.healthmanagement.DB.User;
import com.example.healthmanagement.DB.userDao;
import com.example.healthmanagement.R;
import com.example.healthmanagement.Util.MToast;
//注册
public class SigninFragment extends Fragment {
    private EditText name,password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signin,null);
        initView(view);
        view.findViewById(R.id.sign_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id,pass;
                id = name.getText().toString();
                pass = password.getText().toString();
                if (!id.equals("") && !pass.equals("")){
                    userDao ud = new userDao(getContext());
                    User u = new User();
                    u.setId((int)(Math.random()*50000+1234));
                    u.setPassword(pass);
                    u.setName(id);
                    if (ud.addUser(u)){
                        password.setText("");
                        MToast.mToast(getContext(),"创建成功");
                    }else
                        MToast.mToast(getContext(),"创建失败");
                }else
                    MToast.mToast(getContext(),"用户名和密码不能为空");
            }
        });
        return view;
    }
    private void initView(View view) {
        name = view.findViewById(R.id.sign_name);
        password= view.findViewById(R.id.sign_password);
    }


}