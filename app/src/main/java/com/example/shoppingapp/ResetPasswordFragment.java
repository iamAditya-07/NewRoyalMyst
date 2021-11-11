package com.example.shoppingapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private EditText registeredemail;
    private Button resetpasswordbtn;
    private TextView goback;
    private FrameLayout parentframelayout;
    private FirebaseAuth firebaseAuth;
    private ViewGroup emailiconcontainer;
    private ImageView emailicon;
    private TextView emailicontext;
    private ProgressBar progressBar;

    public ResetPasswordFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_reset_password, container, false);
        registeredemail=view.findViewById(R.id.forgotpassword_email);
        resetpasswordbtn=view.findViewById(R.id.resetpassword_btn);
        goback=view.findViewById(R.id.forgotpassword_goback_tv);
        emailiconcontainer=view.findViewById(R.id.forgotpassword_email_icon_container);
        emailicon=view.findViewById(R.id.forgotpassword_emailsicon);
        emailicontext=view.findViewById(R.id.forgotpassword_emailicon_text);
        progressBar=view.findViewById(R.id.forgotpassword_progressbar);
        parentframelayout=getActivity().findViewById(R.id.register_framelayout);
        firebaseAuth=FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registeredemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetpasswordbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(emailiconcontainer);
                emailicontext.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailiconcontainer);
                emailicon.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                resetpasswordbtn.setEnabled(false);
                resetpasswordbtn.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.sendPasswordResetEmail(registeredemail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    ScaleAnimation scaleAnimation=new ScaleAnimation(1,0,1,0,emailicon.getWidth()/2,emailicon.getHeight()/2);
                                    scaleAnimation.setDuration(100);
                                    scaleAnimation.setInterpolator(new AccelerateInterpolator());
                                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                                    scaleAnimation.setRepeatCount(1);
                                    scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            emailicontext.setText("Recovery Email Sent Successfully! Check Your Inbox");
                                            emailicontext.setTextColor(getResources().getColor(R.color.successgreen));
                                            TransitionManager.beginDelayedTransition(emailiconcontainer);
                                            emailicon.setVisibility(View.VISIBLE);

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                            emailicon.setImageResource(R.drawable.green_email);

                                        }
                                    });
                                    emailicon.startAnimation(scaleAnimation);
                                }else{
                                    String error=task.getException().getMessage();
                                    resetpasswordbtn.setEnabled(true);
                                    resetpasswordbtn.setTextColor(Color.rgb(255,255,255));

                                   emailicontext.setText(error);
                                   emailicontext.setTextColor(getResources().getColor(R.color.red));
                                    TransitionManager.beginDelayedTransition(emailiconcontainer);
                                    emailicontext.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.GONE);

                            }
                        });
            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new sign_in_fragment());
            }
        });
    }
    private void checkInputs(){
        if(TextUtils.isEmpty(registeredemail.getText())){
            resetpasswordbtn.setEnabled(false);
            resetpasswordbtn.setTextColor(Color.argb(50,255,255,255));
        }else{
            resetpasswordbtn.setEnabled(true);
            resetpasswordbtn.setTextColor(Color.rgb(255,255,255));
        }
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentframelayout.getId(),fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slideleft,R.anim.slideoutright);
        fragmentTransaction.commit();
    }
}