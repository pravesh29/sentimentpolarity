package evaluation;

/*
 * Evaluation metrics for the project
 */
public class EvaluationVO {
	
	private int tp ; 
	private int fp;
	private int tn ;
	private int fn ;
	private double recall;
	private double precision;
	private double fscore;
	
	public int getTp() {
		return tp;
	}
	public void setTp(int tp) {
		this.tp = tp;
	}
	public int getFp() {
		return fp;
	}
	public void setFp(int fp) {
		this.fp = fp;
	}
	public int getTn() {
		return tn;
	}
	public void setTn(int tn) {
		this.tn = tn;
	}
	public int getFn() {
		return fn;
	}
	public void setFn(int fn) {
		this.fn = fn;
	}
	public double getRecall() {
		return recall;
	}
	public void setRecall(double recall) {
		this.recall = recall;
	}
	public double getPrecision() {
		return precision;
	}
	public void setPrecision(double precision) {
		this.precision = precision;
	}
	public double getFscore() {
		return fscore;
	}
	public void setFscore(double fscore) {
		this.fscore = fscore;
	}
	
	// calculates precision
	public double calcPrecision(){
		return  (double)this.tp/(this.tp+this.fp);
	}
	
	// calculates Recall
	public double calcRecall(){
		return (double)this.tp/(this.tp+this.fn);
	}
	
	// calculates F-score
	public double calcFScore(){
		return 2 * this.precision * this.recall/ (this.precision + this.recall);
	}
	

	public static EvaluationVO getInstance(){
		
			EvaluationVO confMatrixDO = new EvaluationVO();
			confMatrixDO.setFn(0);
			confMatrixDO.setFp(0);
			confMatrixDO.setTn(0);
			confMatrixDO.setTp(0);
			
			return confMatrixDO;
					
		}
	
	// Prints the Evalutaion Object
	@Override
	public String toString() {
		return "EvaluationVO [tp=" + tp + ", fp=" + fp + ", fn=" + fn +  ", precision=" + precision + ", recall=" + recall
				+ ", fscore=" + fscore + "]";
	}
	

}
