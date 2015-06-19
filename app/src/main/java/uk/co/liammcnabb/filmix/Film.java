package uk.co.liammcnabb.filmix;

import org.apache.http.entity.SerializableEntity;

import java.io.Serializable;

/**
 * Created by Dingle on 19/06/2015.
 */
public class Film implements Serializable {
    private String title;
    private String year;
    private String ageRating;
    private String runTime;
    private String plot;
    private String poster;
    private String metascore;
    private String genre;
    private String id;
    private String imdbString;

    final String imdbBase = "http://www.imdb.com/title/";


    public Film(
            String title,
            String year,
            String ageRating,
            String runTime,
            String plot,
            String poster,
            String metascore,
            String genre,
            String id
    )
    {
        setTitle(title);
        setYear(year);
        setAgeRating(ageRating);
        setRunTime(runTime);
        setPlot(plot);
        setPoster(poster);
        setMetascore(metascore);
        setGenre(genre);
        setId(id);
        setImdbString(imdbString + id + "/");
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMetascore() {
        return metascore;
    }

    public void setMetascore(String metascore) {
        this.metascore = metascore;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImdbString() {
        return imdbString;
    }

    public void setImdbString(String imdbString) {
        this.imdbString = imdbString;
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", ageRating='" + ageRating + '\'' +
                ", runTime='" + runTime + '\'' +
                ", plot='" + plot + '\'' +
                ", poster='" + poster + '\'' +
                ", metascore='" + metascore + '\'' +
                ", genre='" + genre + '\'' +
                ", id='" + id + '\'' +
                ", imdbString='" + imdbString + '\'' +
                ", imdbBase='" + imdbBase + '\'' +
                '}';
    }
}
