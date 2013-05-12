package test;

import file.File;
import file.FileClient;
import folder.FolderTree;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FolderTree ft = new FolderTree("kuku\\");
		String name = "test.txt";
		String uname = "user1";
		ft.addUser(uname);
		File f = new File(name);
		ft.addFile(f, uname);
		FileClient client = new FileClient("1.1.1.20", uname+name, name);

	}

}
