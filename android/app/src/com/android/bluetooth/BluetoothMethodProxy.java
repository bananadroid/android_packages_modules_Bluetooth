/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.bluetooth;

import android.annotation.RequiresPermission;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.internal.annotations.VisibleForTesting;
import com.android.obex.HeaderSet;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Proxy class for method calls to help with unit testing
 */
public class BluetoothMethodProxy {
    private static final String TAG = BluetoothMethodProxy.class.getSimpleName();
    private static final Object INSTANCE_LOCK = new Object();
    private static BluetoothMethodProxy sInstance;

    private BluetoothMethodProxy() {
    }

    /**
     * Get the singleton instance of proxy
     *
     * @return the singleton instance, guaranteed not null
     */
    public static BluetoothMethodProxy getInstance() {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new BluetoothMethodProxy();
            }
        }
        return sInstance;
    }

    /**
     * Allow unit tests to substitute BluetoothPbapMethodCallProxy with a test instance
     *
     * @param proxy a test instance of the BluetoothPbapMethodCallProxy
     */
    @VisibleForTesting
    public static void setInstanceForTesting(BluetoothMethodProxy proxy) {
        Utils.enforceInstrumentationTestMode();
        synchronized (INSTANCE_LOCK) {
            Log.d(TAG, "setInstanceForTesting(), set to " + proxy);
            sInstance = proxy;
        }
    }

    /**
     * Proxies {@link ContentResolver#query(Uri, String[], String, String[], String)}.
     */
    public Cursor contentResolverQuery(ContentResolver contentResolver, final Uri contentUri,
            final String[] projection, final String selection, final String[] selectionArgs,
            final String sortOrder) {
        return contentResolver.query(contentUri, projection, selection, selectionArgs, sortOrder);
    }

    /**
     * Proxies {@link ContentResolver#update(Uri, ContentValues, String, String[])}.
     */
    public int contentResolverUpdate(ContentResolver contentResolver, final Uri contentUri,
            final ContentValues contentValues, String where, String[] selectionArgs) {
        return contentResolver.update(contentUri, contentValues, where, selectionArgs);
    }

    /**
     * Proxies {@link ContentResolver#delete(Uri, String, String[])}.
     */
    public int contentResolverDelete(ContentResolver contentResolver, final Uri url,
            final String where,
            final String[] selectionArgs) {
        return contentResolver.delete(url, where, selectionArgs);
    }

    /**
     * Proxies {@link BluetoothAdapter#isEnabled()}.
     */
    public boolean bluetoothAdapterIsEnabled(BluetoothAdapter adapter) {
        return adapter.isEnabled();
    }

    /**
     * Proxies {@link ContentResolver#openFileDescriptor(Uri, String)}.
     */
    public ParcelFileDescriptor contentResolverOpenFileDescriptor(ContentResolver contentResolver,
            final Uri uri, final String mode) throws FileNotFoundException {
        return contentResolver.openFileDescriptor(uri, mode);
    }

    /**
     * Proxies {@link Context#sendBroadcast(Intent)}.
     */
    public void contextSendBroadcast(Context context, @RequiresPermission Intent intent) {
        context.sendBroadcast(intent);
    }

    /**
     * Proxies {@link HeaderSet#getHeader}.
     */
    public Object getHeader(HeaderSet headerSet, int headerId) throws IOException {
        return headerSet.getHeader(headerId);
    }

    /**
     * Proxies {@link Context#getSystemService(Class)}.
     */
    public <T> T getSystemService(Context context, Class<T> serviceClass) {
        return context.getSystemService(serviceClass);
    }
}
