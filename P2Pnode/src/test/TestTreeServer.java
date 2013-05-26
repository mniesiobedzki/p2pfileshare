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
				long data = file.lastModified();
				System.out.println(data);
				ft.addFile(f, uname);
				ft.getFolder().get(uname+f.getFileName()).getHistory().getLast().setData(data);
				MFolderListener.filesAndTheirHistory.put(
						uname + f.getFileName(), f);
				System.out.println("** "+f.getFileId()+ " zmodyfikowany: "+ ft.getFolder().get(uname+f.getFileName()).getHistory().getLast().getData());
			}
			
			MFolderListener.runFolderListener(path, ft, uname);

			while (true) {
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
					System.err.println(ft.toString());
					objectOutput.writeObject(ft);
					ft.updated=false;
				}

				objectOutput.flush();
				objectOutput.close();
				socketStream.flush();
				socketStream.close();
				welcomeSocket.close();
				Thread.sleep(1000);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
