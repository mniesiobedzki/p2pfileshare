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
		String path = "kuku\\";
		String folderName = "kuku";
		String FileName = "test.txt";
		String uname = "user1";
		FolderTree ft = new FolderTree(folderName);
		ft.addUser(uname, path);
		File f = new File(FileName);
		ft.addFile(f, uname);
		FileClient client = new FileClient("1.1.1.20", uname+FileName,path, FileName);

	}

}
