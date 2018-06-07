/*
 * This is the main Project file. It prints out the Results for both Amazon reviews and Large Movies Reviews dataset
 * 
 *  
 */


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Package contains polarity calcuation
import polarity.*;

//Package contains Evaluation code
import evaluation.*;

//Helper class for file operations.
import helper.FileIOOperations;

public class ProjectMain {
	
	// Opinion lexicon file names;  Given by Hu and Liu
	public static String positiveFileName = "positive-words.txt";
	public static String negativeFileName = "negative-words.txt";
	
	// Data files to be evaluated; 
	
	// Amazon Review files
	public static String corpusFileName_small = "sample.preprocessed.txt";
	public static String corpusFileName_large = "sample.large.preprocessed.txt";

	// LMR dataset file; created using DocumentPreprocessor.java
	public static String corpusExperimentFileName_small = "sample.experiments.txt";
	public static String corpusExperimentFileName_large = "sample.large.experiments.txt";
	
	public static String stopwordsFileName = "stop-word-list.txt";
	
	private static Map<String, Integer> positive_words = new HashMap<String, Integer>();
	private static Map<String, Integer> negative_words = new HashMap<String, Integer>();
	private static List<String> positiveOpinionWords;
	private static List<String> negativeOpinionWords;
	private static List<String> stopWords;
	
	// default window size
	private static Integer windowSize = 5;
	
	// default positive lexicon seed word
	public static List<String> find1 = Arrays.asList("excellent");
	
	//  positive lexicon seed word list
	public static List<String> find1_morewords = Arrays.asList(
			"excellent", "good", "terrific", "great", "fantastic", "love", "amazing"
		    );
	
	// default negative lexicon seed word
	public static List<String> find2 = Arrays.asList("poor");

	//  negative lexicon seed word list
	public static List<String> find2_morewords = Arrays.asList(
			"poor" , "bad", "terrible", "horrible", "useless", "sad", "aweful"
			);
	
