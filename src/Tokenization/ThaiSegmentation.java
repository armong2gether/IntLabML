package Tokenization;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

import IntLabML.ResourceFileLoader;
import IntLabML.Utility;

public class ThaiSegmentation {
	private static HashSet firstVowelHS; // first Sara
	private static HashSet vowelHS; // sara
	private static HashSet tonalHS; // wannayook
	private static HashSet spacialHS; // special sign
	private static String Kaaran;
	private HashSet hsword;
	private static StringBuffer result;
	private static StringTokenizer st;

	public ThaiSegmentation() {
		String v[] = load_Vocab("v.txt");
		String t[] = load_Vocab("t.txt");
		String s[] = load_Vocab("s.txt");
		String k[] = load_Vocab("k.txt");
		Kaaran = k[0].substring(1, 2);

		vowelHS = new HashSet();
		for (int i = 0; i < 12; i++) {
			v[i] = v[i].substring(1, 2);
			vowelHS.add(v[i]);
		}

		firstVowelHS = new HashSet();
		for (int i = 12; i < 17; i++)
			firstVowelHS.add(v[i]);

		tonalHS = new HashSet();
		for (int i = 0; i < 4; i++) {
			t[i] = t[i].substring(1, 2);
			tonalHS.add(t[i]);
		}

		spacialHS = new HashSet();
		for (int i = 0; i < 29; i++)
			spacialHS.add(s[i]);
	}

	private int checkKaaran(String subStr, int offset) {
		int con = 0;
		int index = 0;
		int threshold = 3; // really start
		while ((con != threshold) && ((index = subStr.length() - (offset + 1)) != -1)) {
			String lastChar = subStr.substring(index, index + 1);
			if (lastChar.equals(Kaaran))
				threshold += 3;
			if (!vowelHS.contains(lastChar) && !tonalHS.contains(lastChar))
				con++;
			offset += 1;
		}
		return (offset);
	}

	private int checkVowelTonal(String lastChar1, String lastChar2, int offset) {
		if ((tonalHS.contains(lastChar1) && vowelHS.contains(lastChar2))
				|| (tonalHS.contains(lastChar2) && vowelHS.contains(lastChar1)))
			offset += 2;
		else
			offset += 1;
		return (offset);
	}

	private boolean haveSpaceBetween(String unkn) {
		String lastUnkn = unkn.substring(unkn.length() - 1);
		if (firstVowelHS.contains(lastUnkn))
			return (false);
		else
			return (true);
	}

	private String longestMatching(String str) {

		StringBuffer result = new StringBuffer();
		String unknown = "";
		boolean foundUnknowBefore = false;

		while (!str.equals("")) {
			boolean found = false;
			int offset = 0;
			int endCursor = str.length();

			boolean findword;
			while ((endCursor != 0) && (!found)) {
				String subStr = str.substring(0, endCursor);
				subStr = subStr.toLowerCase();
				findword = Utility.dicth.search(subStr);

				if (findword) {
					if (foundUnknowBefore) {
						if (haveSpaceBetween(unknown)) {
							result.append(unknown + " / ");
						} else {
							result.append(unknown);
						}
					} // if (foundUnknowBefore)

					result.append(subStr + " / ");
					unknown = "";
					foundUnknowBefore = false;
					found = true;
				} else { 
					String lastChar1 = "";
					String lastChar2 = "";

					lastChar1 = str.substring(endCursor - 1, endCursor);
					if (endCursor >= 2) {
						lastChar2 = str.substring(endCursor - 2, endCursor - 1);
					}

					offset = 1;
					if (vowelHS.contains(lastChar1)
							|| tonalHS.contains(lastChar1)) {
						offset = checkVowelTonal(lastChar1, lastChar2, offset);
					} else if (lastChar1.equals(Kaaran)) {
						offset = checkKaaran(subStr, offset);
					}

					if (offset > endCursor) {
						offset = endCursor;
					}

					endCursor -= offset;
				} // if (vocabHS.contains(subStr))
			} // while ((endCursor!=0)&&(!found))

			if ((!found) && (!str.equals(""))) {

				String firstChar = str.substring(0, offset);

				if (spacialHS.contains(firstChar)) {
					unknown += (" / " + firstChar + " / ");
				} else {
					unknown += firstChar;
				}

				foundUnknowBefore = true;
				str = str.substring(offset, str.length());
			} else {
				str = str.substring(endCursor, str.length());
			} // if ( ( !found ) && ( !str.equals("") ) )

		} // while (!str.equals(""))

		if (!unknown.equals("")) {
			if (unknown.length() == 1) {
				String tmp = new String(result);
				tmp = tmp.trim();
				result = new StringBuffer(tmp);
			}

			result.append(unknown + " / ");
		}

		return (new String(result));

	} // public String longestMatching(String str)

