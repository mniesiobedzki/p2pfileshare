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
	  public FileClient (String host, String key, String name ) {


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
	    this.receiveFile(is, name);
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

	  public void receiveFile(InputStream is, String name) throws Exception{
		  try{
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
	 
			StringBuilder sb = new StringBuilder();
	 
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
	 
			System.out.println(sb.toString());
			System.out.println("\nDone!");
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	  }
	}
