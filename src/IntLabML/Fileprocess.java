package IntLabML;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Fileprocess {

	/***************************************************
	 * This method for read File to HashSet. /
	 ***************************************************/

	public static HashSet FileHashSet(String fn) {
		HashSet hs = new HashSet();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fn)));
			String strLine = "";

			while ((strLine = br.readLine()) != null) {
				strLine = strLine.trim();
				hs.add(strLine);
			}
			br.close();
		} catch (Exception ioe) {
			System.out.println(ioe);
		}
		return (hs);
	}

	/***************************************************
	 * This method for read File. /
	 ***************************************************/
	public static StringBuffer readFile(String fileName) {
		StringBuffer strContent = new StringBuffer(100);
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String strLine = "";
			while ((strLine = br.readLine()) != null)
				strContent.append(strLine + " ");
			br.close();
		} catch (Exception ioe) {
			System.out.println(ioe);
		}
		return (strContent);
	}

	public static String openFile(JFrame f) {
		JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
		String pathfile = "";
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int returnVal = fc.showOpenDialog(f);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			pathfile = file.getAbsolutePath().toString();
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			pathfile = "";
		}
		return pathfile;
	}
}