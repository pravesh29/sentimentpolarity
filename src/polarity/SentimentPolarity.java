package polarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentPolarity {
	
	public Double pmi;
	
	// positive lexicon seed list
	private List<String> find1;

	// Negatve lexicon seed list
	private List<String> find2;
	
	// WindowSize
	private Integer windowSize;
	
	private static Map<String, Integer> positive_words = new HashMap<String, Integer>();
	private static Map<String, Integer> negative_words = new HashMap<String, Integer>();
	
	// Smoothing value
	private static double smoothing_val = 0.01;
	
	// Construtor which will be called on class initialisation
	public SentimentPolarity(List<String> find1, List<String> find2, Integer windowsize){
		this.find1=find1;
		this.find2=find2;
		this.windowSize = windowsize;
	}
	
	// this function returns the Semantic orientation score values from positive words map and negative words map.
	// this positive and negative words map has word as key and count of occurences around positive/negative lexicon
	// as its value
	
	public Map<String, Double> calcSOScore(Map<String, Integer> pos_map, Map<String, Integer> neg_map){
		Map<String, Double> soMap = new HashMap<String, Double>();
		
		ArrayList<String> list = new ArrayList<>();
		list.addAll(pos_map.keySet());
		list.addAll(neg_map.keySet());
		
		// variable to count the occurences of positive lexicon
		Integer hits_positive = 0;
		
		// variable to count the occurences of negative lexicon
		Integer hits_negative = 0;
		
		//Temporary variable used
		Integer val = 0;
		
		// Calculate the hits(positive words); It is analogous to hits(excellent) in the given formula
		// If seed word list is passed it calculates the sum for count for all words
		for (String find : find1){
				if(pos_map.get(find) == null){
					val = 0 ;
				} else {
					val = pos_map.get(find);
			}
			hits_positive = hits_positive + val;
			
			// Print counts for positive vocabulary
			//System.out.println( find + " : "  + val);
			
		}
		// Calculate the hits(negative words); It is analogous to hits(poor) in the given formula
		for (String find : find2){
			
			if(neg_map.get(find) == null){
				val = 0 ;
			} else {
				val = neg_map.get(find);
		}
			
			hits_negative = hits_negative + val;
			// Print counts for negative vocabulary			
			//System.out.println( find + " : "  + val);		
		}
		
		// Loop thru all words and calculate the polarity based on given positive and negative words
		for (String word : list)
		{
			Double count_pos;
			Double count_neg;
			
			try{
				// occurences of the word around positive lexicon
				count_pos = (double) pos_map.get(word) + smoothing_val;
				
			} catch(NullPointerException e){
				
				count_pos = smoothing_val;
			}
			try{
				
				// occurences of the word around negative lexicon
				count_neg = (double) neg_map.get(word) + smoothing_val;
			} catch(NullPointerException e){
				
				count_neg = smoothing_val;
			}
			
			
			try{
			this.pmi =  ((count_pos * (hits_negative + smoothing_val))/(count_neg * (hits_positive + smoothing_val)));
			} catch(Exception e){
				System.out.println("count_pos: " + count_pos +  " hits_negative:" + hits_negative + " count_neg: " + count_neg +   " hits_positive: " + count_neg);
			}
			
			// Put the logarithm of pmi value in SemanticOrientation Map
			soMap.put(word, Math.log(this.pmi));
		    
			//System.out.println(word + "/" +  count_pos + "/" + count_neg + "/" + Math.log(this.pmi));
		} 
		
		return soMap;
	}
	
	/*
	 *  get positve and negative words; performs a bit of further preprocessing on top of given pre processed text
	 *    
	 *    returns a map with positive and negative words based on neighborhood function i.e near to postive words is 
	 *    positive words and near to negative words is negative words
	 */
	
	public Map<String, Map<String, Integer>> getOpinionWords(List<String> docs, List<String> stopWords){
		for (String doc : docs){
			
			List<String> split = Arrays.asList(doc.split(" "));
			Integer i = 0;
			for (String str : split){
				
				// scan words around positive lexicon and add to postive words dictionary with its frequency count.
				for (String find : find1){
				if (str.contains(find)) {		
					
					// Scan left and right in window size of the current word; 
					for (Integer j=i-windowSize; j<=i+windowSize; j++){
						try {
							
						String key = split.get(j);
						
						//key = key.replaceAll("[^a-zA-Z]+","");
						key = key.trim();
						

						// Remove strings with length 2; Most of these are special characters.
						if(key.trim().length() <= 2){
							continue;
						}
						
						// Don't track stop words in opinion dictionary
						if (stopWords.contains(key)){
							continue;
						}
						
						
						Integer count = positive_words.get(key);
						if(count == null){
							count = 0;
						}
						// Add the scanned word in positive_words map as we search around positive lexicon
						positive_words.put(key, count + 1);
						} catch(IndexOutOfBoundsException e){
							//e.printStackTrace();

						}
					}
				}
				}
				for (String find : find2){

				// scan words around negative lexicon and add to negative words dictionary with its frequency count.
				if (str.contains(find)) {
					for (Integer j=i-windowSize; j<=i+windowSize; j++){
						try {
						String key = split.get(j);
						
						//key = key.replaceAll("[^a-zA-Z]+","");
						
						key = key.trim();

//						if(key.contains(find)){
//							key = find;
//						}

						// Remove strings with length 2; Most of these are special characters.
						if(key.trim().length() <= 2){
							continue;
						}
						
						// Don't track stop words in opinion dictionary
						if (stopWords.contains(key)){
							continue;
						}
						
						Integer count = negative_words.get(key);
						if(count == null){
							count = 0;
						}
						
						// Add the scanned word in positive_words map as we search around negative lexicon
						negative_words.put(key, count + 1);
						
						} catch(IndexOutOfBoundsException e){
							//e.printStackTrace();
						}
					}
				}
				}
				i = i+1;
			}		
		}
		
		Map<String, Map<String, Integer>> words = new HashMap<String, Map<String, Integer>>();
		words.put("positive_words", positive_words);
		words.put("negative_words", negative_words);

		return words;
	}
	
}
