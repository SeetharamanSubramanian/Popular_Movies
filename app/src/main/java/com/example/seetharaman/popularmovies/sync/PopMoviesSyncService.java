package com.example.seetharaman.popularmovies.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by Seetharaman on 12-02-2017.
 */

public class PopMoviesSyncService extends Service{

    private static final Object sSyncAdapterLock = new Object();
    private static PopMoviesSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("PopMoviesSyncService", "onCreate - SyncService");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new PopMoviesSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

}
