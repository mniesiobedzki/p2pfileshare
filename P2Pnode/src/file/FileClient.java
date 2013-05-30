package file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import folder.FolderTree;
import folder.MFolderListener;
import folder.Nod;

public class FileClient{
	/***
	 * 
	 * @param ft - folder tree
	 * @param n - nod
	 * @param host - hosts ip
	 * @param key - key of file to transfer
	 * @param path - path of folder
	 * @param name - name of file
	 */
	  public FileClient (FolderTree ft, Nod n, String host, String key, String path, String name , String usr, int port) {
	    long start = System.currentTimeMillis();
	    
	    // localhost for testing
	    Socket sock;
		try {
			sock = new Socket(host,port);
	    System.out.println("Connecting...");
	    OutputStream os = sock.getOutputStream();
//	    System.out.println("klucz: "+key);
//	    System.out.println("klucze: "+ft.getFolder().keySet());
//	    System.out.println("wartoœæ pliku: "+ft.getFolder().values());
//	    System.out.println("plik: "+ft.getFolder().get(key));
//	    System.out.println("historia pliku: "+ft.getFolder().get(key).getHistory());
//	    System.out.println("ostatni element historii: "+ft.getFolder().get(key).getHistory().getLast());
//	    System.out.println("data modyfikacji: "+ft.getFolder().get(key).getHistory().getLast().getData());
//	    System.out.println("wysy³am ¿¹danie udostêpnienia pliku: " + key +" modyfikowanego o "+ ft.getFolder().get(key).getHistory().getLast().getData());
	    key+="\n";
	    os.write(key.getBytes(Charset.forName("UTF-8")));
	    
	    InputStream is = sock.getInputStream();
	    // receive file
	    this.receiveFile(ft, n, is, path, name, usr);
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
	   * @param ft
	   * @param n
	   * @param is
	   * @param path
	   * @param name
	   * @throws Exception
	   */
	  public void receiveFile(FolderTree ft, Nod n, InputStream is, String path, String name , String usr) throws Exception{
		  try {
			  	String tempStamp = "^";
				// read this file into InputStream
		 
				// write the inputStream to a FileOutputStream
				FileOutputStream outputStream = new FileOutputStream(new java.io.File(path+"\\"+tempStamp+name));
		 
				int read = 0;
				byte[] bytes = new byte[1024];
		 
				while ((read = is.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				
				outputStream.close();
				outputStream.flush();
		 
				//rename file to final name without tempStamp at the beginning of filename
				java.io.File filenameWithTempStamp = new java.io.File(path+"\\"+tempStamp+name); 
				MFolderListener.ignorowanyPlik = name;
				java.io.File docelowyPlik = new java.io.File(path+"\\"+name);
				if (docelowyPlik.exists()){
				    docelowyPlik.delete();
				}
				filenameWithTempStamp.renameTo(docelowyPlik);
				File f = new File(n.getName(),n.getPath(), usr);
				f.setFileId(n.getValue());
				long data = n.getHistory().getLast().getData();//ostatnia modyfikacja
				System.out.println("%$$$ data modyfikacji"+ data);
				f.setLastModified(data);
				f.getSingleFileHistory().getLast().setData(data);
				docelowyPlik = new java.io.File(path+name);
				if (docelowyPlik.exists()){
				    docelowyPlik.setLastModified(data);
				}//to powinno ustawiæ date modyfikacji
				System.out.println(docelowyPlik.getAbsolutePath());
				if(docelowyPlik.lastModified()==data){
					System.out.println("czas modyfikacji poprawnie zmieniony");
				}else{
					System.out.println("uj!");
					System.out.println("czas modyfikaci mia³ byæ: "+data);
					System.out.println("czas modyfikacji jest: "+ docelowyPlik.lastModified());
				}
				if(ft!=null){
					ft.addFile(f, usr);
					System.out.println("Dodaje do plikow i historii element o nazwie: "+(usr+n.getName()));
					MFolderListener.filesAndTheirHistory.put(usr+n.getName(), f);
				} 
				System.out.println("Done!");
				if(ft!=null){
					System.out.println("updated Tree: ");
					System.out.println(ft.toString());
					System.out.println(" ");
				}
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }
	}
