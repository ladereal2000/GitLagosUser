package com.practice.gitlagosuser.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by David on 8/11/2017.
 */

public class GitUsersTaskLoader extends AsyncTaskLoader<String> {

    /*private static String MAIN_URL =
            "https://api.github.com/search/users?q=location:Lagos";*/

    private static String MAIN_URL =
            "https://api.github.com/search/users?q=location:Lagos&token=24207b7c87e39d656574e833b03f0747e4f3f837";

    public static final String TAG = "TAG";
    /**
     * Response
     */
    private static Response response;

    public GitUsersTaskLoader(Context context) {
        super(context);
    }

    public GitUsersTaskLoader(Context context, String profile_url) {
        super(context);
        this.MAIN_URL = profile_url;
    }

    @Override
    public String loadInBackground() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(MAIN_URL)
                    .build();
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getLocalizedMessage());
            return  null;
        }
    }
}