	private String doBackTracking(String str) {
		String result = str;
		StringTokenizer st = new StringTokenizer(str, " / ");
		int cost = st.countTokens();

		String unit[] = new String[cost];
		int indextoken = 0;
		while (st.hasMoreTokens()) {
			unit[indextoken] = st.nextToken();
			indextoken++;
		} // while

		for (int i = 0; i < cost; i++) {
			String tmp = unit[i];
			if (tmp.length() > 2) {
				String lastChar = tmp.substring(tmp.length() - 1);
				String subResult = longestMatching(tmp.substring(0,
						tmp.length() - 1));
				st = new StringTokenizer(subResult, " / ");
				String newUnit = st.nextToken();
				StringBuffer merge = new StringBuffer();
				while (st.hasMoreTokens()) {
					merge.append(st.nextToken());
				} // while (st.hasMoreTokens())

				merge.append(lastChar);
				for (int j = i + 1; j < cost; j++) {
					merge.append(unit[j]);

				}

				subResult = longestMatching((new String(merge)).trim());
				newUnit += (" / " + subResult);
				merge = new StringBuffer();

				for (int j = 0; j < i; j++) {
					merge.append(unit[j] + " / ");
				}
				merge.append(newUnit);
				st = new StringTokenizer(new String(merge), " / ");
				if (st.countTokens() < cost) {
					result = new String(merge);
				}

			} // if (tmp.length()>2)
		} // for (int i=0;i<cost ;i++ )
		return (result);
	} // public String doBackTracking(String str)

	/***********************************************************/
	public HashSet segment(String text) {
		hsword = new HashSet();
		result = new StringBuffer();
		st = new StringTokenizer(text);
		while (st.hasMoreTokens()) {
			String tmp = st.nextToken();
			String wordLog = longestMatching(tmp);
			String word = doBackTracking(wordLog);
			result.append(word);
		}

		st = new StringTokenizer(new String(result), " / ");
		while (st.hasMoreTokens()) {
			String tstr = st.nextToken();
			tstr = tstr.trim();
			if ((Utility.dicth.search(tstr)) && (!Utility.stopWordHSTH.contains(tstr)) && (tstr.length() > 1)) {
				hsword.add(tstr);
			}
		}
		return hsword;

	} // public String segment(String text)

	/***********************************************/
	private String[] load_Vocab(String file) {
		String[] str_arr = null;
		try {
			BufferedReader charlie = null;
//			charlie = new BufferedReader(ResourceFileLoader.readFromJARFile(Utility.vocabTH+"/"+ file));
			
			//charlie = new BufferedReader(new FileReader(new File(Utility.vocabTH+"/"+ file)));
			charlie = new BufferedReader(ResourceFileLoader.readFromJARFile("../thai_vocab/"+ file));
			
			str_arr = new String[(new Integer(charlie.readLine())).intValue()];
			for (int i = 0; i < str_arr.length; i++) {
				str_arr[i] = charlie.readLine();
			}
			charlie.close();
		} // End try
		catch (IOException e) {
			System.out.println("Error -- " + e.toString());
		} // End catch

		return str_arr;
	}

}