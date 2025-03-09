package com.example.healthmanagement.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.healthmanagement.Frame.LoginFragment;
import com.example.healthmanagement.Frame.SigninFragment;


public class LoginViewPagerAdapter extends FragmentPagerAdapter {
    Fragment[] conFragment = new Fragment[2];

    public LoginViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {

        super(fm, behavior);
        //为Fragment数组绑定
        conFragment[0]= new LoginFragment();
        conFragment[1]= new SigninFragment();

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return conFragment[position];
    }

    @Override
    public int getCount() {
        return conFragment.length;
    }
}
