package org.hackillinois.android.schedule;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerTitleStrip;
import android.util.AttributeSet;

/**
 * @author vishal
 *         TODO change element in XML from .PagerTabStrip to .SchedulePagerTabStrip
 *         Override to show the rocket animations!!!
 */
public class SchedulePagerTabStrip extends PagerTitleStrip {


    public SchedulePagerTabStrip(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
