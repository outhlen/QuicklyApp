package com.escort.carriage.android.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关流工具类
 */
public class CloseUtils {

    private CloseUtils() {
    }

    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
