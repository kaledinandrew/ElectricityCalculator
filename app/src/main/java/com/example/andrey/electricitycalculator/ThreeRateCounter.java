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

public class ThreeRateCounter extends Fragment implements View.OnClickListener{

    private static final String TAG = "ThreeRateCounter";

    SharedPreferences sPref;
    AutoCompleteTextView etLocation;
    EditText etRate1, etRate2, etRate3;
    EditText etInput1Previous, etInput2Previous, etInput3Previous,
            etInput1Current, etInput2Current, etInput3Current;
    TextView tvDelta1, tvDelta2, tvDelta3, tvSum1, tvSum2, tvSum3, tvTotal;
    View view;
    Boolean isCalculated;

    String THREE_RATES = "0.0 0.0 0.0";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_three_rate_counter,
                container, false);

        if (getActivity() != null) getActivity().setTitle("Трехтарифный счетчик");
        isCalculated = false;

        Button saveRates = view.findViewById(R.id.saveThreeRates);
        Button calculate = view.findViewById(R.id.buttonCalculateThreeRates);
        Button saveRecord = view.findViewById(R.id.saveThreeRatesRecord);

        calculate.setOnClickListener(this);
        saveRates.setOnClickListener(this);
        saveRecord.setOnClickListener(this);

        etRate1 = view.findViewById(R.id.rate1);
        etRate2 = view.findViewById(R.id.rate2);
        etRate3 = view.findViewById(R.id.rate3);
        etInput1Previous = view.findViewById(R.id.inputFirstPrevious);
        etInput1Current = view.findViewById(R.id.inputFirstCurrent);
        etInput2Previous = view.findViewById(R.id.inputSecondPrevious);
        etInput2Current = view.findViewById(R.id.inputSecondCurrent);
        etInput3Previous = view.findViewById(R.id.inputThirdPrevious);
        etInput3Current = view.findViewById(R.id.inputThirdCurrent);
        etLocation = view.findViewById(R.id.inputLocationThreeRates);

        tvDelta1 = view.findViewById(R.id.deltaFirst);
        tvDelta2 = view.findViewById(R.id.deltaSecond);
        tvDelta3 = view.findViewById(R.id.deltaThird);
        tvSum1 = view.findViewById(R.id.sumFirst);
        tvSum2 = view.findViewById(R.id.sumSecond);
        tvSum3 = view.findViewById(R.id.sumThird);
        tvTotal = view.findViewById(R.id.totalThreeRates);

        // helps not to enable editing EditText when the activity starts
        view.findViewById(R.id.viewThreeRates).setFocusableInTouchMode(true);
        view.findViewById(R.id.viewThreeRates).requestFocus();

        loadRates();
        refreshLocations();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonCalculateThreeRates:
                calculate();
                break;
            case R.id.saveThreeRates:
                saveRates();
                break;
            case R.id.saveThreeRatesRecord:
                saveRecord();
                break;
        }
    }

    private void calculate(){
        if (etInput1Previous.getText().toString().equals("") ||
                etInput1Current.getText().toString().equals("") ||
                etInput2Previous.getText().toString().equals("") ||
                etInput2Current.getText().toString().equals("") ||
                etInput3Previous.getText().toString().equals("") ||
                etInput3Current.getText().toString().equals("")){
            Toast.makeText(getActivity(),
                    "Некоторые поля пусты", Toast.LENGTH_LONG).show();
        }
        else{
            double Rate1 = Double.valueOf(etRate1.getText().toString()),
                    Rate2 = Double.valueOf(etRate2.getText().toString()),
                    Rate3 = Double.valueOf(etRate3.getText().toString());
            int I1P = Integer.valueOf(etInput1Previous.getText().toString()),
                    I1C = Integer.valueOf(etInput1Current.getText().toString()),
                    I2P = Integer.valueOf(etInput2Previous.getText().toString()),
                    I2C = Integer.valueOf(etInput2Current.getText().toString()),
                    I3P = Integer.valueOf(etInput3Previous.getText().toString()),
                    I3C = Integer.valueOf(etInput3Current.getText().toString()),
                    D1, D2, D3;
            long S1i, S2i, S3i, Total;

            if (I1C < I1P || I2C < I2P || I3C < I3P){
                Toast.makeText(getActivity(),
                        "Введены некорректные данные", Toast.LENGTH_LONG).show();
            }
            else{
                D1 = Math.abs(I1C - I1P);
                D2 = Math.abs(I2C - I2P);
                D3 = Math.abs(I3C - I3P);
                S1i = Math.round(Rate1 * D1 * 100);
                S2i = Math.round(Rate2 * D2 * 100);
                S3i = Math.round(Rate3 * D3 * 100);
                Total = S1i + S2i + S3i;

                tvDelta1.setText(String.valueOf(D1));
                tvDelta2.setText(String.valueOf(D2));
                tvDelta3.setText(String.valueOf(D3));

                tvSum1.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) S1i / 100)
                                .concat(" p."));
                tvSum2.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) S2i / 100)
                                .concat(" p."));
                tvSum3.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) S3i / 100)
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
            sPref = getActivity().getSharedPreferences("ThreePref", Context.MODE_PRIVATE);

        String savedRates = sPref.getString(THREE_RATES, "0.0 0.0 0.0");
        String[] rates = convertToArray(savedRates);

        String savedRate1 = rates[0];
        String savedRate2 = rates[1];
        String savedRate3 = rates[2];

        etRate1.setText(savedRate1);
        etRate2.setText(savedRate2);
        etRate3.setText(savedRate3);
    }

    private String[] convertToArray(String s){
        return s.split(" ");
    }

    private void saveRates(){
        if (getActivity() != null)
            sPref = getActivity().getSharedPreferences("ThreePref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();

        editor.putString(THREE_RATES, etRate1.getText().toString() + " " +
                etRate2.getText().toString() + " " +
                etRate3.getText().toString());

        editor.apply();

        Toast.makeText(getActivity(),
                "Тарифы изменены", Toast.LENGTH_SHORT).show();
    }

    private void saveRecord(){
        if (!isCalculated) {
            if (getActivity() != null) Toast.makeText(getActivity(),
                    "Введите данные для расчета", Toast.LENGTH_SHORT).show();
        } else if (etLocation.getText().toString().equals("")) {
            if (getActivity() != null) {
                Toast.makeText(getActivity(),
                        "Введите название места и \nповторите попытку снова",
                        Toast.LENGTH_LONG).show();
                etLocation.requestFocus();
            }
        } else {
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

            String location = etLocation.getText().toString(),
                    date = dateFormat.format(currentDate),
                    total = tvTotal.getText().toString();
            String[]
                    rates = {etRate1.getText().toString(),
                            etRate2.getText().toString(),
                            etRate3.getText().toString()},
                    previous = {etInput1Previous.getText().toString(),
                            etInput2Previous.getText().toString(),
                            etInput3Previous.getText().toString()},
                    current = {etInput1Current.getText().toString(),
                            etInput2Current.getText().toString(),
                            etInput3Current.getText().toString()},
                    sum = {tvSum1.getText().toString(),
                            tvSum2.getText().toString(),
                            tvSum3.getText().toString()};


            MyRecord toSave = new MyRecord(3, location, date,
                    rates[0], rates[1], rates[2],
                    previous[0], previous[1], previous[2],
                    current[0], current[1], current[2],
                    sum[0], sum[1], sum[2],
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
        // hides keyboard when button is pressed
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
