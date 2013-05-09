package file;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
	  public static void main (String [] args ) throws Exception {
	    // create socket
	    ServerSocket servsock = new ServerSocket(13267);
	    while (true) {
	      System.out.println("Waiting...");

	      Socket sock = servsock.accept();
	      System.out.println("Accepted connection : " + sock);
	      OutputStream os = sock.getOutputStream();
	      
	      InputStream is = sock.getInputStream();
		  java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\n");
		  while(true){
			  while(!s.hasNext()){
			  }
			  String msg = s.next();
		  }
		  if(s.hasNext()){
			  
		  }
		  
	    new FileServer().send(os, path);
	      sock.close();
	      }
	    }
	  
	  
	    
	    
	  public void send(OutputStream os, String path) throws Exception{
	      // sendfile
		  java.io.File myFile = new java.io.File (path);
	      byte [] mybytearray  = new byte [(int)myFile.length()+1];
	      FileInputStream fis = new FileInputStream(myFile);
	      BufferedInputStream bis = new BufferedInputStream(fis);
	      bis.read(mybytearray,0,mybytearray.length);
	      System.out.println("Sending...");
	      os.write(mybytearray,0,mybytearray.length);
	      os.flush();
	  }
	}