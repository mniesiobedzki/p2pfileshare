package test;

import java.io.*;
import java.net.*;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;
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
	int portT;
	public FolderTree ft;
	JCSyncHashMap syncTree;
	
	@Override
	public void run() {
		ServerSocket welcomeSocket;
		try {


			ft = new FolderTree(fname,uname, syncTree,ip, port);
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

			welcomeSocket = new ServerSocket(portT);
			Socket connectionSocket = welcomeSocket.accept();
			System.err.println("TestTreeServer: Connected");

			OutputStream socketStream = connectionSocket.getOutputStream();
			ObjectOutput objectOutput = new ObjectOutputStream(socketStream);
				if (MFolderListener.fileCreated) {
					System.err.println("TestTreeServer: "+ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileCreated = false;
				}
				else if ( MFolderListener.fileDeleted) {
					System.err.println("TestTreeServer: "+ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileDeleted = false;
				}
				else if (MFolderListener.fileModified) {
					System.err.println("TestTreeServer: "+ft.toString());
					objectOutput.writeObject(ft);
					MFolderListener.fileModified = false;
				}
				else if (ft.updated){
					System.err.println("TestTreeServer: "+ft.toString());
					objectOutput.writeObject(ft);
					ft.updated=false;
				}else {
					objectOutput.writeObject(null);
				}
				Thread.sleep(500);
				objectOutput.flush();
				socketStream.flush();
				objectOutput.close();
				socketStream.close();
				welcomeSocket.close();
				Thread.sleep(500);
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
	public TestTreeServer(JCSyncHashMap syncTree, String fname, String uname, String path, int port, int portT, String ip) {
		super();
		this.syncTree=syncTree;
		this.fname = fname;
		this.uname = uname;
		this.path = path;
		this.port = port;
		this.portT = portT;
		this.ip = ip;
	}
}
