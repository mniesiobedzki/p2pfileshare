package test;

import file.File;
import file.FileClient;
import folder.FolderTree;

public class TestFile {

	public static void main(String[] args) {		
		FolderTree ft = new FolderTree("kuku\\");
		String name = "test.txt";
		String uname = "user1";
		ft.addUser(uname);
		File f = new File(name,File.generateFileId(uname));
		ft.addFile(f, uname);
		
		File.runFolderListener("kuku\\");
		
		FileClient client = new FileClient("1.1.1.20", uname+name, name);		
	}

}
