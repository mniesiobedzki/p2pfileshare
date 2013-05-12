package test;

import file.File;
import folder.FolderTree;
import folder.Nod;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FolderTree ft = new FolderTree("c:\\");
		ft.addUser("user1");
		File f = new File("test.txt");
		ft.addFile(f, "user1");
	}

}
