import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import helper.FileIOOperations;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

/*
 * I've used OpenNLP Tokeniser to preprocess this file as the given file (sample.preprocessed.txt) was also 
 * processed using this tool. 
 * 
 * Please make sure you rename num_files and output_file_name variables as documented below
 * 
 */


public class DocumentPreprocessor {
	
	public static List<String> file_content;
	
	// Configurable variable to control the number of files/reviews 
	// Use value 2000 for small file sample.experiments.txt and 50000 for sample.large.experiments.txt
	private static Integer num_files = 50000;
	
	private static String output_file_name = "sample.large.experiments.txt";


	public static void main(String[] args) throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub
		
		// Open the Tokeniser model trained on OpenNLP training data (http://opennlp.sourceforge.net/models-1.5/)
		InputStream is = new FileInputStream("opennlp-models/en-token.bin");
		
		//Create TokeniserModel object from input read model
		TokenizerModel model = new TokenizerModel(is);
		
		//Create Tokeniser object from Tokenisermodel
		Tokenizer tokenizer = new TokenizerME(model);
		
		// Path to read files
		File folder = new File("data/unsup");
		File[] listOfFiles = folder.listFiles();
		
		
		
		FileIOOperations ps = new FileIOOperations();
		FileIOOperations ps1 = new FileIOOperations();
		
		
		List<String> docs = new ArrayList<String>();
		
		// Open n files; Preprocess them and write to a file (Same as the given preprocessed file)
	    for (int i = 0; i < num_files; i++) {
	      if (listOfFiles[i].isFile()) {
	        
	        try {
	        	file_content = ps.readFile("unsup/" + listOfFiles[i].getName());
	        		        	
	        	String tokens[] = tokenizer.tokenize(file_content.toArray()[0].toString().toLowerCase());
	        	
	        	// create a string from Tokens
	        	System.out.println("Processed: " + "unsup/" + listOfFiles[i].getName());
	    		docs.add(StringUtils.join(tokens, " "));
	    		
			} catch (IOException e) {
				e.printStackTrace();
			}    
	        
	      } else if (listOfFiles[i].isDirectory()) {
	        System.out.println("Directory " + listOfFiles[i].getName());
	      }
	    }
	    
		is.close();

	    // Write logic to add the list elements to a file (1 line per record)
		// The output file will now be a processed document that can be used for experiments in main project file
	    try {
			ps1.writeFile(docs,  output_file_name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    System.out.println("File " + output_file_name + " writing done");
		    
		    
	}

}
