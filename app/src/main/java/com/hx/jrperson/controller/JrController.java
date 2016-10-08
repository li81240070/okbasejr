package com.hx.jrperson.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hx.jrperson.bean.entity.VersionEntity;
import com.hx.jrperson.consts.API;
import com.hx.jrperson.consts.Consts;
import com.hx.jrperson.utils.JrUtils;
import com.hx.jrperson.utils.common.util.PreferencesUtils;
import com.hx.jrperson.utils.httpmanager.NetLoader;
import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * 获取版本号
 * Created by ge on 2016/4/6.
 */
public class JrController {

    private Handler handler = new Handler();

    public static void getVersion(final Context context) {
        String url = API.VERSION;
        Map<String, String> map = new HashMap<>();
        map.put(Consts.OS, "1");
        map.put(Consts.TYPE, "1");
        url = JrUtils.appendParams(url, map);
        NetLoader.getInstance(context).loadGetData(url, new NetLoader.NetResponseListener() {
            @Override
            public void success(String resultString, int code) {
                if (code == 200) {
                    Gson gson = new Gson();
                    final VersionEntity versionEntity = gson.fromJson(resultString, VersionEntity.class);
                    if (versionEntity != null) {
                        String versionNow = versionEntity.getDataMap().getVersion();
                        PreferencesUtils.putString(context, Consts.VERSION, versionNow);
                        String versionBefore = JrUtils.getAppVersionName(context);
                        if (!versionNow.equals(versionBefore)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("有新版本, 请更新后使用");
                            builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final Uri uri = Uri.parse(versionEntity.getDataMap().getPath());
                                    Log.i("wangzhi", uri.toString());
                                    final Intent update = new Intent(Intent.ACTION_VIEW, uri);
                                    context.startActivity(update);
                                }
                            });
                            builder.setCancelable(false);
                            builder.create().show();
                        }
                    }
                }
            }

            @Override
            public void fail(String failString, Exception e) {

            }
        });
    }


    public static void setCertificates(Context context, OkHttpClient client, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            client.setSslSocketFactory(sslContext.getSocketFactory());
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
