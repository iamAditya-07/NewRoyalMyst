package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    public static boolean onresetpasswordfragment;
    public static boolean setSignUpFragment = false;

    static {
        onresetpasswordfragment = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout=findViewById(R.id.register_framelayout);

        if(setSignUpFragment){
            setSignUpFragment = false;
            setdefaultFragment(new sign_up_fragment());
        }else {
            setdefaultFragment(new sign_in_fragment());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){

            sign_in_fragment.disableCloseBtn = false;
            sign_up_fragment.disableCloseBtn = false;
            if(onresetpasswordfragment){
                onresetpasswordfragment = false;
                setFragment(new sign_in_fragment());
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setdefaultFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slideleft,R.anim.slideoutright);
        fragmentTransaction.commit();
    }

}