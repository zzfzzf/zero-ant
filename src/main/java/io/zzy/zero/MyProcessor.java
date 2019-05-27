package io.zzy.zero;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 一只小蚂蚁
 * creatBy zzy
 */
public class MyProcessor implements PageProcessor {
    // 需要抓取图片的页数
    private String filePath = "";
    private int count=0;
    private Set set=new HashSet();
    private Site site = Site.me().setRetryTimes(3).setSleepTime(0);

    public MyProcessor(String filePath) {
        this.filePath = filePath;
    }

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

    // 该次请求的数据
    public void process(Page page) {
       // String imageRegex = "(//wallpapers.wallhaven.cc/wallpapers/full/wallhaven-[0-9]+(.jp(e)?g|.png|.gif|.webp))";
       // String allUrl = "((http(s)?:)?(//)?[A-z,a-z,0-9,^,@,$,&,.,#,/,\\-,_,=]{2,9999})";
       // String wallHavenRegex = "https://alpha.wallhaven.cc/wallpaper/[0-9]+";
        try {
       /*    Html html = page.getHtml();
           Document document =html.getDocument();
            Elements elements = document.select("img[src]");
            Iterator iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element element = (Element) iterator.next();
                System.out.println(element);
            }*/

            Set<String> imageSet = new HashSet<String>();
            Set<String> requestSet = new HashSet<String>();

            String imageRegex = "(//[A-z,a-z,0-9,^,@,$,&,.,#,/,\\-,_,=]{2,9999}+(.jp(e)?g|.png|.gif|.webp))";
            String allUrl = "((http(s)?:)?(//)?)";
            String wallHavenRegex = "https://alpha.wallhaven.cc/wallpaper/[0-9]+";
            // 循环无限抓所有的链接
            List<String> list = page.getHtml().links().all();
            for (String s : list) {
                if (MatchStr(imageRegex, s) != null) {
                    imageSet.add(MatchStr(imageRegex, s));
                }
                if (MatchStr(wallHavenRegex, s) != null) {
                    requestSet.add(MatchStr(wallHavenRegex, s));
                }
            }

            for (String s : new ArrayList<>(imageSet)) {
                String str;
                if ((str = MatchStr(imageRegex, s)) != null) {
                    if (MatchStr(imageRegex, s).indexOf("https") == -1) {
                        DownLoadUtil.download("https:" + str, UUID.randomUUID().toString() + ".png");
                    }
                }
            }
            for (String s : list) {
                if(!requestSet.contains(s)&&!imageSet.contains(s)){
                    ZSpider.baseUrl.add(s);
                }
            }

            page.addTargetRequests(new ArrayList<>(requestSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String MatchStr(String rule, String str) {
        if (str == null) {
            return null;
        }
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public Site getSite() {
        return site;
    }
}

