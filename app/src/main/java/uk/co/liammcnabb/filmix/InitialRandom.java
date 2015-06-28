package uk.co.liammcnabb.filmix;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class InitialRandom extends ActionBarActivity {

    LinearLayout settings;
    TextView authorID, customList;
    CheckBox idCheck, customCheck;
    ArrayList<String> filmIds;
    ArrayList<Film> filmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_initial_random);
        getSupportActionBar().setElevation(0);



        AdView adView = (AdView) this.findViewById(R.id.adsDisplay);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

        filmList = new ArrayList<Film>();
        final ImageButton randomButton = (ImageButton) findViewById(R.id.btnRandom);

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomize();
            }
        });

        settings = (LinearLayout) findViewById(R.id.lytAdvancedSettings);
        settings.setTag("off");

        authorID = (TextView) findViewById(R.id.txtAuthorId);
        idCheck = (CheckBox) findViewById(R.id.chkAuthorID);
        customList = (TextView) findViewById(R.id.txtCustomList);
        customCheck = (CheckBox) findViewById(R.id.chkCustomList);

        authorID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idCheck.setChecked(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        customList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customCheck.setChecked(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Starts the film process
        downloadFilmList();

        }

    public void downloadFilmList()
    {
        ArrayList<URL> urls = new ArrayList<URL>();
        String string = "http://rss.imdb.com/list/ls004274013/";
        try {
            urls.add(new URL("http://rss.imdb.com/list/ls004274013/"));
            urls.add(new URL("http://rss.imdb.com/list/ls000050035/"));
            urls.add(new URL("http://rss.imdb.com/list/ls070785016/"));
            urls.add(new URL("http://rss.imdb.com/list/ls070099591/"));
            urls.add(new URL("http://rss.imdb.com/list/ls009668711/"));
            urls.add(new URL("http://rss.imdb.com/list/ls009669258/"));
            urls.add(new URL("http://rss.imdb.com/list/ls076186778/"));
            urls.add(new URL("http://rss.imdb.com/list/ls002875598/"));
            urls.add(new URL("http://rss.imdb.com/list/ls072631945/"));
            urls.add(new URL("http://rss.imdb.com/list/ls056161235/"));
            urls.add(new URL("http://rss.imdb.com/list/ls009674465/"));
            urls.add(new URL("http://rss.imdb.com/list/ls009668314/"));
        }
        catch (MalformedURLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        for(URL url : urls) {
            filmIds.addAll(IMDBParser.parseFeed(WebRequest.requestFeed(url)));
        }
    }

    public void advancedSettings()
    {

        if(settings.getTag().equals("off"))
        {
            settings.setVisibility(View.VISIBLE);
            settings.setTag("on");
        } else if (settings.getTag().equals("on"))
        {
            settings.setVisibility(View.INVISIBLE);
            settings.setTag("off");
        }
    }

    public void randomize()
    {
        Intent intent = new Intent(InitialRandom.this,RandomDisplayer.class);
        if(customCheck.isChecked())
        {
            //TODO Create Random List using Custom List
        } else if (idCheck.isChecked())
        {
            //TODO Create Author Check to Validate Author ID

            //TODO Create Random List using user's Author ID
        } else
        {
            //TODO Use Watchlist's to create a list of new Random Movies
            //dummyData();
            createNewFilmList();
            intent.putExtra("FilmList", new Wrapper(filmList));
        }
        startActivity(intent);
    }

    public void createNewFilmList()
    {
        ArrayList<Film> list = new ArrayList<Film>();
        for(int i = 0; i<10; i++)
        {
            Random random = new Random();
            int ran = random.nextInt(filmIds.size());
            try {
                list.add(IMDBParser.parseFilm(WebRequest.requestFeed(new URL("http://www.omdbapi.com/?i=" + filmIds.get(ran) + "&plot=short&r=xml"))));
            } catch (MalformedURLException e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        filmList = list;
    }

    public void dummyData()
    {
        ArrayList<Film> list = new ArrayList<Film>();

        for(int i=0; i<10; i++)
        {
            Film f = new Film(
                    "Frozen " + i,
                    "2013",
                    "PG",
                    "102 min",
                    "When the newly crowned Queen Elsa accidentally uses her power" +
                            " to turn things into ice to curse her home in infinite winter, her sister," +
                            " Anna, teams up with a mountain man, his playful reindeer, and a snowman to" +
                            " change the weather condition.",
                    "http://ia.media-imdb.com/" +
                    "images/M/MV5BMTQ1MjQwMTE5OF5BMl5BanBnXkFtZTgwNjk3MTcyMDE@._V1_SX300.jpg",
                    "74",
                    "Animation, Adventure, Comedy",
                    "tt2294629"
            );
            Log.d("Film " + i + ":", f.toString());
            list.add(f);
        }
        filmList = list;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.initial_random, menu);
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
            //advancedSettings();
            startActivity(new Intent(this,SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
