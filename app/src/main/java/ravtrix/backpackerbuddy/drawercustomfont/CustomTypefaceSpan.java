package ravtrix.backpackerbuddy.drawercustomfont;

import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;

/**
 * Created by Ravinder on 12/23/16.
 */

public class CustomTypefaceSpan extends TypefaceSpan implements Parcelable {
    private final Typeface newType;
    private final int newSize;

    public CustomTypefaceSpan(String family, Typeface type, int size) {
        super(family);
        newType = type;
        newSize = size;
    }

    public final Parcelable.Creator<CustomTypefaceSpan> CREATOR = new Parcelable.Creator<CustomTypefaceSpan>(){

        @Override
        public CustomTypefaceSpan createFromParcel(Parcel parcel) {
            return null;
        }

        @Override
        public CustomTypefaceSpan[] newArray(int i) {
            return new CustomTypefaceSpan[i];
        }
    };

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, newType, newSize);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, newType, newSize);
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf, int size) {

        try {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTextSize(getPixelsFromDip(size));
            paint.setTypeface(tf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float getPixelsFromDip(float dip) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dip,
                Resources.getSystem().getDisplayMetrics()
        );
    }
}