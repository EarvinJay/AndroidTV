/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itproject.android.androidtvsample.lyriker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * @author Kapilya
 */
public class LyricsFileExtractor {

    public static ParaMuKanta readTextFile(String fileName) {

        String returnValue = "";
        FileReader file = null;
        BufferedReader reader = null;
        // arraylist for the phrases of the lyrics
        ArrayList<String> phrases = new ArrayList<>();
        // arraylist for the timing of the end time
        ArrayList<String> phrasesTiming = new ArrayList<>();
        // temp arraylist to form one phrase
        ArrayList<String> syllable = new ArrayList<>();

        try {
            file = new FileReader(fileName);
            reader = new BufferedReader(file);
            String line, onesyllable;
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss.SSS");
            // used to skip the first 2 lines
            int skipLines = 0;
            Date timing = new Date(); 
            
            while ((line = reader.readLine()) != null) {
                if (skipLines > 1) {
                    StringTokenizer st = new StringTokenizer(line, "\t");
                    while(st.hasMoreTokens()){
                        String timeformat = st.nextToken();  
                        String lyricPart = st.nextToken();
                        if(lyricPart.substring(0, 4).equals("Text")){
                            // gi replace ang double quote para ma wala
                            onesyllable=lyricPart.substring(6).replace("\"", "");
                            System.out.println(onesyllable);
                            if(onesyllable.charAt(0) == '/' || onesyllable.charAt(0) == '\\'){
                                phrases.add(combineToOnePhrase(syllable));
                                phrasesTiming.add(sdf.format(timing));
                                syllable = new ArrayList<>();
                                syllable.add(onesyllable);
                            }
                            else{
                                syllable.add(onesyllable);                   
                                timing = sdf.parse(timeformat);
                                System.out.println(onesyllable+":"+sdf.format(timing));
                            }
                        }
                    
                    }
                    
                    
                } else {
                    skipLines++;
                }
                
                
                 System.out.println("================================================");
                
            }
            
            for (int i = 0; i < phrases.size(); i++) {
                System.out.println(phrasesTiming.get(i)+" "+phrases.get(i));
            }
            phrases.remove(0);
            phrasesTiming.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file != null || reader != null) {
                try {
                    reader.close();
                    file.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ParaMuKanta(phrases, phrasesTiming);
    }

    public static String combineToOnePhrase(ArrayList<String> onePhrase) {
        String complete = "";
        for (String s : onePhrase) {
            complete += s;
        }
        return complete;
    }

}
