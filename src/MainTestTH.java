import java.util.HashSet;

import IntLabML.Fileprocess;
import IntLabML.Utility;
import IntLabML._dictionary;
import Tokenization.ThaiSegmentation;
public class MainTestTH {

	public static void main(String[] args) {
		
		System.out.println(System.getProperty("os.name")+" : "+System.getProperty("os.version"));
		System.out.println();
		
		// Initialize resource
		Utility.stopWordHSTH = Fileprocess.FileHashSet("data/stopwordAndSpc_th.txt");
		Utility.dicth = new _dictionary();
		Utility.dicth.getDictionary("data/SentimentPolarity.txt");
		
		// start
		ThaiSegmentation seg = new ThaiSegmentation();
		String testW = "ให้กรอกข้อมูลในระบบสำรวจภาวะการมีงานทำของบัณฑิต ให้แล้วเสร็จก่อนวันพระราชทานปริญญาบัตร";
		System.out.println("Dict loaded : "+Utility.dicth.dic);
		
		HashSet wordCut = seg.segment(testW);
		System.out.println("\nsegmented : "+wordCut);
		System.out.println("Word count : "+wordCut.size());
		
	}

}
