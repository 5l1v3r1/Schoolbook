package com.marco.marplex.schoolbook.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by marco on 7/24/16.
 */
public class StaticHeightListView extends ListView {
    public StaticHeightListView  (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StaticHeightListView  (Context context) {
        super(context);
    }

    public StaticHeightListView  (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
