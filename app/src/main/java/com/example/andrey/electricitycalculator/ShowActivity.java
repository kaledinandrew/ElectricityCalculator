package com.example.andrey.electricitycalculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShowActivity extends AppCompatActivity {

    TextView tvLocation, tvDate, tvCounterRates, tvRatesTitle, tvRates,
            tvT1Title, tvT1, tvT2Title, tvT2, tvT3Title, tvT3, tvTotal;
    int position, type;
    LinearLayout lineT2;

    MyRecord[] records;
    MyRecord record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        getIncomingIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_nav_drawer);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(("Расчет № " + (position + 1)));
        }

        records = recordsToArray();
        record = records[position];
        this.type = record.type;

        tvLocation = findViewById(R.id.showLocation);
        tvDate = findViewById(R.id.showDate);
        tvCounterRates = findViewById(R.id.showCounterRates);
        tvRatesTitle = findViewById(R.id.showRatesTitle);
        tvRates = findViewById(R.id.showRates);
        tvT1Title = findViewById(R.id.showT1Title);
        tvT1 = findViewById(R.id.showT1);
        tvT2Title = findViewById(R.id.showT2Title);
        tvT2 = findViewById(R.id.showT2);
        tvT3Title = findViewById(R.id.showT3Title);
        tvT3 = findViewById(R.id.showT3);
        tvTotal = findViewById(R.id.showTotal);

        fillFields();
    }

    private void getIncomingIntent(){
        this.position = Integer.parseInt(getIntent().getStringExtra("MyRecordPosition"));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private MyRecord[] recordsToArray(){
        List<MyRecord> list = MyRecord.listAll(MyRecord.class);
        MyRecord[] items = new MyRecord[list.size()];
        for (int i = 0; i < list.size(); i++){
            items[i] = list.get(list.size() - i - 1);
        }
        return items;
    }

    private void fillFields(){
        tvLocation.setText(record.location);
        tvDate.setText(record.date);
        tvCounterRates.setText((String.valueOf(type) + "-тарифный счетчик"));

        if (type == 1) fillTypeOne();
        else if (type == 2) fillTypeTwo();
        else fillTypeThree();

        tvTotal.setText(record.total);
    }

    private void fillTypeOne(){
        tvRatesTitle.setText("Тариф: ");
        tvRates.setText(record.rate1);

        tvT1Title.setText(R.string.T1);
        tvT1.setText((record.previous1 + " -> " + record.current1));
        tvT2Title.setText(R.string.T2);
        tvT2.setText("-----");
        tvT3Title.setText(R.string.T3);
        tvT3.setText("-----");

    }

    private void fillTypeTwo(){
        tvRatesTitle.setText("Тарифы: ");
        tvRates.setText((record.rate1 + " " + record.rate2));

        tvT1Title.setText(R.string.T1);
        tvT1.setText((record.previous1 + " -> " + record.current1));
        tvT2Title.setText(R.string.T2);
        tvT2.setText((record.previous2 + " -> " + record.current2));
        tvT3Title.setText(R.string.T3);
        tvT3.setText("-----");
    }

    private void fillTypeThree(){
        tvRatesTitle.setText("Тарифы: ");
        tvRates.setText((record.rate1 + " " + record.rate2 + " " + record.rate3));

        tvT1Title.setText(R.string.T1);
        tvT1.setText((record.previous1 + " -> " + record.current1));
        tvT2Title.setText(R.string.T2);
        tvT2.setText((record.previous2 + " -> " + record.current2));
        tvT3Title.setText(R.string.T3);
        tvT3.setText((record.previous3 + " -> " + record.current3));
    }

    public void deleteRecord(View view){
        record.delete();
        Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show();
        finish();
    }
}
