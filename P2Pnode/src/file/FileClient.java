package file;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class FileClient{
	  public FileClient (String host, String key ) {


	    long start = System.currentTimeMillis();

	    // localhost for testing
	    Socket sock;
		try {
			sock = new Socket(host,13267);
	    System.out.println("Connecting...");
	    OutputStream os = sock.getOutputStream();
	    key+='\n';
	    os.write(key.getBytes(Charset.forName("UTF-8")));
	    
	    InputStream is = sock.getInputStream();
	    // receive file
	    this.receiveFile(is);
	       long end = System.currentTimeMillis();
	    System.out.println(end-start);

	    sock.close();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }

	  public void receiveFile(InputStream is) throws Exception{
	      int filesize=6022386;
	      int bytesRead;
	      int current = 0;
	      byte [] mybytearray  = new byte [filesize];

	        FileOutputStream fos = new FileOutputStream("def");
	        BufferedOutputStream bos = new BufferedOutputStream(fos);
	        bytesRead = is.read(mybytearray,0,mybytearray.length);
	        current = bytesRead;


	        do {
	           bytesRead =
	              is.read(mybytearray, current, (mybytearray.length-current));
	           if(bytesRead >= 0) current += bytesRead;
	        } while(bytesRead > -1);

	        bos.write(mybytearray, 0 , current);
	        bos.flush();
	        bos.close();
	  }
	}
