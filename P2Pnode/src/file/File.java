package file;

import folder.FolderTree;
import folder.Nod;
import gui.GuiWindower;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import node.ClientP2Pnode;

public class File implements Serializable{

	
	// file hash pliku z pakietu P2PP
	private String fileId; 
	private String fileName;
	private long lastModified;
	

	// historia zmian pliku 
	private LinkedList<FileState> singleFileHistory;

	public File(){
		this.fileName 			= "TO TRZEBA ZMIENIC";
		this.fileId 			= generateFileId("1111", fileName, this);
		this.singleFileHistory 	= new LinkedList<FileState>();
	}
	
	public File(ClientP2Pnode clientP2Pnode) {
		// TODO Auto-generated constructor stub
	}
	
	public File(String fileName, String userId){
		this.fileName = fileName;
		this.fileId   = generateFileId(userId, fileName, this);
		this.singleFileHistory 	= new LinkedList<FileState>();
		
		try {
			java.io.File kuku = new java.io.File(fileName);
			this.singleFileHistory.add(new FileState(userId,kuku.lastModified(),fileName,kuku.length(),File.getMD5Checksum(fileName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************REGION METOD**********************/
	
	/***
	 * 
	 * Zwraca obiekt "File" z jednoelementową LinkedListą "FileState".
	 * Lista ta zawiera tylko ostatni wpis z historii zmian pliku. 
	 * 
	 * @return
	 */
	
	public File getCurrentFileWithLatestHistoryEntry(){
		File file = new File();
		file.setFileId(this.getFileId());
		LinkedList<FileState> latestFileState = new LinkedList<FileState>();
		file.setSingleFileHistory(latestFileState);
		return file; 
	}
	
	

	/***
	 * 
	 * Metoda sprawdzająca jakie pliki są w folderze. Tworzy się lista ktora potem jest porównywana z elementami w drzewie.
	 * 
	 */
	public static java.io.File[] listAllTheFilesInDir(String directoryPath) {
		
		String files;
		java.io.File folder = new java.io.File(directoryPath);
		java.io.File[] listOfFiles = folder.listFiles();
		if(listOfFiles.length != 0){
			int counter = 0;
			for (int i = 1; i < listOfFiles.length-counter; i++) {
				if(listOfFiles[i-1].getName().startsWith("^")){
					listOfFiles[i-1]=listOfFiles[i];
					counter++;
				}
			}
			return  Arrays.copyOfRange(listOfFiles, 0, listOfFiles.length-counter-1);
		}else{
			return new java.io.File[0]; 
		}

		
		
//		for (int i = 0; i < listOfFiles.length; i++) {
//			if (listOfFiles[i].isFile()) {
//				files = listOfFiles[i].getName();
//				System.out.println(files);
//				if (files.endsWith(".txt") || files.endsWith(".TXT")) {
//					
//				}
//			}
//		}
	}
	
	public void setFileStateHistoryEntry(long entryDate, String fileName, String userID, long fileSize, String fileMD5Hash) {

		FileState fileStateObj = new FileState(userID,entryDate,fileName,fileSize,fileMD5Hash);
		this.getSingleFileHistory().add(fileStateObj);
	}

	/***
	 * 
	 * Generuje ID dla nowo tworzonego pliku.
	 * 
	 * @param userId
	 * @param fileName 
	 * @param myFile 
	 * @return
	 */
	
	public static String generateFileId(String userId, String fileName, File myFile) {
		java.io.File file = new java.io.File(fileName);
		myFile.setLastModified(file.lastModified());
		return userId+"_"+file.lastModified()+"_"+fileName;
	}
	
	/***
	 * Checking if the file is currently stored within our HashMaps.
	 * What this method does is:
	 * 1. It calculates MD5 checksum of a file with the specified fileName
	 * 2. Goes through all the files within our system.
	 * 3. Returns HashMap entry if the file had been found
	 * 4. Returns NULL if the file does not exist in the system. 
	 * 
	 * @param fileName
	 * @return
	 */
	
	

	/*****************************************************/
	
	 public static byte[] createChecksum(String filename) throws Exception {
		byte[] buffer = new byte[1024];
		try {
			InputStream fis = new FileInputStream(filename);

			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;

			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);

			fis.close();
			return complete.digest();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return buffer;

	}

	   // see this How-to for a faster way to convert
	   // a byte array to a HEX string
	   public static String getMD5Checksum(String filename) throws Exception {
	       byte[] b = createChecksum(filename);
	       String result = "";

	       for (int i=0; i < b.length; i++) {
	           result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	       }
	       return result;
	   }
	
	/*****************************************************/
	
	// Gettery i settery SingleFileHistory
	
	public LinkedList<FileState> getSingleFileHistory() {
		return singleFileHistory;
	}

	public void setSingleFileHistory(LinkedList<FileState> singleFileHistory) {
		this.singleFileHistory = singleFileHistory;
	}

	// Gettery i settery FileID
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}
