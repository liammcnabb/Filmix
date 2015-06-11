package uk.co.liammcnabb.filmix;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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


public class InitialRandom extends ActionBarActivity {

    LinearLayout settings;
    TextView authorID, customList;
    CheckBox idCheck, customCheck;

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
        }
        startActivity(new Intent(InitialRandom.this,RandomDisplayer.class));
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
            advancedSettings();
        }

        return super.onOptionsItemSelected(item);
    }
}
