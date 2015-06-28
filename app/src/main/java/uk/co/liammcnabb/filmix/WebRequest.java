package uk.co.liammcnabb.filmix;

/**
 * Created by joshtaylor on 28/06/15.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public String requestFeed(final String location) {

            return doInBackground(location);


    }
    protected String doInBackground(String... url) {
        String stream = "";
        URL location;
        try {
            location = new URL(url[0]);
            URI uri = new URI(location.getProtocol(), location.getUserInfo(),
                    location.getHost(), location.getPort(), location.getPath(),
                    location.getQuery(), location.getRef());
            location = uri.toURL();

            //Fails Here vvv
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(location.openStream(), "UTF-8"));
            // Fails Here ^^^

            for (String line; (line = reader.readLine()) != null; ) {
                stream += line;
            }


        } catch (MalformedURLException e)
        {
            //
        } catch (URISyntaxException e){
            //
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

}
