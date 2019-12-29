package com.example.andrey.electricitycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import static android.content.Context.MODE_PRIVATE;

public class TwoRateCounter extends Fragment implements View.OnClickListener {

    private static final String TAG = "TwoRateCounter";

    SharedPreferences sPref;
    AutoCompleteTextView etLocation;
    EditText etRateDay, etRateNight;
    EditText etInputDayPrevious, etInputNightPrevious,
            etInputDayCurrent, etInputNightCurrent;
    TextView tvDeltaDay, tvDeltaNight, tvSumDay, tvSumNight, tvTotal;
    Boolean isCalculated;

    String RATE_DAY = "22", RATE_NIGHT = "11";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_two_rate_counter,
                container, false);

        if (getActivity() != null) getActivity().setTitle("Двухтарифный счетчик");
        isCalculated = false;

        Button saveRates = view.findViewById(R.id.saveTwoRates);
        Button calculate = view.findViewById(R.id.buttonCalculateTwoRates);
        Button saveRecord = view.findViewById(R.id.saveTwoRatesRecord);

        calculate.setOnClickListener(this);
        saveRates.setOnClickListener(this);
        saveRecord.setOnClickListener(this);

        etRateDay = view.findViewById(R.id.rateDay);
        etRateNight = view.findViewById(R.id.rateNight);
        etInputDayPrevious = view.findViewById(R.id.inputDayPrevious);
        etInputDayCurrent = view.findViewById(R.id.inputDayCurrent);
        etInputNightPrevious = view.findViewById(R.id.inputNightPrevious);
        etInputNightCurrent = view.findViewById(R.id.inputNightCurrent);
        etLocation = view.findViewById(R.id.inputLocationTwoRates);

        tvDeltaDay = view.findViewById(R.id.deltaDay);
        tvDeltaNight = view.findViewById(R.id.deltaNight);
        tvSumDay = view.findViewById(R.id.sumDay);
        tvSumNight = view.findViewById(R.id.sumNight);
        tvTotal = view.findViewById(R.id.total);

        // helps not to enable editing EditText when the activity starts
        view.findViewById(R.id.viewTwoRates).setFocusableInTouchMode(true);
        view.findViewById(R.id.viewTwoRates).requestFocus();

        loadRates();
        refreshLocations();

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonCalculateTwoRates:
                calculate();
                break;
            case R.id.saveTwoRates:
                saveRates();
                break;
            case R.id.saveTwoRatesRecord:
                saveRecord();
                break;
        }
    }

    private void calculate() {
        if (etInputDayPrevious.getText().toString().equals("") ||
                etInputDayCurrent.getText().toString().equals("") ||
                etInputNightPrevious.getText().toString().equals("") ||
                etInputNightCurrent.getText().toString().equals("")) {
            Toast.makeText(getActivity(),
                    "Некоторые поля пусты", Toast.LENGTH_LONG).show();
        }
        else {
            double RateDay = Double.valueOf(etRateDay.getText().toString()),
                    RateNight = Double.valueOf(etRateNight.getText().toString());
            int IDP = Integer.valueOf(etInputDayPrevious.getText().toString()),
                    IDC = Integer.valueOf(etInputDayCurrent.getText().toString()),
                    INP = Integer.valueOf(etInputNightPrevious.getText().toString()),
                    INC = Integer.valueOf(etInputNightCurrent.getText().toString()),
                    DD, DN; // deltaDay, deltaNight
            long SDi, SNi, Total;

            if (IDC < IDP || INC < INP) {
                Toast.makeText(getActivity(),
                        "Введены некорректные данные", Toast.LENGTH_LONG).show();
            } else {
                DD = Math.abs(IDC - IDP);
                DN = Math.abs(INC - INP);
                SDi = Math.round(RateDay * DD * 100);
                SNi = Math.round(RateNight * DN * 100);
                Total = SDi + SNi;

                tvDeltaDay.setText(String.valueOf(DD));
                tvDeltaNight.setText(String.valueOf(DN));

                tvSumDay.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) SDi / 100)
                                .concat(" p."));
                tvSumNight.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) SNi / 100)
                                .concat(" p."));

                tvTotal.setText(
                        String.format(Locale.getDefault(), "%.2f", (double) Total / 100)
                                .concat(" p."));
                isCalculated = true;
            }
        }
        hideKeyboard();
    }

    private void loadRates() {
        if (getActivity() != null)
            sPref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);

        String savedRateDay = sPref.getString(RATE_DAY, "0.0");
        String savedRateNight = sPref.getString(RATE_NIGHT, "0.0");

        etRateDay.setText(savedRateDay);
        etRateNight.setText(savedRateNight);
    }

    private void saveRates() {
        if (getActivity() != null)
            sPref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();

        editor.putString(RATE_DAY, etRateDay.getText().toString());
        editor.putString(RATE_NIGHT, etRateNight.getText().toString());

        editor.apply();

        Toast.makeText(getActivity(),
                "Тарифы изменены", Toast.LENGTH_SHORT).show();
    }

    private void saveRecord() {
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

            String date = dateFormat.format(currentDate),
                    total = tvTotal.getText().toString();
            String[]
                    rates = {etRateDay.getText().toString(), etRateNight.getText().toString()},
                    previous = {etInputDayPrevious.getText().toString(),
                            etInputNightPrevious.getText().toString()},
                    current = {etInputDayCurrent.getText().toString(),
                            etInputNightCurrent.getText().toString()},
                    sum = {tvSumDay.getText().toString(), tvSumNight.getText().toString()};


            MyRecord toSave = new MyRecord(
                    2, etLocation.getText().toString(), date,
                    rates[0], rates[1], "-1",
                    previous[0], previous[1], "-1",
                    current[0], current[1], "-1",
                    sum[0], sum[1], "-1",
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
