package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 3;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private BufferedReader in;
    private Random random = new Random();
    private  ArrayList wordList;
    private HashSet wordSet;
    private HashMap lettersToWords;
    private  String key;
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        in= new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        wordList = new ArrayList();
        wordSet = new HashSet();
        lettersToWords = new HashMap<String,ArrayList>();
        int i=0;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(i++,word);
            wordSet.add(word);
            ArrayList al = new ArrayList();
            key = sortLetters(word);
            if(!lettersToWords.containsKey(key)){
                al.add(word);
                lettersToWords.put(key,al);
            }
            else{
                al = (ArrayList) lettersToWords.get(key);
                al.add(word);
                lettersToWords.put(key, al);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word) && !word.contains(base))
            return true;
        else
            return false;
    }

    public ArrayList<String> getAnagrams(String targetWord){
        String tword = targetWord.trim().toLowerCase();
        tword = sortLetters(tword);
        return (ArrayList<String>) lettersToWords.get(tword);
    }

    public static String sortLetters(String str)
    {
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> al = null;
        for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {
            String newword = targetWord + alphabet;
            newword = sortLetters(newword);

            if(lettersToWords.containsKey(newword))
            {
                al = (ArrayList<String>) lettersToWords.get(newword);
            }
            for(int i = 0;i< al.size();i++)
                result.add(String.valueOf(al.get(i)));
       }
       return result;
    }


    public String pickGoodStarterWord() {
        int num = 0;
        ArrayList res;
        String randomWord = null;
        while(true) {
            num = random.nextInt(9999) + 1;
            randomWord = (String) wordList.get(num);
            res = (ArrayList<String>) lettersToWords.get(sortLetters(randomWord));
            if(res.size() >= MIN_NUM_ANAGRAMS && randomWord.length() < MAX_WORD_LENGTH){
                return randomWord;
            }
        }
    }
}
