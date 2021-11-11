package com.example.shoppingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.shoppingapp.RegisterActivity.onresetpasswordfragment;


public class sign_in_fragment extends Fragment {
    public sign_in_fragment() {

    }


    private TextView donthaveanaccount;
    private TextView forgotpassword;
    private FrameLayout parentframeLayout;
    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private ImageButton closebtn;
    private Button signinbtn;
    private FirebaseAuth firebaseAuth;
    private String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean disableCloseBtn = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sign_in_fragment, container, false);
        donthaveanaccount=view.findViewById(R.id.tv_dont_have_account_signup);
        forgotpassword=view.findViewById(R.id.signin_forgot_password);
        parentframeLayout=getActivity().findViewById(R.id.register_framelayout);
        email=view.findViewById(R.id.et_email_signin);
        progressBar=view.findViewById(R.id.progressBarsignin);
        password=view.findViewById(R.id.et_password_signin);
        closebtn=view.findViewById(R.id.signin_closebtn);
        signinbtn=view.findViewById(R.id.btn_signin);
        firebaseAuth=FirebaseAuth.getInstance();

        if (disableCloseBtn){
            closebtn.setVisibility(View.GONE);
        }else
        {
            closebtn.setVisibility(View.VISIBLE);
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull  View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new sign_up_fragment());
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onresetpasswordfragment = true;

                setFragment(new ResetPasswordFragment());
            }
        });
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainintent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailandpassword();
            }
        });

    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentframeLayout.getId(),fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slideright,R.anim.slideoutleft);
        fragmentTransaction.commit();
    }

    private void checkinputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(password.getText())){
                signinbtn.setEnabled(true);
                signinbtn.setTextColor(Color.rgb(255,255,255));
            }else{
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signinbtn.setEnabled(false);
            signinbtn.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private void checkemailandpassword(){
        if(email.getText().toString().matches(emailpattern)){
            if(password.length()>=8){
                progressBar.setVisibility(View.VISIBLE);
                signinbtn.setEnabled(false);
                signinbtn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                              if(task.isSuccessful()){
                                  mainintent();
                              }else{
                                  progressBar.setVisibility(View.INVISIBLE);
                                  signinbtn.setEnabled(true);
                                  signinbtn.setTextColor(Color.rgb(255,255,255));
                                  String error=task.getException().getMessage();
                                  Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                              }
                            }
                        });

            }else{
                Toast.makeText(getActivity(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getActivity(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
        }

    }
    private void mainintent(){
        if (disableCloseBtn){
            disableCloseBtn = false;
        }else {
            Intent mainintent = new Intent(getActivity(), MainActivity2.class);
            startActivity(mainintent);
        }
        getActivity().finish();
    }
}