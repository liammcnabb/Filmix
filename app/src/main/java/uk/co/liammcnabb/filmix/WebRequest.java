package uk.co.liammcnabb.filmix;

/**
 * Created by joshtaylor on 28/06/15.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class WebRequest extends AsyncTask<String, Void, String> {
    String stream;

   // public WebRequest(String location)
    //{
    //    requestFeed(location);
    //}

   /** public String requestFeed(final String location) {

            return doInBackground(location);


    }**/
    protected String doInBackground(String... url) {
        String stream = "";
        URL location;
        try {
            location = new URL(url[0]);
            //URI uri = new URI(location.getProtocol(), location.getUserInfo(),
            //        location.getHost(), location.getPort(), location.getPath(),
            //        location.getQuery(), location.getRef());
            //location = uri.toURL();

            //Fails Here vvv
            Log.d("WebRequest","location: " + location);
            try{
                Reader r = new InputStreamReader(location.openStream(),"UTF-8");
                BufferedReader reader = new BufferedReader(r);
                for (String line; (line = reader.readLine()) != null; ) {
                    stream += line;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (MalformedURLException e)
        {
            //
        //} catch (URISyntaxException e){
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    protected void onPostExecute(String feed)
    {
        //filmIds.addAll(IMDBParser.parseFeed(feed));
    }

}
