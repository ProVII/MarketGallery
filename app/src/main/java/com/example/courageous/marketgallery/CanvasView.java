package com.example.courageous.marketgallery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class CanvasView extends View {
    // Using an external typeface font for the app title.
    Typeface pacifico = Typeface.createFromAsset(getContext().getAssets(), "fonts/pacifico.ttf");

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attribs) {
        super(context, attribs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTypeface(pacifico);
        // Sets the color to the standardized color across the app.
        paint.setColor(ContextCompat.getColor(getContext(), R.color.yellow));
        paint.setTextSize(60);
        // Sets the text to be drawn and the location of it.
        canvas.drawText("Snackies Market Gallery", 170, 62, paint);
    }
}
