package Weighting;

import java.util.HashSet;
import java.util.Iterator;

import IntLabML.Utility;
import Tokenization.EngSegmentation;
import Tokenization.ThaiSegmentation;

public class TF_IDF {
	
	static ThaiSegmentation wordcuttingTH = new ThaiSegmentation();
	static EngSegmentation wordcuttingEN = new EngSegmentation();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	// parse for trainning
	public static double[] parseTFIDF(String strDoc) {

		HashSet docHS = null;
		double[] docVSM = new double[Utility.vocabHM.size()];
		for (int i = 0; i < docVSM.length; i++)
			docVSM[i] = 0.0;
		int numofDoc = Utility.numdoc * Utility.numClass;

		if (Utility.language.equalsIgnoreCase("th")) {
			docHS = wordcuttingTH.segment(strDoc);
		} else {
			docHS = wordcuttingEN.segment(strDoc);
		}

		String word;

		Iterator i = docHS.iterator();
		Integer rows[] = new Integer[2];
		while (i.hasNext()) {
			word = (String) i.next();
			word = word.toLowerCase();
			rows = (Integer[]) Utility.vocabHM.get(word);
			// Check word with out BOW
			if (rows != null) {
				int position = rows[0].intValue();
				int df = rows[1].intValue();
				double idf = (Math.log((double) numofDoc / (double) df)) / Math.log(10.0);
				docVSM[position] += idf;
			} // end if( rows != null )
		}
		return docVSM;
	} // ens parse

}
