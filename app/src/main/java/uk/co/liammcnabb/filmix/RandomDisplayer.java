package uk.co.liammcnabb.filmix;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;


public class RandomDisplayer extends ActionBarActivity {

    TextView fTitle,fRating,fRunTime,fPlot,fScore,fGenre;
    ImageView fPoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_random_displayer);

        fTitle = (TextView) findViewById(R.id.txtTitle);
        fRating = (TextView) findViewById(R.id.txtAgeRating);
        fRunTime = (TextView) findViewById(R.id.txtRunTime);
        fPlot = (TextView) findViewById(R.id.txtPlot);
        fScore = (TextView) findViewById(R.id.txtScore);
        fGenre = (TextView) findViewById(R.id.txtGenre);
        fPoster = (ImageView) findViewById(R.id.imgposter);



        testData();
    }

    public void testData()
    {
        String title = "Frozen";
        String year = "2013";
        String ageRating = "PG";
        String runtime="102 min";
        String plot = "When the newly crowned Queen Elsa accidentally uses her power" +
                " to turn things into ice to curse her home in infinite winter, her sister," +
                " Anna, teams up with a mountain man, his playful reindeer, and a snowman to" +
                " change the weather condition.";
        String poster = "http://ia.media-imdb.com/" +
                "images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX300.jpg";
        String metascore="74";
        String genre = "Animation, Adventure, Comedy";
        new DownloadImageTask((ImageView) findViewById(R.id.imgposter))
                .execute(poster);


        StringBuffer sb = new StringBuffer();
        sb.append(title);
        sb.append(" ("+ year + ")");
        fTitle.setText(sb.toString());
        fRating.setText(ageRating);
        fRunTime.setText(runtime);
        fPlot.setText(plot);
        fScore.setText(metascore);
        fGenre.setText(genre);

        fPoster.setAdjustViewBounds(true);
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

