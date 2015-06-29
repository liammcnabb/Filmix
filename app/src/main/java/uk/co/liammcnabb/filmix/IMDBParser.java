package uk.co.liammcnabb.filmix;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by joshtaylor on 28/06/15.
 */
public class IMDBParser {

    public static ArrayList<String> parseFeed(String string)
    {
        boolean unsucessful=true;
        ArrayList<String> ids = new ArrayList<String>();
        ByteArrayInputStream in;
        in = new ByteArrayInputStream("".getBytes());
        while(unsucessful)
        {
            try{
                in = new ByteArrayInputStream(string.getBytes());
                unsucessful = false;
            } catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            if(parser.getName() == "link") { ids.add(parser.getText()); }

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ids;
    }

    public static Film parseFilm(String string)
    {
        String title = new String();
        String year = new String();
        String ageRating = new String();
        String runTime = new String();
        String plot = new String();
        String poster = new String();
        String metascore = new String();
        String genre = new String();
        String id = new String();

        ByteArrayInputStream in = new ByteArrayInputStream(string.getBytes());
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            /*

            <movie title="Shanghai Noon" year="2000" rated="PG-13" released="26 May 2000" runtime="110 min" genre="Action, Adventure, Comedy" director="Tom Dey" writer="Miles Millar, Alfred Gough" actors="Jackie Chan, Owen Wilson, Lucy Liu, Brandon Merrill" plot="A Chinese man who travels to the Wild West to rescue a kidnapped princess. After teaming up with a train robber, the unlikely duo takes on a Chinese traitor and his corrupt boss." language="English, Mandarin, Sioux, Spanish" country="USA, Hong Kong" awards="1 win & 7 nominations." poster="http://ia.media-imdb.com/images/M/MV5BMTI0MjE2MzUwOV5BMl5BanBnXkFtZTYwMTk5NjU3._V1_SX300.jpg" metascore="77" imdbRating="6.6" imdbVotes="86,918" imdbID="tt0184894" type="movie"/></root>

             */

            if(parser.getName() == "movie")
            {
                for(int i=0; i < parser.getAttributeCount(); i++)
                {
                    if(parser.getAttributeName(i) == "title") { title = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "year") { year = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "ageRating") { ageRating = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "runtime") { runTime = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "plot") { plot = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "poster") { poster = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "metascore") { metascore = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "genre") { genre = parser.getAttributeValue(i); }
                    else if(parser.getAttributeName(i) == "imdbID") { id = parser.getAttributeValue(i); }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //in.close();
        }


        return new Film(title, year, ageRating, runTime, plot, poster, metascore, genre, id);
    }

    public static ArrayList<Film> parseFilms(ArrayList<String> strings)
    {
        ArrayList<Film> films = new ArrayList<Film>();
        for(String string : strings)
            films.add(parseFilm(string));
        return films;
    }
}