	public static void main(String[] args){
		// TODO Auto-generated method stubs
		
		FileIOOperations ps = new FileIOOperations();
		
		// Read the stopwordsfile and add in a list
		try {
			stopWords = ps.readFile(stopwordsFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Read Positive opinion words
		try {
			positiveOpinionWords = ps.readFile(positiveFileName);
			//System.out.println(positiveOpinionWords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Read Negative opinion words
		try {
			negativeOpinionWords = ps.readFile(negativeFileName);
			//System.out.println(negativeOpinionWords);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Reports results for Amazon reviews small version with default seed word

		System.out.println("Print Results for Sample Preprocessed file (Amazon Reviews) given by Christian Schieble (small)");
		// Only Excellent and Poor
		processData(ps, find1, find2, corpusFileName_small);
		
		// Reports results for Amazon reviews large version with seed word list		
		// Results with more positive and negative words
		System.out.println("\nPrint Results for Sample Preprocessed file (Amazon Reviews) given by Christian Schieble (small; Opinion Lexicon)");

		processData(ps, find1_morewords, find2_morewords, corpusFileName_small);
		
		// Reports results for Amazon reviews large version with default seed word
		System.out.println("\nPrint Results for Sample Preprocessed (Amazon Reviews) file given by Christian Schieble (large)");
		processData(ps, find1, find2, corpusFileName_large);
		
		// Reports results for Amazon reviews large version with seed word list
		System.out.println("\nPrint Results for Sample Preprocessed file (Amazon Reviews) given by Christian Schieble (large; Opinion Lexicon))");
		processData(ps, find1_morewords, find2_morewords, corpusFileName_large);
		
		// Reports results for Large Movies reviews with default seed words
		System.out.println("\nPrint Results for Sample Preprocessed file (Large Moveies Reviews) for Experiments (small)");
		// Only Excellent and Poor
		processData(ps, find1, find2, corpusExperimentFileName_small);
		
		// Reports results for Large Movies reviews with seed words list
		System.out.println("\nPrint Results for Sample Preprocessed file (Large Moveies Reviews) for Experiments (small; Opinion Lexicon )");
		// Results with more positive and negative words
		processData(ps, find1_morewords, find2_morewords, corpusExperimentFileName_small);
		
		// Reports results for Large Movies reviews large version with default seed word

		System.out.println("\nPrint Results for Sample Preprocessed file (Large Moveies Reviews) for Experiments (large)");
		processData(ps, find1, find2, corpusExperimentFileName_large);
		
		// Reports results for Large Movies reviews large version with seed words list

		System.out.println("\nPrint Results for Sample Preprocessed file (Large Moveies Reviews) for Experiments (large; Opinion Lexicon)");
		processData(ps, find1_morewords, find2_morewords, corpusExperimentFileName_large);
		
	}
	/*
	 * find1 here is positive lexicon seed word
	 * find2 is negative lexicon
	 * corpusFileName is the file to read to get positve and negative words
	 */
	public static void processData(FileIOOperations ps, List<String> find1, List<String> find2, String corpusFileName){
				
				// Object of Sentiment Polarity Class;
				// This objects initialised the parameters
				SentimentPolarity polarity = new SentimentPolarity(find1, find2, windowSize);
				
				
				try {
					
					// This flag marks for the check if we read the document corpus or opinion dictionary
					// True means we now read document corpus whose words we're extracting to 
					// evaluate semantic orientation score
					ps.setDocsFlag(true);
					List<String> docs = ps.readFile(corpusFileName);
					
					// Get the list of positive of positive and negative words based on neighborhood 
					// words in the documents
					Map<String, Map<String, Integer>> words = polarity.getOpinionWords(docs, stopWords); 
					positive_words  = words.get("positive_words");
					negative_words  = words.get("negative_words");
					
					// Calculates SO score for all words
					Map<String, Double>  soMap = polarity.calcSOScore(positive_words, negative_words );
						
					// EvaluationVO initialisation for Evaluation object
					EvaluationVO confMatrixPos = new EvaluationVO();
					EvaluationVO confMatrixNeg = new EvaluationVO();
					
					//Loop for all words in positive opinion words
					for (String word : positiveOpinionWords){
						
							
						 if (soMap.get(word) == null){
							 continue;
						 }
						// Check if SemanticOrientation Map has a positive value
						 else if(soMap.get(word) > 0 ){
							 // This is a TP for Pos class
							confMatrixPos.setTp(confMatrixPos.getTp() + 1);
						
						// Check if SemanticOrientation Map has a negative value
						} else if (soMap.get(word) < 0){
							
							// This is a FN for positive class
							confMatrixPos.setFn(confMatrixPos.getFn() + 1);
							// This is a FP for negative class
							confMatrixNeg.setFp(confMatrixNeg.getFp() + 1);
						} 
					}
					
					//Loop for all words in negative opinion words
					for (String word : negativeOpinionWords){
						if (soMap.get(word) == null){
							 continue;
						 }
						 else if(soMap.get(word) < 0){
							 //This is a TP for Neg class
							confMatrixNeg.setTp(confMatrixNeg.getTp() + 1);
						} else if (soMap.get(word) > 0){
							//System.out.println(word + soMap.get(word));

							// This is a FN for Negative class
							confMatrixNeg.setFn(confMatrixNeg.getFn() + 1);
							
							// This is a FP for Positive class
							confMatrixPos.setFp(confMatrixPos.getFp() + 1);
						}
					}
					
					// Calls methods from Evaluation object to calculate evaluation metrics for positive class
					confMatrixPos.setPrecision(confMatrixPos.calcPrecision());
					confMatrixPos.setRecall(confMatrixPos.calcRecall());
					confMatrixPos.setFscore(confMatrixPos.calcFScore());

					// Calls methods from Evaluation object to calculate evaluation metrics for Negative class					
					confMatrixNeg.setPrecision(confMatrixNeg.calcPrecision());
					confMatrixNeg.setRecall(confMatrixNeg.calcRecall());
					confMatrixNeg.setFscore(confMatrixNeg.calcFScore());

					// Print Evaluation objects
					System.out.println(confMatrixPos);
					System.out.println(confMatrixNeg);
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
}
