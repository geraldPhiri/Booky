package com.afripayzm.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class FrameLayoutForRatio extends FrameLayout
{
    private float a = 1.33F;

    public FrameLayoutForRatio(Context paramContext)
    {
        super(paramContext);
    }

    public FrameLayoutForRatio(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
    }


    @SuppressLint("WrongConstant")
    protected void onMeasure(int paramInt1, int paramInt2)
    {
        if (MeasureSpec.getMode(paramInt1) == 1073741824) {
            paramInt2 = MeasureSpec.makeMeasureSpec((int)(MeasureSpec.getSize(paramInt1) * this.a), 1073741824);
        }
        super.onMeasure(paramInt1, paramInt2);
    }

}
