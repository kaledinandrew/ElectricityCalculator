package com.example.andrey.electricitycalculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class ConfirmDeleteRecord extends DialogFragment implements View.OnClickListener {

    public ConfirmDialogListener listener;

    public ConfirmDeleteRecord() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        @SuppressLint("InflateParams")
        View v = inflater.inflate(R.layout.fragment_confirm_delete_record, null);
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        v.findViewById(R.id.btnNo).setOnClickListener(this);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        return v;
    }

    public void onClick(View v){
        if (v.getId() == R.id.btnYes){
            listener.deleteConfirmed();
        }
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (ConfirmDialogListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ConfirmDialogListener");
        }
    }

    public interface ConfirmDialogListener{
        void deleteConfirmed();
    }
}
