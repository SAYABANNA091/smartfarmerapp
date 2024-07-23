package com.soni.services.web;

import static com.soni.Preference.UserPreferenceKt.StoreUserData;
import static com.soni.Preference.UserPreferenceKt.storeLoginToken;
import static com.soni.Preference.UserPreferenceKt.storeUserID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.soni.activity.AuthenticationActivity;
import com.soni.activity.BaseActivity;
import com.soni.services.web.models.UserModel;
import com.soni.utils.Const;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class LoggingIntercepor implements Interceptor {

    public static String TAG = "LoggingInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestLog = String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers());

        if(request.method().compareToIgnoreCase("post")==0){
            requestLog ="\n"+requestLog+"\n"+bodyToString(request);
        }
        printLongMessage(requestLog);

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        String responseLog = String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers());

        String bodyString = response.body().string();

        printLongMessage("response"+"\n"+responseLog+"\n"+bodyString);

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }


    void printLongMessage(String veryLongString){
        int maxLogSize = 1000;
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
        }
    }

}
class UnauthorizedInterceptor implements Interceptor {
    private final Context  context;
    public UnauthorizedInterceptor(Context  context) {
        this.context =context;
    }
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 401) {
            Intent mIntent= new Intent(context, AuthenticationActivity.class);
            mIntent.putExtra(Const.IntentKey.Unauthorized,true);
            UserModel user=new UserModel();
            storeUserID("");
            storeLoginToken("");
            StoreUserData(user);
            ((Activity) context).finishAffinity();
            context.startActivity(mIntent);


        }
        return response;
    }
}