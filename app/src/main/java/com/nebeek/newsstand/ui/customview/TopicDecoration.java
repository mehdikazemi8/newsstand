package com.nebeek.newsstand.ui.customview;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nebeek.newsstand.R;

public class TopicDecoration extends DividerItemDecoration {

    int padding;

    public TopicDecoration(Context context, int orientation) {
        super(context, orientation);

        padding = context.getResources().getDimensionPixelOffset(R.dimen.topic_padding);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.bottom = padding / 2;
//        outRect.top = padding / 2;
//        outRect.left = padding / 2;
//        outRect.right = padding / 2;
    }
}
