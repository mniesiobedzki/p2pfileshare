package file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import folder.FolderTree;
import folder.Nod;

public class FileServer extends Thread {
	public FolderTree tree;
	public String uname;
	public FileServer(FolderTree ft, String name) {
		// create socket
		tree=ft;
		uname=name;
		this.start();

	}

	public void send(OutputStream os, String path) throws Exception {
		// sendfile
		java.io.File myFile = new java.io.File(path);
		byte[] mybytearray = new byte[(int) myFile.length() + 1];
		FileInputStream fis = new FileInputStream(myFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(mybytearray, 0, mybytearray.length);
		System.out.println("Sending...");
		os.write(mybytearray, 0, mybytearray.length);
		os.flush();
	}

	public void run() {
		ServerSocket servsock;
		try {
			servsock = new ServerSocket(13267);

			System.out.println("Waiting...");

			Socket sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);
			OutputStream os = sock.getOutputStream();

			InputStream is = sock.getInputStream();
			java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\n");
			while (true) {
				while (!s.hasNext()) {
				}
				String msg = s.next();
				String path = tree.getFolder().get(uname).getPath();
				//String path = tree.getFolder().get("root").getValue();
				Nod n = tree.getFolder().get(msg);
				path += n.getValue();
				this.send(os, path);
				sock.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}