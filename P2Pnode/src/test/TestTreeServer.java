package test;

import java.io.*;
import java.net.*;
import file.File;
import folder.FolderTree;
import folder.MFolderListener;

public class TestTreeServer {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		ServerSocket welcomeSocket = new ServerSocket(6789);

		FolderTree ft = new FolderTree("kuku");
		String uname = "user1";
		ft.addUser(uname, "kuku\\");
		// File fa = new File(name, File.generateFileId(uname));
		java.io.File[] listaFajli = File.listAllTheFilesInDir("kuku\\");

		for (java.io.File file : listaFajli) {
			File f = new File(file.getName(), uname);
			ft.addFile(f, uname);
			MFolderListener.filesAndTheirHistory.put(uname + f.getFileName(), f);
			System.out.println(f.getFileId());
		}
		System.out.println("teraz jestem tu");
		MFolderListener.runFolderListener("kuku\\", ft, uname);
		System.out.println("jestem tu");
		Socket connectionSocket = welcomeSocket.accept();
		System.err.println("Connected");

		OutputStream socketStream = connectionSocket.getOutputStream();
		ObjectOutput objectOutput = new ObjectOutputStream(socketStream);
		objectOutput.writeObject(ft);
	}
}
