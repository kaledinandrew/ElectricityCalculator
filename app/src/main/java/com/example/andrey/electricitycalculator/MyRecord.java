package com.example.andrey.electricitycalculator;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class MyRecord extends SugarRecord {
     int type;
     String location, date;
     String rate1, rate2, rate3,
            previous1, previous2, previous3,
            current1, current2, current3;
     private String sum1, sum2, sum3;
     String total;

    public MyRecord(){

    }

    MyRecord(int type, String location, String date,
             String rate1, String rate2, String rate3,
             String previous1, String previous2, String previous3,
             String current1, String current2, String current3,
             String sum1, String sum2, String sum3,
             String total) {
        this.type = type;
        this.location = location;
        this.date = date;
        this.rate1 = rate1;
        this.rate2 = rate2;
        this.rate3 = rate3;
        this.previous1 = previous1;
        this.previous2 = previous2;
        this.previous3 = previous3;
        this.current1 = current1;
        this.current2 = current2;
        this.current3 = current3;
        this.sum1 = sum1;
        this.sum2 = sum2;
        this.sum3 = sum3;
        this.total = total;
    }
}
