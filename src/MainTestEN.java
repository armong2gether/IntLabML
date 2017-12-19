import java.util.HashSet;
import java.util.Set;

import IntLabML.Fileprocess;
import IntLabML.Utility;
import IntLabML._dictionary;
import Tokenization.EngSegmentation;

public class MainTestEN {

	public static void main(String[] args) {
		
		// Initialize resource
		Utility.stopWordHSEN = Fileprocess.FileHashSet("data/stopwordAndSpc_eng.txt");
		Utility.dicen = new _dictionary();
		Utility.dicen.getDictionary("data/dicEngScore.txt");
		
		// start
		EngSegmentation seg = new EngSegmentation();
		String testW = "MasterChef is a competitive cooking show television format created by Franc Roddam, which originated with the UK version in July 1990";
		System.out.println("Dict loaded : "+Utility.dicen.dic);
		
		HashSet wordCut = seg.segment(testW);
		System.out.println("\nsegmented : "+wordCut);
		System.out.println("Word count : "+wordCut.size());

		HashSet stopwordWordCut = seg.stopwordSegment(testW);
		System.out.println("\nstopword segmented : "+stopwordWordCut);
		System.out.println("Word count : "+stopwordWordCut.size());
		
		Set<Integer> complement = new HashSet<Integer>();
		complement.addAll(wordCut);
		complement.removeAll(stopwordWordCut);
		System.out.println("stop word is : "+complement);
	}

}
