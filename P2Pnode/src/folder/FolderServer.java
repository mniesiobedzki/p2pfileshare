package folder;

import java.io.*;
import java.net.*;
import file.File;
import folder.FolderTree;
import folder.MFolderListener;

public class FolderServer implements Runnable {

	
	String uname;
	String path;
	int port;
	public FolderTree ft;
	
	@Override
	public void run() {
		ServerSocket welcomeSocket;
		try {
			welcomeSocket = new ServerSocket(port);
			java.io.File[] listaFajli = File.listAllTheFilesInDir(path);

			for (java.io.File file : listaFajli) {
				if(file==null){
					break;
				}
				File f = new File(file.getName(), uname);

				System.out.println(f.getFileName());
				
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
				if (MFolderListener.fileCreated) {
					System.err.println(ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileCreated = false;
				}
				if ( MFolderListener.fileDeleted) {
					System.err.println(ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileDeleted = false;
				}
				if (MFolderListener.fileModified) {
					System.err.println(ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileModified = false;
				}
				if (ft.updated){
					ft.updated=false;
					System.err.println(ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileModified = false;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FolderServer(FolderTree ft, String uname, String path, int port) {
		super();
		this.uname = uname;
		this.path = path;
		this.port = port;
		this.ft = ft;
	}
}
