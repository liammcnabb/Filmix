package uk.co.liammcnabb.filmix;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dingle on 19/06/2015.
 */
public class Wrapper implements Serializable {

    private ArrayList<Film> films;

    public Wrapper(ArrayList<Film> selections) {
        this.films = selections;
    }

    public ArrayList<Film> getFilms() {
        return this.films;
    }

}