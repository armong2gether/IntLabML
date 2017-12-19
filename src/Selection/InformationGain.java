package Selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import IntLabML.Utility;

public class InformationGain {
	
	private static boolean isSetParam = false;
	static double dataSet;
	
	public static HashSet[][] wordArr;
	static int wording[][];
	public static ArrayList<String> wordSet;
	
	public InformationGain(double infoDataset,ArrayList<String> wSet, HashSet[][] wordar) {
		// TODO Auto-generated constructor stub
		this.setParam(infoDataset,wSet,wordar);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
	
	public static void setParam(double infoDataset,ArrayList<String> wSet, HashSet[][] wordar) {
		wordSet = wSet;
		dataSet = infoDataset;
		wordArr = wordar;
		wording = new int[wordArr.length*wordArr[0].length][wordSet.size()];
		isSetParam = true;
	}
	
	private void createArrayWording(){
		int start = 0;
		for (int i = 0; i < wording.length;) {
			if(i>=wordArr[0].length)
				start=1;
			for (int k = 0; k < wordArr[0].length; k++) {
				String word;
				Iterator index = wordArr[start][k].iterator();
				while (index.hasNext()) {
					word = (String)index.next();
					int indexof = wordSet.indexOf(word);
					wording[i][indexof] += 1;
				}
				i++;
			}
		}
	}
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *        return feature an under than threshold param           *
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public ArrayList<String> featureSelection(double threshold) {
		if (!isSetParam) return null;
		
		ArrayList<String> temp = new ArrayList<String>();
		double prob = (double) Utility.numClass / (double) dataSet;
		double infoDataSet = -(prob * (Math.log(prob) / Math.log(2))) - (prob * (Math.log(prob) / Math.log(2)));
		double infoAtt[] = new double[wording[0].length];
		for (int i = 0; i < infoAtt.length; i++) {
			double tmp[] = countW(i);
			double allw = tmp[0] + tmp[1];
			double c = Math.log(tmp[0] / wording[0].length) / Math.log(2);
			if (Double.isInfinite(c))
				c = 0.0;
			double e = Math.log(tmp[1] / wording[0].length) / Math.log(2);
			if (Double.isInfinite(e))
				e = 0.0;
			//System.out.println(" wd "+wording.length+"  "+dataSet+"  -- "+allw+" : "+c+" : "+e);
			infoAtt[i] = infoDataSet - (allw / wording[0].length * (-((tmp[0] / wording[0].length) * c)
					- ((tmp[1] / wording[0].length) * Math.log(tmp[1] / wording[0].length) / Math.log(2))));
			
			//System.out.println(infoDataSet+" - ("+allw+" / "+wording[0].length+" * (-(("+tmp[0]+" / "+wording[0].length+") * "+c+") - (("+tmp[1]+" / "+wording[0].length+") * Math.log("+tmp[1]+" / "+wording[0].length+") / Math.log("+2+"))))");
			
			if (infoAtt[i] == threshold || Double.isNaN(infoAtt[i])) {
				temp.add(wordSet.get(i));
			}

		}
		return temp;
	}
	
	// static 2 class **** NEED FIXS
	static double []countW(int index){
		double count[] = new double[(int)Utility.numClass];
		for(int i=0;i<wording.length;i++){
			if (wording[i][index]==1){
				if (i>=(dataSet/Utility.numClass))
					count[0]++;
				else
					count[1]++;
			}
		}
		return count;
	}

}
