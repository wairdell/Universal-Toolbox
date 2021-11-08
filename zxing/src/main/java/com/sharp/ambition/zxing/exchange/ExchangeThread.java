package com.sharp.ambition.zxing.exchange;

import android.os.Handler;
import android.os.Looper;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;
import com.sharp.ambition.zxing.decoding.DecodeFormatManager;

import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 *
 */

public class ExchangeThread extends Thread {
    public static final String BARCODE_BITMAP = "barcode_bitmap";
    private final Hashtable<DecodeHintType, Object> hints;
    private Handler handler;
    private final CountDownLatch handlerInitLatch;

    private Handler mScanCodeHandler;
    public ExchangeThread(Handler handler,
                        Vector<BarcodeFormat> decodeFormats,
                        String characterSet,
                        ResultPointCallback resultPointCallback) {
        mScanCodeHandler = handler;
        handlerInitLatch = new CountDownLatch(1);

        hints = new Hashtable<>(3);

        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<>();
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        if (characterSet != null) {
            hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }

        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
    }

    public Handler getHandler() {
        try {
            handlerInitLatch.await();
        } catch (InterruptedException ie) {
            // continue?
        }
        return handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new ExchangeHandle(mScanCodeHandler, hints);
        handlerInitLatch.countDown();
        Looper.loop();
    }
}
