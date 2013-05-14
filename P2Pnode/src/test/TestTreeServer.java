package test;

import java.io.*;
import java.net.*;
import file.File;
import folder.FolderTree;
import folder.MFolderListener;

public class TestTreeServer implements Runnable {

	/***
	 * @param fname
	 *            - folder name
	 * @param uname
	 *            - user name
	 * @param path
	 *            - file path
	 */
	String fname;
	String uname;
	String path;

	@Override
	public void run() {
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(6789);

			FolderTree ft = new FolderTree(fname);
			ft.addUser(uname, path);
			// File fa = new File(name, File.generateFileId(uname));
			java.io.File[] listaFajli = File.listAllTheFilesInDir(path);

			for (java.io.File file : listaFajli) {
				File f = new File(file.getName(), uname);
				ft.addFile(f, uname);
				MFolderListener.filesAndTheirHistory.put(
						uname + f.getFileName(), f);
				System.out.println(f.getFileId());
			}
			MFolderListener.runFolderListener(path, ft, uname);
			Socket connectionSocket = welcomeSocket.accept();
			System.err.println("Connected");

			OutputStream socketStream = connectionSocket.getOutputStream();
			ObjectOutput objectOutput = new ObjectOutputStream(socketStream);
			while (true) {
				if (MFolderListener.fileCreated || MFolderListener.fileDeleted || MFolderListener.fileModified) {
					objectOutput.writeObject(ft);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TestTreeServer(String fname, String uname, String path) {
		super();
		this.fname = fname;
		this.uname = uname;
		this.path = path;
	}
}
