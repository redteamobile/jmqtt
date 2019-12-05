package org.jmqtt.java;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Alex Liu
 * @date 2019/11/20
 */
public class TestTCP {
    public static void main(String args[]) throws Exception{

        SSLContext ctx = SSLContext.getInstance("SSL");

        TrustManager[] myTrustManagerArray = new TrustManager[]{new TrustEveryoneManager()};


        ctx.init(null, myTrustManagerArray, null);
        SSLSocket socket = (SSLSocket) ctx.getSocketFactory().createSocket("localhost", 1883);

        // 1、创建客户端的 Socket 服务
        //Socket socket = clientWithoutCert();
        //Socket socket = new Socket("202.105.145.194", 1884);

        // 2、获取 Socket 流中输入流
        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        // 3、使用输出流将指定的数据写出去
        //out.write("TCP is coming !".getBytes());

        //out.write("TCP is coming 2222!".getBytes());

        out.write("TCP is coming 3333!".getBytes());

        System.out.println(in.read());

        // 4、关闭 Socket 服务
        //socket.close();
    }

    //信任所有服务端证书的trustManager
    static class TrustEveryoneManager implements X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }
        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType)
                throws CertificateException {
            return;
        }
        }
}
