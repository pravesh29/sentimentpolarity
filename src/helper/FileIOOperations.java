package helper;
/*
 * Helper class for FileIO operations
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class FileIOOperations {
	
	public String dataPath = "data/";
	private String filePath;
	Boolean docsFlag = false;

	// Sets docs flag
	public void setDocsFlag(Boolean docsFlag) {
		this.docsFlag = docsFlag;
	}
	
	/*
	 * read file function; configured to read corpus documents or opinion dictionary
	 * uses docsFlag to check if it reads document corpus or opinion dictionary
	 * 
	 */
	public List<String> readFile(String filePath) throws IOException{
		
		this.filePath = this.dataPath + filePath;
		
		
		List<String> words = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(this.filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		
		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null)   {
			
		  // Condition when reading corpus
			if (this.docsFlag == true) {
				//if (this.docsFlag == true && (strLine.contains("excellent") || strLine.contains("poor"))){
					
					// remove multiple occurrences of periods and spaces. 
					// These are generally typos and never intentionally
					strLine.replaceAll("( |. )+", " ");
					
					words.add(strLine);
				//}
				
			} 
			  // Condition when reading opinion dictionary; Ignore all other line
			else if( strLine.length() > 0 && !strLine.startsWith(";")){
				words.add(strLine);
		}
		}

		//Close the input stream
		br.close();
		return words;
		
	}
	
	/*
	 * Writes list of docs to a file given the file name
	 */
	
	public Boolean writeFile(List<String> docs, String filename) throws IOException{
		//this.filePath = this.dataPath + filePath;
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(this.dataPath + filename), "utf-8"))) {
			for (String doc : docs){
				writer.write(doc + "\n");
				//System.out.println(doc);
			}
		}
	
		return true;
	}
	
}
