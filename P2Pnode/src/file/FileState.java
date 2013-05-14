package file;

import java.io.FileInputStream;
import java.io.Serializable;
import java.security.MessageDigest;

// Obiekt kt�ry jest dodawany do LinkedListy folderu kt�ry obserwujemy.
// FileState jest po prostu wygodn� wersj� wiersza w historii danego pliku 

public class FileState implements Serializable{
	
	private	String personID = ""; 	// kto zmienil plik
	private long   data 	= 0;	// kiedy plik by� zmieniany		
	private String fileName = "";	// nazwa zmienianego pliku
	private double size		= 0;	// rozmiar pliku
	private String md5		= "";	// suma kontrolna		
	
	public FileState(String personID, long data, String fileName, double size,
			String md5) {
		super();
		this.personID = personID;
		this.data = data;
		this.fileName = fileName;
		this.size = size;
		this.md5 = md5;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}
	
//	public static String generateFileMD5Hash(String filePath){
//		
//		String datafile = filePath;
//		try
//		{
//		    MessageDigest md = MessageDigest.getInstance("MD5");
//		    FileInputStream fis = new FileInputStream(datafile);
//		    byte[] dataBytes = new byte[1024];
//		 
//		    int nread = 0; 
//		 
//		    while ((nread = fis.read(dataBytes)) != -1) {
//		      md.update(dataBytes, 0, nread);
//		    };
//		 
//		    byte[] mdbytes = md.digest();
//		 
//		    //convert the byte to hex format
//		    StringBuffer sb = new StringBuffer("");
//		    for (int i = 0; i < mdbytes.length; i++) {
//		    	sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
//		    }
//		    
//		    System.out.println("DEBUG: MD5 " + sb.toString());
//		    fis.close();
//			return sb.toString();
//			
//		} catch(Exception e){
//		
//		}
//		return "";
//	}
	
}
