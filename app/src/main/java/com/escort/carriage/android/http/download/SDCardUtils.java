package com.escort.carriage.android.http.download;

import android.os.Environment;

import java.io.File;

public class SDCardUtils {
    private SDCardUtils() {
        /* cannot be instantiated *///
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            return Environment.getExternalStorageDirectory().getFreeSpace();
        }
        return 0;
    }

}
