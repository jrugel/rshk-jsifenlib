package com.roshka.sifen.http;

import com.roshka.sifen.exceptions.SifenException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class HTTPHelper {

    public static void request(SSLSocketFactory sslSocketFactory, String url)
        throws SifenException
    {
        try {
            URL actualURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) actualURL.openConnection();
            connection.setInstanceFollowRedirects(false);
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection)connection).setSSLSocketFactory(sslSocketFactory);
            }
            connection.setRequestMethod("GET");
            //connection.setDoOutput(true);
            //DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            DataInputStream in = new DataInputStream(connection.getInputStream());
            connection.connect();
            byte[] buff = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int count;
            while ((count = in.read(buff)) != -1) {
                baos.write(buff, 0, count);
            }

            System.out.println("Headers");
            System.out.println("-------");

            Map<String, List<String>> headerFields = connection.getHeaderFields();
            if (headerFields != null) {
                for (String key : headerFields.keySet()) {
                    List<String> strings = headerFields.get(key);
                    System.out.println(key + ": [" + String.join(",", strings) + "]");
                }
            }

            System.out.println(new String(baos.toByteArray()));



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}