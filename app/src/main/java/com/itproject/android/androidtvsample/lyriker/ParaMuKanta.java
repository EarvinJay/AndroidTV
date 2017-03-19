/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itproject.android.androidtvsample.lyriker;

import java.util.ArrayList;

/**
 *
 * @author Kapilya
 */
public class ParaMuKanta {
    
    private ArrayList<String> lyrics;
    private ArrayList<String> timing;

    public ParaMuKanta(ArrayList<String> lyrics, ArrayList<String> timing) {
        this.lyrics = lyrics;
        this.timing = timing;
    }

    public ArrayList<String> getLyrics() {
        return lyrics;
    }

    public void setLyrics(ArrayList<String> lyrics) {
        this.lyrics = lyrics;
    }

    public ArrayList<String> getTiming() {
        return timing;
    }

    public void setTiming(ArrayList<String> timing) {
        this.timing = timing;
    }
    
    
    
}
