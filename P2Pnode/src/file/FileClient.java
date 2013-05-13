package file;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

public class FileClient{
	/***
	 * 
	 * @param host - Hosts IP
	 * @param key - Key of file to transfer
	 * @param path - Local Path to save
	 * @param name - Files Name
	 */
	  public FileClient (String host, String key, String path, String name ) {
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
	    this.receiveFile(is, path, name);
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

	  /***
	   * 
	   * @param is - Input stream to save from
	   * @param path - Files path
	   * @param name - Files name
	   * @throws Exception - Goddamn Exception
	   */
	  public void receiveFile(InputStream is, String path, String name ) throws Exception{
		  try {
				// read this file into InputStream
		 
				// write the inputStream to a FileOutputStream
				FileOutputStream outputStream = new FileOutputStream(new java.io.File(path+name));
		 
				int read = 0;
				byte[] bytes = new byte[1024];
		 
				while ((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
		 
				System.out.println("Done!");
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	}
