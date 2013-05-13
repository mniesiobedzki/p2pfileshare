package test;

import file.File;
import file.FileClient;
import folder.FolderTree;

public class TestFile {

	public static void main(String[] args) {		
		FolderTree ft = new FolderTree("kuku");
		String name = "test.txt";
		String uname = "user1";
		ft.addUser(uname, "kuku\\");
		//File fa = new File(name, File.generateFileId(uname));
		java.io.File[] listaFajli = File.listAllTheFilesInDir("kuku\\");
		
		for (java.io.File file : listaFajli) {
			File f = new File(file.getName(),uname);
			ft.addFile(f, uname);
			File.filesAndTheirHistory.put(uname+f.getFileName(),f);
			System.out.println(f.getFileId());
		}
		File.runFolderListener("kuku\\", ft, uname);
		
		//FileClient client = new FileClient("1.1.1.20", uname+name, name);		
	}

}
