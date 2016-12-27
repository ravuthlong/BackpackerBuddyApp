package ravtrix.backpackerbuddy.drawercustomfont;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Ravinder on 12/23/16.
 */

public class FontTypeface {
    private Context context;

    public FontTypeface(Context context){
        this.context = context;
    }

    public Typeface getTypefaceAndroid(){
        String strFont = "Text.ttf";
        return Typeface.createFromAsset(context.getAssets(), strFont);
    }
}