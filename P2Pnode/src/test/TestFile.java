package test;

import file.File;
import file.FileClient;
import folder.FolderTree;
import folder.MFolderListener;

public class TestFile {

	public static void main(String[] args) {		
		String uname = "user1";
		FolderTree ft = new FolderTree("kuku", uname, null, "1.1.1.1", 1234);
		//ft.addUser(uname, "kuku\\", "1.1.1.1", 1234);
		//File fa = new File(name, File.generateFileId(uname));
		java.io.File[] listaFajli = File.listAllTheFilesInDir("kuku\\");
		
		for (java.io.File file : listaFajli) {
			File f = new File(file.getName(),uname);
			ft.addFile(f, uname);
			MFolderListener.filesAndTheirHistory.put(uname+f.getFileName(),f);
			System.out.println(f.getFileId());
		}
		MFolderListener.runFolderListener("kuku\\", ft, uname);
		
		//FileClient client = new FileClient("1.1.1.20", uname+name, name);		
	}

}
