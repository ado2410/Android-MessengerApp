package com.example.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MessageMenuFragment extends BottomSheetDialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_menu, container);

        Button btnRecall = (Button) view.findViewById(R.id.fragment_message_menu_btn_recall);
        MessageActivity messageActivity = (MessageActivity) getActivity();

        btnRecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageActivity.recallMessage();
                dismiss();
            }
        });
        return view;
    }
}