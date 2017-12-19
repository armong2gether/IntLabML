package IntLabML;
import java.awt.Desktop;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;

public class Utility {
	
	public static String algorithm;
	
	public static String language;
	
	public static String os;
	
	public static String vocabTH = "data/thai_vocab";
	
	public static String classLabel[];
	
	public static int numdoc;
	public static int numClass;
	public static double PrClass[];
	public static String classes;
	
	public static String dirPath;
	
	public static String listFile [][];
	public static String listFileUsage [];
	public static String listFileTS [][];
	
	//dictionary
	public static HashMap<String,double[]> dicPriority;
	public static _dictionary dicen;
	public static _dictionary dicth;
	public static HashSet stopWordHSEN;
	public static HashSet stopWordHSTH;
	
	// BOW
	public static HashMap vocabHM;
	
	/////////////////// Open WebSite ////////////////////////////
	public static void openWebpage(String urlString) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
