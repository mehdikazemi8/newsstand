package com.nebeek.newsstand.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.nebeek.newsstand.R;

public class GlobalToast {
    /**
     * This methods is a global method used to make toasts, all toasts that are shown
     * in the program are generated using this method
     *
     * @param context  This is the context that toast is shown in
     * @param message  This is the text message that needs to be shown
     * @param duration The duration is either Toast.LENGTH_LONG or Toast.LENGTH_SHORT
     */
    public static void makeToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);

        TextView messageTextView = (TextView) toast.getView().findViewById(android.R.id.message);
//        messageTextView.setTextSize(26);
        // todo change typeface
//        messageTextView.setTypeface(FontHelper.getDefaultTypeface(context));

        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize}
        );
        int height = (int) styledAttributes.getDimension(0, 0) + 5;
        toast.setGravity(Gravity.BOTTOM, 0, height * 2);
        toast.show();
    }

    public static void showError(Context context) {
        makeToast(context, context.getString(R.string.try_again_later), Toast.LENGTH_SHORT);
    }

    public static void showNetworkFailureError(Context context) {
        makeToast(context, context.getString(R.string.no_internet_connection), Toast.LENGTH_SHORT);
    }

}
