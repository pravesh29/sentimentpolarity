This is the implementation of Turney's paper 2002 which calculates the polarity of words on the basis of Pointwise Mutual Information.



# Main Files
	

1. DocumentPreprocessor.java - This is used to process the experimental corpus. This file looks for unsup folder in data directory present in root folder. This reads all files from this folder preprocesses them and write it to a single file. Make sure to write the file names as sample.experiments.txt and sample.experiments.large.txt for small and large files respectively
	
2. ProjectMain.java - This is the main project file. It does all the processing work i.e reading all document corpus files and prints the Evaluation of opinion lexicon.
	

# Other files
	
1. EvalutionVO.java - It contains code for Evaluation module. Calculates precision, recall , F-score
	
2. FileIOOperation.java - Helper class for read and write file operations
	
3. SentimentPolarity.java - It contains code for getting positive and negative words based on neighbourhood functions and also for calculating the Semantic Orientation Score
	


# Data Directory

Please untar the data.tar file using this command
tar -xvf data.tar 



# opennlp-models
	
This folder contains the OpenNLP model for tokenizing text. I've used en-token.bin
	


# Project Startup
	
To execute the project just open the project in eclipse and execute Main File - ProjectMain.java 
	
The files for experiments are already existing; 
You can further generate them by reading instructions/comments in DocumentProcessor.java file
	
	
