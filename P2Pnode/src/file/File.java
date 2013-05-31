package file;


import java.security.MessageDigest;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

import node.ClientP2Pnode;

public class File implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2367214998788855842L;
	// file hash pliku z pakietu P2PP
	private String fileId; 
	private String fileName;
	private long lastModified;
	

	// historia zmian pliku 
	private LinkedList<FileState> singleFileHistory;
	private String filePath;

	public File(){
		this.fileName 			= "TO TRZEBA ZMIENIC";
		this.fileId 			= generateFileId("1111", fileName, this);
		this.singleFileHistory 	= new LinkedList<FileState>();
		this.filePath			= "to te¿ trzeba zminiæ i czemu do cholery podawa³eœ name jako path?";
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public File(ClientP2Pnode clientP2Pnode) {
		// TODO Auto-generated constructor stub
	}
	
	public File(String fileName, String filePath, String userId){
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileId   = generateFileId(userId, fileName, this);
		this.singleFileHistory 	= new LinkedList<FileState>();
		
		try {
			java.io.File kuku = new java.io.File(fileName);
			this.singleFileHistory.add(new FileState(userId,kuku.lastModified(),fileName,kuku.length(),File.getMD5Checksum(filePath), this.getFileId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*******************REGION METOD**********************/
	
	/***
	 * 
	 * Zwraca obiekt "File" z jednoelementowÄ… LinkedListÄ… "FileState".
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
	 * Metoda sprawdzajÄ…ca jakie pliki sÄ… w folderze. Tworzy siÄ™ lista ktora potem jest porÃ³wnywana z elementami w drzewie.
	 * 
	 */
	public static java.io.File[] listAllTheFilesInDir(String directoryPath) {
		
		java.io.File folder = new java.io.File(directoryPath);
		java.io.File[] listOfFiles = folder.listFiles();
		if(listOfFiles.length != 0){
			int counter = 0;
			
			for (int i = 1; i < listOfFiles.length-counter; i++) {
				if(listOfFiles[i].getName().contains("^")){
					System.out.println("ptaszek wykryty");
					counter++;
				}
			}
			java.io.File[] backupArray = new java.io.File[listOfFiles.length-counter];
			
			counter = 0;
			
			for (int i = 0; i < listOfFiles.length; i++) {
				if(!listOfFiles[i].getName().contains("^")){
					backupArray[counter] = listOfFiles[i];
					counter++;
				}
			}
			return backupArray;
			
		}
		else{
			
			return new java.io.File[0]; 
			
		}
	}
	
	public void setFileStateHistoryEntry(long entryDate, String fileName, String userID, long fileSize, String fileMD5Hash) {

		FileState fileStateObj = new FileState(userID,entryDate,fileName,fileSize,fileMD5Hash, this.getFileId());
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
