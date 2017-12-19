package Tokenization;
import java.util.HashSet;
import java.util.StringTokenizer;

import IntLabML.Utility;

public class EngSegmentation {

	public EngSegmentation() {
	}

	public HashSet stopwordSegment(String text) {
		HashSet docHS = new HashSet();
		StringTokenizer st = new StringTokenizer(text); // StringTokenizer
		String word;

		while (st.hasMoreTokens()) {
			word = st.nextToken(); 
			word = word.toLowerCase();
			if ((Utility.dicen.search(word)) && (!Utility.stopWordHSEN.contains(word)) && (word.length()>1))
			{
				docHS.add(word);
			}
		}
		return docHS;
	}
	
	public HashSet segment(String text) {
		HashSet docHS = new HashSet();
		StringTokenizer st = new StringTokenizer(text); // StringTokenizer
		String word;

		while (st.hasMoreTokens()) {
			word = st.nextToken(); 
			word = word.toLowerCase();
			if ((Utility.dicen.search(word)) && (word.length()>1))
			{
				docHS.add(word);
			}
		}
		return docHS;
	}
	
}