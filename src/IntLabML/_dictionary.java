package IntLabML;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashSet;

public class _dictionary {

	public HashSet dic;

	public _dictionary() {
		dic = new HashSet();
	}

	public void getDictionary(String fullPathName) {
		try {
			int sum = 0;
			LineNumberReader lnr = new LineNumberReader(new FileReader(new File(fullPathName)));
			String line = lnr.readLine();
			String strArray;
			while (line != null) {
				double[] prio = new double[2];
				strArray = (line.split(" "))[0].trim().toLowerCase();
				prio[0] = Double.parseDouble(line.split(" ")[1]);
				prio[1] = Double.parseDouble(line.split(" ")[2]);
				if (!dic.contains(strArray)) {
//					Utility.dicPriority.put(strArray,prio);
					add(strArray);
				}
				line = lnr.readLine();
			}
			lnr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // getDictionary

	public boolean search(String str) {
		return dic.contains(str);
	}

	public void add(String str) {
		dic.add(str);
	}

	public void del(String str) {
		dic.remove(str);
	}

}