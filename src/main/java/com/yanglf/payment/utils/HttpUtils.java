package com.yanglf.payment.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

/**
 * @author : yanglf
 * @version : 1.0
 * @created IntelliJ IDEA.
 * @date : 2019/9/4 18:38
 * @desc :
 */
@Slf4j
public class HttpUtils {


    public static String get(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.info("e:{}", e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.info(response.body().string());

            }
        });
        return null;

    }


}
