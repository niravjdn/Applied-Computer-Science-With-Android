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
    private int index;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while ((line = in.readLine()) != null) {
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

        Log.d("prefix", prefix+" "+prefix.length() + "");
        if (prefix.length() == 0) {
            int rand = r.nextInt(9999) + 1;
            index = rand;
            return String.valueOf(words.get(rand).charAt(0));
        }
        int first = 0, last = words.size(), middle = 0;


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
                return lhs.substring(0, Math.min(rhs.length(), lhs.length())).compareToIgnoreCase(rhs);
            }
        };
        index = Collections.binarySearch(words, prefix, comparator);
        if (index >= 0) {
            Log.d("index", index + "");
            Log.d("wordatindex",words.get(index));
            return words.get(index);
        } else {
            Log.d("giveanywordmethod","null");
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
    public String getGoodWordStartingWith(String midword) {
        int i = index, j = index;
        Log.d("prefix",midword);
        ArrayList<String> ans = new ArrayList<>();
        ans.add(words.get(index));
        while (++i<=words.size() && words.get(i).length()>=midword.length() && words.get(i).substring(0,midword.length()).equalsIgnoreCase(midword)) {
            if(words.get(i).length()%2 == midword.length()%2)
            {
                ans.add(words.get(i));
                Log.d("substringi",words.get(i).substring(0,midword.length()));
            }
        }
        while (--j >= 0 && words.get(j).length()>=midword.length() && words.get(j).substring(0,midword.length()).equalsIgnoreCase(midword)) {
            if(words.get(i).length()%2 == midword.length()%2)
            {
                ans.add(words.get(j));
                Log.d("substringj",words.get(j).substring(0,midword.length()));
            }
        }


        Log.d("iandj",i+" "+j);
        try {
            Log.d("goodwodloop","in try block");
            for(int n=0;n<3;n++)
            {
                String q = ans.get(new Random().nextInt(ans.size() + 1));;
                if(q.length()+1!=words.get(index).length())
                {
                    return ans.get(new Random().nextInt(ans.size() + 1));
                }
                Log.d("goodwodloop",n+"");
            }
            return ans.get(0);
        } catch (Exception e) {
            return null;
        }

    }
}
