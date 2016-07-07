package com.google.engedu.ghost;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class TrieNode {

    private static ArrayList<String> mylist;
    private HashMap<Character, TrieNode> children;
    private boolean isWord;
    private static String mainprefix;
    public TrieNode(){
        children = new HashMap<Character,TrieNode>();
        isWord = false;
    }

    public void add(String s) {
        TrieNode crawl = this;
        int n = s.length();
        for(int i=0;i<n;i++){
            char ch = s.charAt(i);
            if(crawl.children.containsKey(ch)){
                crawl = crawl.children.get(ch);
                if(i == n-1)
                {
                    crawl.isWord = true;
                }
            }
            else {
                crawl.children.put(ch, new TrieNode());
                TrieNode temp = crawl.children.get(ch);
                if(i == n-1){
                    temp.isWord = true;
                }
                crawl = temp;
            }
        }
    }

    public boolean isWord(String s) {

        TrieNode crawl = this;
        int n = s.length();
        for(int i=0;i<n;i++){
            char ch = s.charAt(i);
            if(crawl.children.get(ch) == null){
                return false;
            }
            else {
                crawl = crawl.children.get(ch);
                if(i==n-1 && crawl.isWord == true){
                    return true;
                }

            }
        }
        return false;
    }


    public String getAnyWordStartingWith(String prefix) {

        if (prefix.length() == 0) {
            int rand = new Random().nextInt(26) + 1;
            char c = (char)('a' + rand);
            return String.valueOf(c);
        }
        TrieNode crawl = this;
        mainprefix = prefix;
        int n = prefix.length();
        for(int i=0;i<n;i++){
            char ch = prefix.charAt(i);
            crawl = crawl.children.get(ch);
            Log.d("inpretraceloop",i+"");
        }
         mylist = new ArrayList<String>();;
         recursivelytrace(crawl,prefix);
         Log.d("myistsize",mylist.size()+"");
         if(mylist.size()==0)
             return null;
        else
         return mylist.get(new Random().nextInt(mylist.size()));
    }

    void recursivelytrace(TrieNode crawl,String prefix)
    {
        Iterator it;
       try{
           it = crawl.children.entrySet().iterator();
       }
       catch(NullPointerException e)
       {
           //first attempt failed bcoz word is not in a list
           return;
       }
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            TrieNode temp = crawl.children.get(pair.getKey());
            Log.d("prfix+valuetrue",isWord(prefix+pair.getKey())+"");
            if(this.isWord(prefix+pair.getKey()))
                mylist.add(prefix+pair.getKey());
            if(!temp.children.isEmpty())
                recursivelytrace(temp,prefix+pair.getKey());
        }
    }
    public String getGoodWordStartingWith(String prefix) {

        if (prefix.length() == 0) {
            int rand = new Random().nextInt(26) + 1;
            char c = (char)('a' + rand);
            return String.valueOf(c);
        }
        TrieNode crawl = this;
        int n = prefix.length();
        for(int i=0;i<n;i++){
            char ch = prefix.charAt(i);
            crawl = crawl.children.get(ch);
            Log.d("inpretraceloop",i+"");
        }
        mylist = new ArrayList<String>();;
        smartRecursivelytrace(crawl,prefix);
        Log.d("myistsize",mylist.size()+"");
        if(mylist.size()==0)
            return null;
        else
            return mylist.get(new Random().nextInt(mylist.size()));
    }
    void smartRecursivelytrace(TrieNode crawl,String prefix)
    {
        Iterator it;
        try{
            it = crawl.children.entrySet().iterator();
        }
        catch(NullPointerException e)
        {
            //first attempt failed bcoz word is not in a list
            return;
        }
        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();
            TrieNode temp = crawl.children.get(pair.getKey());
            Log.d("prfix+valuetrue",isWord(prefix+pair.getKey())+"");
            if(this.isWord(prefix+pair.getKey()) && (prefix+pair.getKey()).length()%2 == mainprefix.length()%2)
                mylist.add(prefix+pair.getKey());
            if(!temp.children.isEmpty())
                smartRecursivelytrace(temp,prefix+pair.getKey());
        }
    }
}
