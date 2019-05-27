package io.zzy.zero;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;

import javax.net.ssl.*;
import java.io.FilterOutputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ZSpider {
    static String url = "https://alpha.wallhaven.cc/random";

    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);
    public static List<String> baseUrl= new ArrayList<>();
    static {
        try {
            // 重置HttpsURLConnection的DefaultHostnameVerifier，使其对任意站点进行验证时都返回true
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            // 创建随机证书生成工厂
            //SSLContext context = SSLContext.getInstance("TLS");
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());

            // 重置httpsURLConnection的DefaultSSLSocketFactory， 使其生成随机证书
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            int count =1;
            baseUrl.add("https://alpha.wallhaven.cc/random?page=1");
            while(!baseUrl.isEmpty()){
                url = "https://alpha.wallhaven.cc/random?page="+count;
                Spider.create(new MyProcessor("")).addUrl(url).thread(10).run();
                count++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
