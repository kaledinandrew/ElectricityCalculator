package com.example.andrey.electricitycalculator;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class History extends Fragment {

    RecyclerView recyclerView;
    MyRecord[] myRecords;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        if (getActivity() != null) getActivity().setTitle("История расчетов");

        recyclerView = view.findViewById(R.id.sugar_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new VerticalSpace(10));
        refreshRecycler();

        return view;
    }

    public MyRecord[] recordsToArray(){
        List<MyRecord> list = MyRecord.listAll(MyRecord.class);
        MyRecord[] items = new MyRecord[list.size()];
        for (int i = 0; i < list.size(); i++){
            items[i] = list.get(list.size() - i - 1);
        }
        return items;
    }

    public void refreshRecycler(){
        myRecords = recordsToArray();
        recyclerView.setAdapter(new RecordAdapter(getContext(), myRecords));
        if (myRecords.length == 0)
            Toast.makeText(getActivity(), "История расчетов пуста", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecycler();
    }
}
