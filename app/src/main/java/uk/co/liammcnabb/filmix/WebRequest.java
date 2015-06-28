package uk.co.liammcnabb.filmix;

/**
 * Created by joshtaylor on 28/06/15.
 */

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class WebRequest extends AsyncTask {
    String stream;

    public String requestFeed(final URL location) {
        return doInBackground(location);
    }

    protected String doInBackground(URL location)
    {
        String stream = new String();
        try {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(location.openStream(), "UTF-8"));
                for (String line; (line = reader.readLine()) != null; ) {
                    stream += line;
                }
            } catch (MalformedURLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }
}
