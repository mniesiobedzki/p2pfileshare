package test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import file.File;
import file.FileServer;
import folder.FolderTree;
import folder.Nod;


public class TestServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("c:\\test.txt", "UTF-8");
			writer.println("pierwsza linia testu");
			writer.println("II linia testu");
			writer.println("#3 linia testu");
			writer.println("0100 linia testu");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		String fileName = "test.txt";
		String path = "kuku\\";
		String folderName = "kuku";
		String uname = "user1";
		FolderTree ft = new FolderTree(folderName);
		ft.addUser(uname, path);
		File f = new File(fileName);
		ft.addFile(f, uname);
		FileServer server = new FileServer(ft, uname);
	}

}
