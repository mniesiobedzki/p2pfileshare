package test;

import java.io.*;
import java.net.*;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import file.File;
import folder.FolderTree;
import folder.MFolderListener;

public class TestTreeServer implements Runnable {

	
	String fname;
	String uname;
	String path;
	String ip;
	int port;
	public FolderTree ft;
	JCSyncTreeMap syncTree;
	
	@Override
	public void run() {
		ServerSocket welcomeSocket;
		try {

			while (true) {
			welcomeSocket = new ServerSocket(port);

			ft = new FolderTree(fname,uname, syncTree,ip, port);
			ft.addUser(uname, path, ip, port);
			// File fa = new File(name, File.generateFileId(uname));
			java.io.File[] listaFajli = File.listAllTheFilesInDir(path);

			for (java.io.File file : listaFajli) {
				if(file==null){
					break;
				}
				
				File f = new File(file.getName(), file.getPath(), uname);
				System.out.println(f.getFileName());
				
				ft.addFile(f, uname);
				MFolderListener.filesAndTheirHistory.put(
						uname + f.getFileName(), f);
				System.out.println("** "+f.getFileId());
			}
			MFolderListener.runFolderListener(path, ft, uname);
			Socket connectionSocket = welcomeSocket.accept();
			System.err.println("Connected");

			OutputStream socketStream = connectionSocket.getOutputStream();
			ObjectOutput objectOutput = new ObjectOutputStream(socketStream);
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

				objectOutput.flush();
				objectOutput.close();
				socketStream.flush();
				socketStream.close();
				welcomeSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * @param fname
	 *            - folder name
	 * @param uname
	 *            - user name
	 * @param path
	 *            - file path
	 * @param port - port
	 */
	public TestTreeServer(JCSyncTreeMap syncTree, String fname, String uname, String path, int port, String ip) {
		super();
		this.syncTree=syncTree;
		this.fname = fname;
		this.uname = uname;
		this.path = path;
		this.port = port;
		this.ip = ip;
	}
}
