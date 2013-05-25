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
		String thisIp = "1.1.1.11";
		String ip = "1.1.1.12";
		int port = 1234;
		FolderTree ft = new FolderTree(folderName, uname, null, ip, port);
		//ft.addUser(uname, path, thisIp, port );
		File f = new File(FileName,path+FileName, uname);
		ft.addFile(f, uname);
		FileClient client = new FileClient(ft, null, ip, uname+FileName,path, FileName, uname);

	}

}
