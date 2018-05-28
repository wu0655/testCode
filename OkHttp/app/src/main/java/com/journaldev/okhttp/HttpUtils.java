/*
 * Copyright (c) 2018 Opzoon, Inc.
 * Author(s): Ma Ning (maning1@opzoon.com)
 */

package com.journaldev.okhttp;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by matthew on 2018/4/9.
 */

public class HttpUtils {
    public static final String TAG = "OKHTTP";

    public static final String HTTPS_HEADER = "https://";
    public static final String HTTP_HEADER = "http://";
    public static final String URL_GET_VERSION = "/devapi/v1/server/version/ops";
    private static final String PATH_CER = "/data/data/com.opzoon.face.standard/cer/https.cer";
    private static int HTTP_CONNECTION_TIMEOUT = 20; // connection timeout 20s
    private static int HTTP_DOWNLOAD_TIMEOUT = 5; // connection timeout 20s
    private static int HTTP_READ_TIMEOUT = 10; // read timeout 20s
    private static int HTTP_WRITE_TIMEOUT = HTTP_READ_TIMEOUT; // write timeout 20s

    public static final Map<String, String> DEFAULT_REQUEST_HEADER = new HashMap<>();
    public static final int ACK_RETRY = 3;

    public static final String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType MEDIA_TYPE_ZIP = MediaType.parse("application/zip");

    public static final String CONTENT_TYPE_FILE = "multipart/form-data";
    public static final MediaType MEDIA_TYPE_FILE = MediaType.parse(CONTENT_TYPE_FILE);


    public static final String URL = "http://172.16.3.4/api/datacollection/logger/v1/app";
    public static final String SECRET_KEY = "K/1OTBT9j1rqyVjVPQdf"; //TODO define from SECRET_KEY

    public static final String FILE_UPLOAD_URL = "http://172.16.3.3/api/ocfs/file/upload";


    public static void postRequestFile(File file) throws IOException {
        OkHttpClient client = HttpUtils.createOkHttpClient(HttpLoggingInterceptor.Level.HEADERS);


        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        final MediaType MEDIA_TYPE = MEDIA_TYPE_ZIP;
        builder.addFormDataPart("file",file.getName(),RequestBody.create(MEDIA_TYPE,file));

        RequestBody body = builder.build();

        //RequestBody body = RequestBody.create(MEDIA_TYPE_FILE, file);
        Request request = HttpUtils.createrRequest(FILE_UPLOAD_URL, body);

        /*
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
*/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure");
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                String s = response.body().string();
                Log.d(TAG, s);
            }
        });
    }

    public static void postRequestJson(JSONObject json) throws IOException {
        OkHttpClient client = HttpUtils.createOkHttpClient(HttpLoggingInterceptor.Level.BODY);
        String postBody = json.toString();

        Log.d(TAG, "postBody lenght = " + postBody.length());

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, postBody);
        Request request = HttpUtils.createrRequest(URL, body);

        /*
        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();
*/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure");
                call.cancel();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "onResponse");
                //String s = response.body().string();
                //Log.d(TAG, s);
            }
        });
    }
    public static OkHttpClient createOkHttpClient() {
        return createOkHttpClient(HttpLoggingInterceptor.Level.BODY);
    }

    public static OkHttpClient createOkHttpClient(HttpLoggingInterceptor.Level level) {
        /*
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new LogInterceptor()).build();
        return client;
        */
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(level);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        return client;
    }

    public static Request createrRequest(String url, RequestBody body) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);

        //builder.header("Content-Type", type);

        //User-Agent: bb-device
        builder.header("User-Agent", "bb-device");

        //Timestamp: 1524206935754
        String timestamp = String.valueOf(System.currentTimeMillis());
        builder.header("Timestamp", timestamp);
        //Sign: cd560d49427431998d81db2e5ae474dd
        builder.header("Sign", authorization(url, timestamp));

        builder.method(Methods.POST, body);
        return builder.build();
    }

    /**
     * 设signatureKey="iusP73@1/sdfPiz012-",
     * sign=MD5("http://172.16.3.5/i18n/hello?lang=zh-CNiusP73@1/sdfPiz012-1524206935754".getBytes("UTF-8")));
     * @param data
     * @param timestamp
     * @return
     */
    public static String authorization(String url, String timestamp){
        String param1 = url;
        String param2 = SECRET_KEY;
        String param3 = timestamp;
        StringBuilder authStr = new StringBuilder();
        authStr.append(param1).append(param2).append(param3);
        return MD5(authStr.toString());
    }

    /*
    public static String convertMethod(Command.Method method) {
        switch (method) {
            case GET:
                return Methods.GET;
            case POST:
                return Methods.POST;
        }
        return Methods.POST;
    }
    */
    private static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public class Methods {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String HEAD = "HEAD";
        public static final String OPTIONS = "OPTIONS";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
        public static final String TRACE = "TRACE";
    }

    private static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        return (new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.w("HttpConnection", "getHostnameVerifier");
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    } else if (getHostHome(host).equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        });
    }

    private static SSLSocketFactory getSSLSocketFactory() {
        File cerFile = new File(PATH_CER);
        if (cerFile.exists()) {//存在证书
            CertificateFactory certificateFactory = null;
            SSLContext sslContext = null;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(PATH_CER);
                certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                keyStore.setCertificateEntry("cer", certificateFactory.generateCertificate(fis));
                sslContext = SSLContext.getInstance("TLS");
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                        fis = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {//忽略证书
            SSLContext sslContext = null;
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] x509Certificates = new X509Certificate[0];
                    return x509Certificates;
                }
            }};
            try {
                sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new SecureRandom());
                return sslContext.getSocketFactory();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private static String getHostHome(String home) {
        if (home.startsWith("https://")) {
            home = home.substring(8);
        }
        if (home.startsWith("http://")) {
            home = home.substring(7);
        }
        int index = home.indexOf(":");
        return home.substring(0, index);
    }
}
