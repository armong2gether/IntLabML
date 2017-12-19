package Predictions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import IntLabML.Fileprocess;
import IntLabML.ResourceFileLoader;
import IntLabML.Utility;
import Weighting.TF_IDF;

public class NaiveBayes {

	private static boolean isTrain = false;
	private static boolean isSetParam = false;

	public static int TotalClass;
	public int TotalWord;
	public int totalLabeledDoc;
	public double PrClass[];
	public double PrWordGivenClass[][];

	private double docVSM[];

	public NaiveBayes() {
		this.setParam();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	void setParam() {
		if (Utility.vocabHM.size() < 1 || Utility.vocabHM == null) {
			System.out.println("error vocab in HashMap : null or missing");
			return;
		}

		TotalClass = Utility.numClass;
		TotalWord = Utility.vocabHM.size();
		totalLabeledDoc = 0;
		PrClass = new double[TotalClass];
		PrWordGivenClass = new double[TotalClass][];
		for (int i = 0; i < TotalClass; i++)
			PrWordGivenClass[i] = new double[TotalWord];

		isSetParam = true;
	}

	// trainning by naivebayes
	public void naiveTrain(boolean isLaplace) {

		for (int id = 0; id < Utility.listFile.length; id++) {
			int numTrainingDocInClass = 0;
			for (int isd = 0; isd < Utility.listFile[id].length; isd++) {
				StringBuffer strDoc = Fileprocess.readFile(Utility.listFile[id][isd]);

				docVSM = TF_IDF.parseTFIDF(new String(strDoc)); // ***********
																// weight with
																// TF-IDF
																// ************

				// Simply count word given a class
				for (int j = 0; j < TotalWord; j++)
					PrWordGivenClass[id][j] += docVSM[j];
				numTrainingDocInClass++;
				totalLabeledDoc++;
			}
			// set the number of training doc. in each class
			PrClass[id] = (double) numTrainingDocInClass;
		}

		// PrClass + PrWordGivenClass
		for (int nClass = 0; nClass < TotalClass; nClass++) {

			if (isLaplace) {
				// Laplace Smoothing
				PrClass[nClass] = (1 + PrClass[nClass])
						/ ((double) TotalClass + (double) (Utility.numClass * Utility.numdoc));
			} else {
				PrClass[nClass] = (PrClass[nClass]) / ((double) (Utility.numClass * Utility.numdoc));
			}

			// Count total word in a class
			double totalWordInClass = 0;
			for (int nWord = 0; nWord < TotalWord; nWord++)
				totalWordInClass += PrWordGivenClass[nClass][nWord];

			for (int nWord = 0; nWord < TotalWord; nWord++) {
				if (isLaplace) {
					// Laplace Smoothing
					PrWordGivenClass[nClass][nWord] = (1 + PrWordGivenClass[nClass][nWord])
							/ (TotalWord + totalWordInClass);
				} else {
					PrWordGivenClass[nClass][nWord] = (PrWordGivenClass[nClass][nWord]) / (totalWordInClass);
				}
			}

		} // end for( int nClass = 0;

		isTrain = true;

	}

	public int naiveUsage(String strDoc) {

		if (Utility.vocabHM.size() < 1 || Utility.vocabHM == null) {
			System.out.println("error vocab in HashMap : null or missing");
			return -1;
		}

		double prob[] = new double[Utility.numClass];
		double sumPrioN = 0.0;
		double sumPrioP = 0.0;
		
		docVSM = TF_IDF.parseTFIDF(new String(strDoc));
		
		for (int i = 0; i < Utility.numClass; i++) {
			prob[i] = 0.0;

			/* Naive calculation by using the independence assumption */
			for (int j = 0; j < TotalWord; j++) {
				prob[i] += (docVSM[j] * PrWordGivenClass[i][j]);
			}
//			prob[i] += PrClass[i];
		}
		double summm = 0.0;
		for(int i=0;i<prob.length;i++){
//			System.out.print(prob[i]+"  ");
			summm+=prob[i];
		}
		double[] score1 = new double[Utility.numClass];
		System.out.println();
		for (int i = 0; i < Utility.numClass; i++) {
			score1[i] = prob[i]/summm;
			System.out.print(Utility.classLabel[i]+" : "+score1[i]+"  ");
		}
		// System.out.printf("\n%.0f",(score1[0]+score1[1]));
		/* Find maximum a posteriori probability */
		int product = 0;
		double sum = 0.0;
		double maxProb = prob[0];
		for (int i = 0; i < Utility.numClass; i++) {
			sum+=prob[i];
			if (prob[i] > maxProb) {
				maxProb = prob[i];
				product = i;
			}
		}
		double[] score = new double[Utility.numClass];
		for (int i = 0; i < Utility.numClass; i++) {
			score[i] = prob[i]/sum;
		}
		
		String ss = "";
		if(TotalClass > 2){
			if(product == 0) ss = "Bad";
		    if(product == 1) ss = "Normal";
		    if(product == 2) ss = "Good";
		}else{
			if(product == 0) ss = "Good";
		    if(product == 1) ss = "Bad";
		}
		
		return (product);
	}

	public void saveModel() {
		if (!isTrain)
			return;
		try {
			BufferedWriter charlie = null;
			if (Utility.classes.equals("twoClass")) {
				if (Utility.language.equals("th")) {
					charlie = new BufferedWriter(new FileWriter(new File("NaiveTH.model")));
				} else {
					charlie = new BufferedWriter(new FileWriter(new File("NaiveEng.model")));
				}
			} else if (Utility.classes.equals("threeClass")) {
				if (Utility.language.equals("th")) {
					charlie = new BufferedWriter(new FileWriter(new File("NaiveTH_3rating.model")));
				} else {
					charlie = new BufferedWriter(new FileWriter(new File("NaiveEng_3rating.model")));
				}
			}
			// class
			charlie.write((new Integer(TotalClass)).toString());
			charlie.newLine();
			// numtrain
			charlie.write((new Integer(Utility.numClass * Utility.numdoc)).toString());
			charlie.newLine();
			charlie.write((new Integer(TotalWord)).toString());
			charlie.newLine();

			charlie.write((new Integer(docVSM.length)).toString());
			charlie.newLine();
			for (int i = 0; i < docVSM.length; i++) {
				charlie.write((new Double(docVSM[i])).toString());
				charlie.newLine();
			}
			charlie.write((new Integer(PrClass.length)).toString());
			charlie.newLine();

			Utility.PrClass = new double[PrClass.length];
			for (int i = 0; i < PrClass.length; i++) {
				charlie.write((new Double(PrClass[i])).toString());
				charlie.newLine();
			}

			charlie.write((new Integer(PrWordGivenClass.length)).toString());
			charlie.newLine();
			for (int i = 0; i < PrWordGivenClass.length; i++) {
				charlie.write((new Integer(PrWordGivenClass[i].length)).toString());
				charlie.newLine();
				for (int j = 0; j < PrWordGivenClass[i].length; j++) {
					charlie.write((new Double(PrWordGivenClass[i][j])).toString());
					charlie.newLine();
				}
			}

			HashMap temp = Utility.vocabHM;
			Set setKeys = temp.keySet();
			Iterator i = setKeys.iterator();
			charlie.write((new Integer(temp.size())).toString());
			charlie.newLine();
			while (i.hasNext()) {
				String s = (String) i.next();
				Integer rows[] = new Integer[2];
				rows = (Integer[]) temp.get(s);
				charlie.write(s + " " + rows[0].toString() + " " + rows[1].toString());
				charlie.newLine();
			}

			charlie.close();

		} // End try
		catch (IOException e) {
			System.out.println("Error -- " + e.toString());
		} // End catch
	}

	// load model TF-IDF
	private void loadModel() {
		
		if (Utility.vocabHM != null) {
			System.out.println("error vocab in HashMap : model are loaded ..");
			return;
		}
		
		try {
			BufferedReader charlie = null;
			if (Utility.classes.equals("twoClass")) {
				if (Utility.language.equals("th")) {
					charlie = new BufferedReader(ResourceFileLoader.readFromJARFile("NaiveTH.model"));
				} else if (Utility.language.equals("eng")) {
					charlie = new BufferedReader(ResourceFileLoader.readFromJARFile("NaiveEng.model"));
				}
			} else if (Utility.classes.equals("threeClass")) {
				if (Utility.language.equals("th")) {
					charlie = new BufferedReader(ResourceFileLoader.readFromJARFile("NaiveTH_3rating.model"));
				} else if (Utility.language.equals("eng")) {
					charlie = new BufferedReader(ResourceFileLoader.readFromJARFile("NaiveEng_3rating.model"));
				}
			}

			Utility.numClass = (new Integer(charlie.readLine())).intValue();
			Utility.numdoc = (new Integer(charlie.readLine())).intValue();
			Utility.numClass = TotalClass;
			TotalWord = (new Integer(charlie.readLine())).intValue();
			docVSM = new double[(new Integer(charlie.readLine())).intValue()];

			for (int i = 0; i < docVSM.length; i++) {
				docVSM[i] = (new Double(charlie.readLine())).doubleValue();
			}
			PrClass = new double[(new Integer(charlie.readLine())).intValue()];
			for (int i = 0; i < PrClass.length; i++) {
				PrClass[i] = (new Double(charlie.readLine())).doubleValue();
			}

			PrWordGivenClass = new double[(new Integer(charlie.readLine())).intValue()][];
			for (int i = 0; i < PrWordGivenClass.length; i++) {
				PrWordGivenClass[i] = new double[(new Integer(charlie.readLine())).intValue()];
				for (int j = 0; j < PrWordGivenClass[i].length; j++) {
					PrWordGivenClass[i][j] = (new Double(charlie.readLine())).doubleValue();
				}
			}

			int numword = (new Integer(charlie.readLine())).intValue();
			for (int i = 0; i < numword; i++) {
				StringTokenizer st = new StringTokenizer(charlie.readLine());
				String w = st.nextToken();
				Integer rows[] = new Integer[2];
				rows[0] = new Integer(st.nextToken()); /* word position */
				rows[1] = new Integer(st.nextToken()); /* df */
				Utility.vocabHM.put(w, rows);
			}
			charlie.close();
		} // End try
		catch (IOException e) {
			System.out.println("Error -- " + e.toString());
		} // End catch
	}

}
