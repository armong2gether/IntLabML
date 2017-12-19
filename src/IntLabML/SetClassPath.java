package IntLabML;

import java.io.File;
import java.util.Scanner;

public class SetClassPath {

	public static void read_path(String pathfile) {
		File dir = new File(pathfile);
		if (dir.isDirectory()) { // check directory
			String strFiles[] = load_subDirectory();
			System.out.println("class : " + strFiles.length+ "\ndoc per class : " + Utility.numdoc);
			if (strFiles.length != 0) { // check class
				Utility.numClass = strFiles.length;
				Utility.listFile = new String[strFiles.length][];
				for (int i = 0; i < Utility.numClass; i++) { // read class name
					File subdir = new File(pathfile + "/" + strFiles[i]);
					String strsubFiles[] = subdir.list(); // get List Files
					if (strsubFiles.length < Utility.numdoc) {// read file
																// in class
						System.out.println("Error : file in directory less than select file. : " + pathfile + "/"
								+ strFiles[i].toString());
					} else {
						Utility.listFile[i] = new String[Utility.numdoc];
						for (int j = 0; j < Utility.numdoc; j++) {
							Utility.listFile[i][j] = pathfile + "/" + subdir.getName() + "/" + strsubFiles[j];
						}
					} // end read file in class
				} // end read class
			} else {
				System.out.println("Error : Directory is empty.");
			}
		} else {
			System.out.println("Error : path file is not Directory.");
		}
	}

	public static void read_path_Test(String pathfile) {
		File dir = new File(pathfile);
		if (dir.isDirectory()) { // check directory
			String strFiles[] = load_subDirectory();
			if (strFiles.length != 0) { // check class
				Utility.numClass = strFiles.length;
				Utility.listFileTS = new String[strFiles.length][];
				for (int i = 0; i < Utility.numClass; i++) { // read class
																// name
					File subdir = new File(pathfile + "/" + strFiles[i]);
					String strsubFiles[] = subdir.list(); // get List Files
					if (strsubFiles.length < Utility.numdoc) {// read file
																// in class
						System.out.println("Error : file in directory less than select file. : " + pathfile + "/"
								+ strFiles[i].toString());
					} else {
						Utility.listFileTS[i] = new String[Utility.numdoc];
						for (int j = 0; j < Utility.numdoc; j++) {
							Utility.listFileTS[i][j] = pathfile + "/" + subdir.getName() + "/" + strsubFiles[j];
						}
					} // end read file in class
				} // end read class
			} else {
				System.out.println("Error : Directory is empty.");
			}
		} else {
			System.out.println("Error : path file is not Directory.");
		}
	}

	public static void read_path_Usage(String pathfile) {
		File dir = new File(pathfile);
		if (dir.isDirectory()) { // check directory
			String strFiles[] = dir.list(); // get List Files
			Utility.listFileUsage = new String[strFiles.length];
			for (int i = 0; i < strFiles.length; i++) {
				Utility.listFileUsage[i] = pathfile + "/" + strFiles[i];
			}
		} else {
			System.out.println("Error : path file is not Directory.");
			Utility.listFileUsage = new String[0];
		}
	}

	// load subDirectory
	// private static String[] load_subDirectory() {
	// String subf[] = null;
	// try {
	// Scanner read = new
	// Scanner(ResourceFileLoader.readFromJARFile("List_folders.txt"));
	// if (Utility.classes.equals("threeClass"))
	// read = new
	// Scanner(ResourceFileLoader.readFromJARFile("List_rating.txt"));
	// int numfloder = read.nextInt();
	// subf = new String[numfloder];
	// read.nextLine();
	// for (int i = 0; i < numfloder; i++) {
	// subf[i] = read.nextLine();
	// }
	// } // End try
	// catch (Exception e) {
	// System.out.println("Error -- " + e.toString());
	// } // End catch
	// return subf;
	// }

	// load class label
	private static String[] load_subDirectory() {
		
		String subf[] = Utility.classLabel;
		
		return subf;
	}
}