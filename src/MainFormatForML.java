import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import IntLabML.Fileprocess;
import IntLabML.SetClassPath;
import IntLabML.Utility;
import IntLabML._dictionary;
import Predictions.NaiveBayes;
import Selection.InformationGain;
import Tokenization.EngSegmentation;
import Tokenization.ThaiSegmentation;
import Weighting.TF_IDF;

public class MainFormatForML {
	
	public HashSet[][] word_arr;
	public ArrayList<String> wordSet;
	
	ThaiSegmentation wordcuttingTH = new ThaiSegmentation();
	EngSegmentation wordcuttingEN = new EngSegmentation();
	
	public static void main(String[] args) {
		
		// ================= initial Parameter of main Program =======================
		Utility.dirPath = "C:\\Users\\imSlappy\\workspace\\FeelingAnalysis\\Documents\\2_group_eng";
		Utility.numdoc = 350;
		SetClassPath.read_path(Utility.dirPath);
		Utility.language = "en";
		
		Utility.stopWordHSEN = Fileprocess.FileHashSet("data/stopwordAndSpc_eng.txt");
		Utility.dicen = new _dictionary();
		Utility.dicen.getDictionary("data/dicEngScore.txt");
		
		Utility.vocabHM = new HashMap();
		// ================= completed initial main Program ==========================

		MainFormatForML ob = new MainFormatForML();
		
		// --------------------- Pre-process -----------------------
		ob.word_arr = new HashSet [Utility.listFile.length][];
		ob.wordSet = new ArrayList<String>();
		for (int i = 0; i < Utility.listFile.length; i++) {
			ob.word_arr[i]=new HashSet[Utility.listFile[i].length];
			for (int j = 0; j < Utility.listFile[i].length; j++) {
				StringBuffer strDoc = Fileprocess.readFile(Utility.listFile[i][j]);
				ob.word_arr[i][j]=ob.docTokenization(new String(strDoc));
			}
			ob.checkBound(3, 10000);
		}
		
		System.out.println("++ Total : "+Utility.vocabHM.size()+" words");
		System.out.println("++ "+Utility.vocabHM);
		
		// ---------------------- Selection ------------------------
//		InformationGain ig = new InformationGain(ob.word_arr.length*ob.word_arr[0].length , ob.wordSet,ob.word_arr);
//		ArrayList<String> ban_word = ig.featureSelection(0.0); // selected out with IG = 0
//		//ob.banFeature(ban_word);
//		System.out.println("ban list["+ban_word.size()+"] : "+ban_word);
//		
//		System.out.println("-- After "+Utility.vocabHM.size());
//		System.out.println("-- "+Utility.vocabHM);
		
		ob.setWordPosition();
		// ---------------------- Processing -----------------------
		NaiveBayes naive = new NaiveBayes();
		naive.naiveTrain(true);
		
		int result = naive.naiveUsage("after cold reset of my pc (crash of xp) the favorites of firefox are destroyed and all settings are standard again! Where are firefox-favorites stored and the settings ? how  to backup them rgularely?  All other software on my pc still works properly ! even INternetExplorer");
		System.out.println("\nResult : "+Utility.classLabel[result]);
	}
	
	
	
	private HashSet docTokenization(String strDoc) {

		HashSet docHS = new HashSet();
		if (Utility.language.equals("th")) {
			docHS = wordcuttingTH.segment(strDoc);
		} else {
			docHS = wordcuttingEN.segment(strDoc);
		}
		
		String word;
		Iterator i = docHS.iterator();
		while (i.hasNext()) {
			word = (String) i.next();
			Integer docFreq;
			Integer tmpDocFreq = (Integer) Utility.vocabHM.get(word);

			if (tmpDocFreq == null) {
				docFreq = new Integer(1);
			} else {
				docFreq = new Integer(tmpDocFreq.intValue() + 1);
			}
			Utility.vocabHM.put(word, docFreq);
			
			if(!wordSet.contains(word))
				wordSet.add(word); // distinct all word in dataSet
		}
		return docHS;
	}
	
	/***************************************************
	 * This method for check frequency bound.(< low && > upper) /
	 ***************************************************/
	private void checkBound(int lowerBound, int upperBound) {
		Set setKeys = Utility.vocabHM.keySet();
		Iterator i = setKeys.iterator();

		while (i.hasNext()) {
			String s = (String) i.next();
			Integer df = (Integer) Utility.vocabHM.get(s);

			if ((df.intValue() <= lowerBound) || (df.intValue() >= upperBound))
				i.remove();
		}
	}
	
	/***************************************************
	 * This method for ban feature when selection       /
	 ***************************************************/
	private static void banFeature(ArrayList t) {
		Set setKeys = Utility.vocabHM.keySet();
		Iterator i = setKeys.iterator();
		System.out.print("Word cut : [");
		int idx = 0;
		while (i.hasNext()) {
			String s = (String) i.next();
			Integer df = (Integer) Utility.vocabHM.get(s);

			if (t.contains(s)){
				System.out.print(s+", ");
				i.remove();
				idx++;
			}
		}
		System.out.println("]\n ::banned::"+idx+" feature.");
	}
	
	/***************************************************
	 * This method for set position of word /
	 ***************************************************/
	private static void setWordPosition() {
		Set setKeys = Utility.vocabHM.keySet();
		Iterator i = setKeys.iterator();
		int position = 0;
		while (i.hasNext()) {
			String w = (String) i.next();
			Integer rows[] = new Integer[2];
			rows[0] = new Integer(position); /* word position */
			rows[1] = (Integer) Utility.vocabHM.get(w); /* df */

			Utility.vocabHM.put(w, rows);
			position++;
		}
	}

}
