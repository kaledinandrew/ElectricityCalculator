package com.example.andrey.electricitycalculator;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class VerticalSpace extends RecyclerView.ItemDecoration{

    int space;

    public VerticalSpace(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildLayoutPosition(view) == 0){
            outRect.top = space;
        }
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
    }
}
