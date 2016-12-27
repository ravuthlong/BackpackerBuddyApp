package ravtrix.backpackerbuddy.recyclerviewfeed.travelpostsrecyclerview.decorator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ravinder on 9/9/16.
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    private Drawable divider;

    public DividerDecoration(Context context, int dividerID) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            divider = context.getResources().getDrawable(dividerID, context.getTheme());
        } else {
            divider = ContextCompat.getDrawable(context, dividerID);
        }
    }

    // Called for each child of recyclerview. Call offset to ensure we're not drawing on the childs.
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent.getChildAdapterPosition(view) == 0) {
            return;
        }
        outRect.top = divider.getIntrinsicHeight();
    }

    // Run once to calculate the bounds and draw at the correct position below each child
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + divider.getIntrinsicHeight();

            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            divider.draw(c);
        }
    }
}
