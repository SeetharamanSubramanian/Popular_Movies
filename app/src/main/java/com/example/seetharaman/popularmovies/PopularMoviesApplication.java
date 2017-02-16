package com.example.seetharaman.popularmovies;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

/**
 * Created by Seetharaman on 04-02-2017.
 */

public class PopularMoviesApplication extends Application {

    public OkHttpClient httpClient;

    @Override
    public void onCreate() {
        super.onCreate();
//        // Initialize Stetho
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(this)
//                        .enableDumpapp(new SampleDumperPluginsProvider(this))
//                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
//                        .build());

        if (BuildConfig.DEBUG) {

            // Initialize Stetho
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

            //Initialize Stetho Interceptor into OkHttp client
            httpClient = new OkHttpClient.Builder().addNetworkInterceptor(new StethoInterceptor()).build();
        } else {
            httpClient = new OkHttpClient();
        }

        //Initialize Picasso
        Picasso picasso = new Picasso.Builder(this).downloader(new OkHttp3Downloader(httpClient)).build();
        Picasso.setSingletonInstance(picasso);

    }


//    // Create class for Stetho
//    private static class SampleDumperPluginsProvider implements DumperPluginsProvider {
//        private final Context mContext;
//
//        public SampleDumperPluginsProvider(Context context){mContext = context;}
//
//        @Override
//        public Iterable<DumperPlugin> get() {
//            ArrayList<DumperPlugin> plugins = new ArrayList<>();
//            for (DumperPlugin defaultPlugin : Stetho.defaultDumperPluginsProvider(mContext).get()) {
//                plugins.add(defaultPlugin);
//            }
//            //plugins.add(new SyncAdapterFragment());
//            return plugins;
//        }
//    }
}
