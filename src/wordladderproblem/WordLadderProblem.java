/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordladderproblem;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;  
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author Sayyed Shozib Abbas
 */
public class WordLadderProblem {

public static void main(String[] args) {
    JSONParser parser = new JSONParser();
    JSONObject wordDictionary = new JSONObject();

    try {
        Object obj = parser.parse(new FileReader("e:\\dictionary.json"));

        wordDictionary = (JSONObject) obj;

    } catch (Exception ex) {
        Logger.getLogger(WordLadderProblem.class.getName()).log(Level.SEVERE, null, ex);
    }

    String startWord = "FOOL";
    String endWord = "SAGE";

    Set<String> dictionary = buildDictionary(wordDictionary, startWord.length());
    Ladder result = buildLadder(startWord, endWord, dictionary);
    
    ArrayList<Ladder> all = executioner(wordDictionary);    

    for(int i = 0; i < all.size(); i++) {
        System.out.println(all.get(i).getPath());
    }
    
    if(result!=null){
     System.out.println("Length is "+result.getLength() + " and path is :"+ result.getPath());
    }else{
     System.out.println("No Path Found");
  }
 
 }

public static ArrayList<Ladder> executioner(JSONObject dict) {
    ArrayList<Ladder> temp = new ArrayList<Ladder>();
    List<String> keys1 = new ArrayList(dict.keySet());

    for(int a = 0; a < keys1.size(); a++) {
        String curWord = keys1.get(a);
        Set<String> customDict = buildDictionary(dict, curWord.length());
        List<String> keys2 = new ArrayList(customDict);
        for(int i = 0; i < keys2.size(); i++) {
            String obsWord = keys2.get(i);

            if(curWord.length() != obsWord.length())
                continue;
            Ladder bl = buildLadder(curWord, obsWord, customDict);
            temp.add(bl);
        }
    }  
    
    return temp;
}

public static ArrayList<ArrayList<Ladder>> minChains(JSONObject dict) {
    ArrayList<ArrayList<Ladder>> temp = new ArrayList<ArrayList<Ladder>>();
    ArrayList<Ladder> temp2 = new ArrayList<Ladder>();
    List<String> keys1 = new ArrayList(dict.keySet());

    for(int a = 0; a < keys1.size(); a++) {
        String curWord = keys1.get(a);
        int minLength = 99999;
        Set<String> customDict = buildDictionary(dict, curWord.length());
        List<String> keys2 = new ArrayList(customDict);
        for(int i = 0; i < keys2.size(); i++) {
            String obsWord = keys2.get(i);
            if(curWord.length() != obsWord.length())
                continue;
            Ladder bl = buildLadder(curWord, obsWord, customDict);
            if(bl.getLength() < minLength) {
                temp2.clear();
                temp2.add(bl);
                minLength = bl.getLength();
            }
            else
            if(bl.getLength() == minLength) {
                temp2.add(bl);
            }  
        }
        temp.add(temp2);
    }  
    
    return temp;    
}

public static ArrayList<Ladder> maxChains(JSONObject dict) {
    ArrayList<Ladder> temp = new ArrayList<Ladder>();
    List<String> keys1 = new ArrayList(dict.keySet());

    for(int a = 0; a < keys1.size(); a++) {
        String curWord = keys1.get(a);
        int maxLength = 0;
        Set<String> customDict = buildDictionary(dict, curWord.length());
        List<String> keys2 = new ArrayList(customDict);
        for(int i = 0; i < keys2.size(); i++) {
            String obsWord = keys2.get(i);
            if(curWord.length() != obsWord.length())
                continue;
            Ladder bl = buildLadder(curWord, obsWord, customDict);
            if(bl.getLength() > maxLength) {
                temp.clear();
                temp.add(bl);
                maxLength = bl.getLength();
            }
            else
            if(bl.getLength() == maxLength) {
                temp.add(bl);
            }  
        }
    }  
    
    return temp;    
}

public static Set<String> buildDictionary(JSONObject dict, int wordLength) {
    Set<String> temp = new HashSet<String>();
    List<String> temp2 = new ArrayList<String>(dict.keySet());
    
    for(int a = 0; a < temp2.size(); a++) {
        if(temp2.get(a).length() == wordLength)
            temp.add(temp2.get(a));
    }    
    
    return temp;
}
 
public static Ladder buildLadder(String startWord, String endWord, Set<String> dictionary) {
    if(dictionary.contains(startWord) && dictionary.contains(endWord)){

     List<String> path = new LinkedList<String>();
     path.add(startWord);

     //All intermediate paths are stored in queue.
     Queue<Ladder> queue = new LinkedList<Ladder>(); 
     queue.add(new Ladder(path, 1, startWord));

     //We took the startWord in consideration, So remove it from dictionary, otherwise we might pick it again.
     dictionary.remove(startWord);

     //Iterate till queue is not empty or endWord is found in Path.
     while(!queue.isEmpty() && !queue.peek().equals(endWord)){
      Ladder ladder = queue.remove();

      if(endWord.equals(ladder.getLastWord())){
       return ladder;
      }

      Iterator<String> i = dictionary.iterator();
      while (i.hasNext()) {
       String string = i.next(); 

       if(differByOne(string, ladder.getLastWord())){

        List<String> list = new LinkedList<String>(ladder.getPath());
        list.add(string);

        //If the words differ by one then dump it in Queue for later processsing.
        queue.add(new Ladder(list, ladder.getLength()+1, string));

        //Once the word is picked in path, we don't need that word again, So remove it from dictionary.
        i.remove();
       }
      }
   }
    
   //Check is done to see, on what condition above loop break, 
   //if break because of Queue is empty then we didn't got any path till endWord.
   //If break because of endWord matched, then we got the Path and return the path from head of Queue.
   if(!queue.isEmpty()){
    return queue.peek();
   }
  }
  return null;
 }
 
 private static boolean differByOne(String word1, String word2){
  if (word1.length() != word2.length()) {
   return false;
  }
 
  int diffCount = 0;
  for (int i = 0; i < word1.length(); i++) {
   if (word1.charAt(i) != word2.charAt(i)) {
    diffCount++;
   }
  }
  return (diffCount == 1);
 }
    
}
