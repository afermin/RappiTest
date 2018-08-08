package com.example.rappitest.ui.common;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Alexander Fermin (alexfer06@gmail.com) on 08/08/2018.
 */
public class PosterAdaptableImageView extends android.support.v7.widget.AppCompatImageView {

    public PosterAdaptableImageView(Context context) {
        super(context);
    }

    public PosterAdaptableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PosterAdaptableImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, (int) (width*1.5));
    }

}