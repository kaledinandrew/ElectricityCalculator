package com.example.andrey.electricitycalculator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    MyRecord[] myRecords;

    public RecordAdapter(Context context, MyRecord[] myRecords){
        this.context = context;
        this.myRecords = myRecords;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View row = layoutInflater.inflate(R.layout.recycler_row, viewGroup, false);
        return new Item(row);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final MyRecord myRecord = myRecords[position];
        final int toPut = position;

        if (myRecord.type == 1)
            ((Item)viewHolder).ivIcon.setImageResource(R.drawable.ic_looks_one_black_24dp);
        if (myRecord.type == 2)
            ((Item)viewHolder).ivIcon.setImageResource(R.drawable.ic_looks_two_black_24dp);
        if (myRecord.type == 3)
            ((Item)viewHolder).ivIcon.setImageResource(R.drawable.ic_looks_3_black_24dp);

        ((Item)viewHolder).tvLocation.setText(myRecord.location);
        ((Item)viewHolder).tvDate.setText(myRecord.date);
        ((Item)viewHolder).tvSum.setText(myRecord.total);

        ((Item) viewHolder).linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowActivity.class);
                intent.putExtra("MyRecordPosition", String.valueOf(toPut));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myRecords.length;
    }

    public class Item extends RecyclerView.ViewHolder{
        TextView tvLocation, tvDate, tvSum;
        ImageView ivIcon;
        LinearLayout linearLayout;

        public Item(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.recycler_item_icon);
            tvLocation = itemView.findViewById(R.id.recycler_item_location);
            tvDate = itemView.findViewById(R.id.recycler_item_date);
            tvSum = itemView.findViewById(R.id.recycler_item_sum);
            linearLayout = itemView.findViewById(R.id.recyclerViewLinearLayout);
        }
    }
}


