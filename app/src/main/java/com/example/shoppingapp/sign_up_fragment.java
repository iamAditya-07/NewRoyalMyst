package com.example.shoppingapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sign_up_fragment extends Fragment {


    public sign_up_fragment() {

    }

    private TextView alreadyhaveanaccount;
    private FrameLayout parentframelayout;
    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmpassword;
    private ImageButton closebutton;
    private Button signupbutton;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static boolean disableCloseBtn = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_fragment, container, false);
        alreadyhaveanaccount = view.findViewById(R.id.tv_already_have_account_signin);
        email=view.findViewById(R.id.signup_email);
        fullname=view.findViewById(R.id.signup_fullname);
        password=view.findViewById(R.id.signup_password);
        confirmpassword=view.findViewById(R.id.signup_confirm_password);
        closebutton=view.findViewById(R.id.signup_close_btn);
        signupbutton=view.findViewById(R.id.btn_signup);
        progressBar=view.findViewById(R.id.progressBarsignup);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        parentframelayout=getActivity().findViewById(R.id.register_framelayout);


        if (disableCloseBtn){
            closebutton.setVisibility(View.GONE);
        }else
        {
            closebutton.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull  View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveanaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new sign_in_fragment());
            }
        });
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainintent();
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputs();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputs();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputs();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmpassword.addTextChangedListener(new TextWatcher()  {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInputs();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkemailandpassword();


            }
        });
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(parentframelayout.getId(),fragment);
        fragmentTransaction.setCustomAnimations(R.anim.slideleft,R.anim.slideoutright);
        fragmentTransaction.commit();
    }
    private void checkInputs(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){
               if(!TextUtils.isEmpty(password.getText()) && password.length()>=8){
                   if(!TextUtils.isEmpty(confirmpassword.getText())){
                       signupbutton.setEnabled(true);
                       signupbutton.setTextColor(Color.rgb(255,255,255));
                   }else{
                       signupbutton.setEnabled(false);
                       signupbutton.setTextColor(Color.argb(50,255,255,255));

                   }
               }else{
                   signupbutton.setEnabled(false);
                   signupbutton.setTextColor(Color.argb(50,255,255,255));

               }
            }else{
                signupbutton.setEnabled(false);
                signupbutton.setTextColor(Color.argb(50,255,255,255));

            }
        }else{
            signupbutton.setEnabled(false);
            signupbutton.setTextColor(Color.argb(50,255,255,255));
        }
    }
    private  void checkemailandpassword(){
        Drawable customerroricon=getResources().getDrawable(R.drawable.ic_baseline_error_24);
        customerroricon.setBounds(0,0,customerroricon.getIntrinsicWidth(),customerroricon.getIntrinsicHeight());

        if(email.getText().toString().matches(emailPattern)){
            if(password.getText().toString().equals(confirmpassword.getText().toString())){
                progressBar.setVisibility(View.VISIBLE);
                signupbutton.setEnabled(false);
                signupbutton.setTextColor(Color.argb(50,255,255,255));

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Map<String,Object> userdata=new HashMap<>();
                            userdata.put("fullname",fullname.getText().toString());
                            userdata.put("email",email.getText().toString());
                            userdata.put("profile","");

                            firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                    .set(userdata)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isSuccessful()) {

                                                CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");


                                                Map<String, Object> wishlistMap = new HashMap<>();
                                                wishlistMap.put("list_size", (long) 0);

                                                Map<String, Object> ratingMap = new HashMap<>();
                                                ratingMap.put("list_size", (long) 0);

                                                Map<String, Object> cartMap = new HashMap<>();
                                                cartMap.put("list_size", (long) 0);

                                                Map<String, Object> myAddressesMap = new HashMap<>();
                                                myAddressesMap.put("list_size", (long) 0);

                                                Map<String, Object> notificationsMap = new HashMap<>();
                                                notificationsMap.put("list_size", (long) 0);


                                                List<String> documentNames = new ArrayList<>();
                                                documentNames.add("MY_WISHLIST");
                                                documentNames.add("MY_RATINGS");
                                                documentNames.add("MY_CART");
                                                documentNames.add("MY_ADDRESSES");
                                                documentNames.add("MY_NOTIFICATIONS");

                                                List<Map<String, Object>> documentFields = new ArrayList<>();
                                                documentFields.add(wishlistMap);
                                                documentFields.add(ratingMap);
                                                documentFields.add(cartMap);
                                                documentFields.add(myAddressesMap);
                                                documentFields.add(notificationsMap);

                                                for (int x = 0; x < documentNames.size(); x++) {
                                                    int finalX = x;
                                                    userDataReference.document(documentNames.get(x))
                                                            .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (finalX == documentNames.size() - 1) {
                                                                    mainintent();
                                                                }
                                                            } else {
                                                                progressBar.setVisibility(View.INVISIBLE);
                                                                signupbutton.setEnabled(true);
                                                                signupbutton.setTextColor(Color.rgb(255, 255, 255));
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }




                                            }else{
                                                String error=task.getException().getMessage();
                                                Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }else{
                            progressBar.setVisibility(View.INVISIBLE);
                            signupbutton.setEnabled(true);
                            signupbutton.setTextColor(Color.rgb(255,255,255));
                            String error=task.getException().getMessage();
                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                confirmpassword.setError("Password doesn't match!",customerroricon);
            }
        }else{
            email.setError("Invalid Email!",customerroricon);
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