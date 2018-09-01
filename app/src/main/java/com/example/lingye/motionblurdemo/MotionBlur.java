package com.example.lingye.motionblurdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RSInvalidStateException;
import android.support.v8.renderscript.RenderScript;
import android.util.Log;

/**
 * a filter which produces the motion blur
 */
public class MotionBlur {
    private static final String TAG = "MotionBlur";
    private static RenderScript sRs;
    private  static ScriptC_motionblur scriptC_motionblur;

    /**
     * notice that blur radius = dx * delta
     * @param src the bitmap to make a motion blur filter.the Bitmap type should be {@link Bitmap.Config#ARGB_8888}
     * @param dx the blur distance
     * @param delta sampling interval to compute the blur filter.
     * @return the bitmap after make a motion blur filter in the src.
     */
    public static Bitmap bitmapMotionBlur(Context context, Bitmap src, int dx, int delta) {
        int h = src.getHeight();
        int w = src.getWidth();

        Allocation temIn;
        Allocation temOut;

        if(null == context || null == src) {
            return null;
        }

        if(null == sRs) {
            sRs = RenderScript.create(context);
        }

        Bitmap dst = Bitmap.createBitmap(w, h ,Bitmap.Config.ARGB_8888);
        temIn = Allocation.createFromBitmap(sRs, src,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        temOut = Allocation.createTyped(sRs, temIn.getType());
        if (null == scriptC_motionblur) {
            scriptC_motionblur = new ScriptC_motionblur(sRs);
        }

        scriptC_motionblur.invoke_init_var(dx, w, delta);
        scriptC_motionblur.set_allocation_out(temOut);
        scriptC_motionblur.set_allocation_in(temIn);
        scriptC_motionblur.forEach_motionblur(temIn);
        temOut.copyTo(dst);

        try {
            temIn.destroy();
            temOut.destroy();
        }catch (RSInvalidStateException e) {
            Log.w(TAG, "destroy temp in or out failed");
        }
        return dst;

    }
}
