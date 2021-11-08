package com.sharp.ambition.zxing;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.sharp.ambition.zxing.view.ViewfinderView;

/**
 * @author Wairdell
 * @date 2018/6/25
 */
public interface ProxyView {

    Handler getHandler();

    ViewfinderView getViewfinderView();

    void handleDecode(Result result, Bitmap barcode);

    void drawViewfinder();
}
