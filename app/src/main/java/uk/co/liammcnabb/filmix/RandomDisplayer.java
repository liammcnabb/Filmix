package uk.co.liammcnabb.filmix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.InputStream;
import java.util.ArrayList;


public class RandomDisplayer extends ActionBarActivity {

    final String imdbString = "http://www.imdb.com/title/";
    TextView fTitle,fRating,fRunTime,fPlot,fScore,fGenre;
    ImageView fPoster;
    Button imdb;
    String lastId;
    int listIndex = 0;
    ArrayList<Film> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_random_displayer);
        AdView adView = (AdView) this.findViewById(R.id.adsDisplay2);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        fTitle = (TextView) findViewById(R.id.txtTitle);
        fRating = (TextView) findViewById(R.id.txtAgeRating);
        fRunTime = (TextView) findViewById(R.id.txtRunTime);
        fPlot = (TextView) findViewById(R.id.txtPlot);
        fScore = (TextView) findViewById(R.id.txtScore);
        fGenre = (TextView) findViewById(R.id.txtGenre);
        fPoster = (ImageView) findViewById(R.id.imgposter);
        imdb = (Button) findViewById(R.id.btnImdb);
        Intent intent = getIntent();

        Wrapper selections = (Wrapper) intent.getSerializableExtra("FilmList");
        list = selections.getFilms();

        final ImageButton randomButton = (ImageButton) findViewById(R.id.btnFurtherRandom);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listIndex++;
                randomize();
            }
        });
        randomize();


    }

    public void testData()
    {

    }

    public void randomize()
    {

        if(listIndex != 10)
        {
            final String imdbString = list.get(listIndex).getImdbString();
            try
            {
                new DownloadImageTask((ImageView) findViewById(R.id.imgposter))
                        .execute(list.get(listIndex).getPoster());
            }
            catch(Exception e)
            {
                Log.e("RandomDisplayer","Download Poster");
            }

            list.get(listIndex).toString();
            lastId = list.get(listIndex).getId();
            StringBuffer sb = new StringBuffer();
            sb.append(list.get(listIndex).getTitle());
            sb.append(" ("+ list.get(listIndex).getYear() + ")");
            fTitle.setText(sb.toString());
            fRating.setText(list.get(listIndex).getAgeRating());
            fRunTime.setText(list.get(listIndex).getRunTime());
            fPlot.setText(list.get(listIndex).getPlot());
            fScore.setText(list.get(listIndex).getMetascore());
            fGenre.setText(list.get(listIndex).getGenre());

            fPoster.setAdjustViewBounds(true);

            imdb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(imdbString));
                    startActivity(browserIntent);
                }
            });
        } else
        {
            listIndex--;
        }

    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_random_displayer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


