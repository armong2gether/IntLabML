package IntLabML;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceFileLoader {
	static ResourceFileLoader res = new ResourceFileLoader();
	public static InputStreamReader readFromJARFile(String filename){
		try {
			InputStream is = res.getClass().getResourceAsStream(filename);
			InputStreamReader isr = new InputStreamReader(is);
			return isr;
		} catch (Exception e) {}
		return null;
	}
	
	public static InputStreamReader readFromJARFile(String filename,String encode){
		try {
			InputStream is = res.getClass().getResourceAsStream(filename);
			InputStreamReader isr = new InputStreamReader(is,encode);
			return isr;
		} catch (Exception e) {}
		return null;
	}
}
