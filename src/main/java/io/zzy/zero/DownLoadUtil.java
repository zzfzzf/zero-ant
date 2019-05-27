package io.zzy.zero;

import us.codecraft.webmagic.Page;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class DownLoadUtil {
    /**
     * 下载图片工具
     *
     * @param urlString 图片链接地址
     * @param savePath  图片保存的路径
     * @throws Exception
     */
    public static void download(String urlString, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 设置请求头
        con.addRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)");
        // 设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }


        OutputStream os = new FileOutputStream(sf.getPath()+"\\"+ UUID.randomUUID().toString()+".jpg");
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }
}