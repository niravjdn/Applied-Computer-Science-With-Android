package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private Random r = new Random();
    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {

         if(prefix.length()==0)
         {
            int rand = r.nextInt(9999)+1;
             return String.valueOf(words.get(rand).charAt(0));
         }
        Log.d("arraysize",words.size()+"");
        int first =0,last=words.size(),middle=0;
        String temp;

       /* while( first <= last )
        {
            middle = (first + last)/2;
            temp = words.get(middle).substring(0,prefix.length());
            if (words.get(middle).compareTo(prefix)==0)
                return words.get(middle);
            else if ( words.get(middle).compareTo(prefix) < 0)
                last = middle - 1;
            else
                first = middle + 1;
        } */


        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.substring(0, Math.min(rhs.length(),lhs.length())).compareToIgnoreCase(rhs);
            }
        };
        int index = Collections.binarySearch(words,prefix,comparator);
        if(index >= 0){
            return words.get(index);
        }
        else{
            return null;
        }


      /*  for (String w : words) {
            if(w.length()>=prefix.length() && w.substring(0,prefix.length()).equalsIgnoreCase(prefix)) //why not exception
            {
                Log.d("success for fiding word","yes");
                Log.d("word",w);
                return w;
            }
        } */
        //return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        return selected;
    }
}
