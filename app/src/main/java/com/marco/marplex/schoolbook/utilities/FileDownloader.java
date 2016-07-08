package com.marco.marplex.schoolbook.utilities;

import android.content.Context;

import com.marco.marplex.schoolbook.interfaces.Updateable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by marco on 6/3/16.
 */

public class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;

    public static void downloadFile(String fileUrl, File directory, Context c, Updateable updateable){
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:37.0) Gecko/20100101 Firefox/37.0");
            urlConnection.setRequestProperty("Set-Cookie", "PHPSESSID=" + Credentials.getSession(c));
            urlConnection.setRequestProperty("Cookie", "PHPSESSID=" + Credentials.getSession(c));
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            System.out.println("Total size is: " + totalSize);

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            long total = 0;

            while( (bufferLength = inputStream.read(buffer)) != -1 ){
                total += bufferLength;
                if (totalSize > 0) { // only if total size is known
                    updateable.onDownloadProgress((int) (total * 100 / totalSize));
                }

                fileOutputStream.write(buffer, 0, bufferLength);
            }

            fileOutputStream.close();
            urlConnection.disconnect();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}