package com.example.andrey.electricitycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OneRateCounter extends Fragment implements View.OnClickListener {

    private static final String TAG = "OneRateCounter";

    SharedPreferences sPref;
    AutoCompleteTextView etLocation;
    EditText etRate;
    EditText etInputPrevious, etInputCurrent;
    TextView tvDelta, tvSum, tvTotal;
    Boolean isCalculated;

    String SINGLE_RATE = "0";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one_rate_counter, container,
                false);

        if (getActivity() != null) getActivity().setTitle("Однотарифный счетчик");
        isCalculated = false;

        Button saveRates = view.findViewById(R.id.saveOneRate);
        Button calculate = view.findViewById(R.id.buttonCalculateOneRate);
        Button saveRecord = view.findViewById(R.id.saveOneRateRecord);

        calculate.setOnClickListener(this);
        saveRates.setOnClickListener(this);
        saveRecord.setOnClickListener(this);

        etRate = view.findViewById(R.id.rateSingle);
        etInputPrevious = view.findViewById(R.id.inputPreviousSingle);
        etInputCurrent = view.findViewById(R.id.inputCurrentSingle);
        etLocation = view.findViewById(R.id.inputLocationSingle);

        tvDelta = view.findViewById(R.id.deltaSingle);
        tvSum = view.findViewById(R.id.sumSingle);
        tvTotal = view.findViewById(R.id.totalSingle);

        // helps not to enable editing EditText when the activity starts
        view.findViewById(R.id.viewOneRate).setFocusableInTouchMode(true);
        view.findViewById(R.id.viewOneRate).requestFocus();

        loadRates();
        refreshLocations();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCalculateOneRate:
                calculate();
                break;
            case R.id.saveOneRate:
                saveRates();
                break;
            case R.id.saveOneRateRecord:
                saveRecord();
                break;
        }
    }

    private void calculate(){
        if (etInputPrevious.getText().toString().equals("") ||
                etInputCurrent.getText().toString().equals("")){
            Toast.makeText(getActivity(),
                    "Некоторые поля пусты", Toast.LENGTH_LONG).show();
        }
        else{
            double Rate = Double.valueOf(etRate.getText().toString());
            int IP = Integer.valueOf(etInputPrevious.getText().toString()),
                    IC = Integer.valueOf(etInputCurrent.getText().toString()),
                    D;
            long Si, Total;

            if (IC < IP){
                Toast.makeText(getActivity(),
                        "Введены некорректные данные", Toast.LENGTH_LONG).show();
            }
            else{
                D = Math.abs(IC - IP);
                Si = Math.round(Rate * D * 100);
                Total = Si;

                tvDelta.setText(String.valueOf(D));
                tvSum.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) Si / 100)
                                .concat(" p."));
                tvTotal.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) Total / 100)
                                .concat(" p."));
                isCalculated = true;
            }
        }
        hideKeyboard();
    }

    private void loadRates(){
        if (getActivity() != null)
            sPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        String savedRate = sPref.getString(SINGLE_RATE, "0.0");
        etRate.setText(savedRate);
    }

    private void saveRates(){
        if (getActivity() != null)
            sPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();

        editor.putString(SINGLE_RATE, etRate.getText().toString());
        editor.apply();

        Toast.makeText(getActivity(), "Тариф изменен", Toast.LENGTH_SHORT).show();
    }

    private void saveRecord(){
        if (!isCalculated) {
            if (getActivity() != null) Toast.makeText(getActivity(),
                    "Введите данные для расчета", Toast.LENGTH_SHORT).show();
        }
        else if (etLocation.getText().toString().equals("")){
            if (getActivity() != null) {
                Toast.makeText(getActivity(),
                        "Введите название места и \nповторите попытку снова",
                        Toast.LENGTH_LONG).show();
                etLocation.requestFocus();
            }
        }
        else{
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            String date = dateFormat.format(currentDate),
                    total = tvTotal.getText().toString();
            String[]
                    rates = {etRate.getText().toString()},
                    previous = {etInputPrevious.getText().toString()},
                    current = {etInputCurrent.getText().toString()},
                    sum = {tvSum.getText().toString()};


            MyRecord toSave = new MyRecord(
                    1, etLocation.getText().toString(), date,
                    rates[0], "-1", "-1",
                    previous[0], "-1", "-1",
                    current[0], "-1", "-1",
                    sum[0], "-1", "-1",
                    total);
            toSave.save();

            Toast.makeText(getActivity(), "Расчет сохранен", Toast.LENGTH_SHORT).show();
            refreshLocations();
        }
        hideKeyboard();
    }

    private String[] locationsToArray(List<String> list){
        String[] records = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            records[i] = list.get(i);
        }
        return records;
    }

    private void refreshLocations(){
        List<MyRecord> list = MyRecord.listAll(MyRecord.class);
        List<String> toStringArray = new ArrayList<>();

        for (int i = 0; i < list.size(); i++){
            if (!toStringArray.contains(list.get(i).location))
                toStringArray.add(list.get(i).location);
        }

        String[] locations = locationsToArray(toStringArray);
        if (getActivity() != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, locations);
            etLocation.setAdapter(adapter);
        }
    }

    private void hideKeyboard(){
        if (getActivity() != null) {
            View view = getActivity().getCurrentFocus();
            if (view != null && getContext() != null) {
                InputMethodManager imm = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
