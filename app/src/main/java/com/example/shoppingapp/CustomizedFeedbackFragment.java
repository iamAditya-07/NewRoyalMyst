package com.example.shoppingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class CustomizedFeedbackFragment extends Fragment {

    LinearLayout facesLinearLayout;
    ImageView firstfaceicon,secondfaceicon,thirdfaceicon,fourthfaceicon,fifthfaceicon;
    EditText feedbackMessageEdittext;
    Button submitFeedbackButton;



    public CustomizedFeedbackFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customized_feedback, container, false);

        facesLinearLayout = view.findViewById(R.id.faces_linear_layout);
        firstfaceicon = view.findViewById(R.id.firstfaceicon);
        secondfaceicon = view.findViewById(R.id.secondfaceicon);
        thirdfaceicon = view.findViewById(R.id.thirdfaceicon);
        fourthfaceicon = view.findViewById(R.id.fourthfaceicon);
        fifthfaceicon = view.findViewById(R.id.fifthfaceicon);

        feedbackMessageEdittext = view.findViewById(R.id.feedback_message_edittext);

        submitFeedbackButton = view.findViewById(R.id.feedback_submit_btn);

        firstfaceicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return  view;
    }
}