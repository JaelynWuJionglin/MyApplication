package com.example.jaelyn.marqueetext;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jaelyn on 2015/11/27.
 */
public class MarqueeText extends TextView{
    public MarqueeText(Context context) {
        this(context, null);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean isFocused(){
        return true;
    }

}
