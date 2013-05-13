package file;

import gui.GuiWindower;

import java.security.MessageDigest;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import node.ClientP2Pnode;

public class File {

	// Lista obiektów typu FileState.
	// Lista ta przechowuje informacje o wszystkich modyfikacjach
	// danego pliku z folderu.
	
	public static Map<String, File> filesAndTheirHistory = new HashMap<String, File>();
	public static Path listenedPath;		// ścieżka folderu który jest nasłuchiwany
	
	// file hash pliku z pakietu P2PP
	private String fileId; 
	private String fileName;
	
	// historia zmian pliku 
	private LinkedList<FileState> singleFileHistory;

	public File(){
		fileId 				= generateFileId("1111");
		singleFileHistory 	= new LinkedList<FileState>();
		fileName 			= "TO TRZEBA ZMIENIC";
	}
	
	public File(ClientP2Pnode clientP2Pnode) {
		// TODO Auto-generated constructor stub
	}
	
	public File(String fileName, String fileId){
		this.fileName = fileName;
		this.fileId   = generateFileId(fileId);
	}

	/*******************REGION METOD**********************/
	public File getCurrentFileWithLatestHistoryEntry(){
		File file = new File();
		file.setFileId(this.getFileId());
		LinkedList<FileState> latestFileState = new LinkedList<FileState>();
		file.setSingleFileHistory(latestFileState);
		return file; 
	}
	
	public static void runFolderListener(String path){
		//final Path myDir = Paths.get("C:/Programowanie"); // define a folder root
		
		final Path myDir = Paths.get(path); // define a folder root
		listenedPath = myDir;
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (true) {
						handleDirectoryChangeEvent(myDir);
					}
				}
			};

			Thread t = new Thread(r);
			t.start();
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static void handleDirectoryChangeEvent(Path myDir) {
		
		try 
		{
			WatchService watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
									StandardWatchEventKinds.ENTRY_DELETE,
									StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey watchKey = watcher.take();

			List<WatchEvent<?>> events = watchKey.pollEvents();
			for (WatchEvent event : events) {
				
				Entry<String, File> deFajl = searchForFile(event.context().toString());
				
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					System.out.println("Created: " + event.context().toString());				
					
					// To-Do PERSON ID podstawic prawdziwe dane
					deFajl.getValue().setFileStateHistoryEntry(
							new Date().getTime(),
							event.context().toString(),
							"USER ID TO ZMIENIC",
							new java.io.File(myDir.toString() + "/"	+ event.context().toString()).length(),
							getMD5Checksum(listenedPath.toString() + "\\" + event.context().toString())
							);
					
					filesAndTheirHistory.put(deFajl.getValue().getFileId(), deFajl.getValue());
					
					
//					setFileStateHistoryEntry(new Date().getTime(),
//							event.context().toString(),
//							"1111",
//							new java.io.File(myDir.toString() + "/" + event.context().toString()).length(),
//							FileState.generateFileMD5Hash(myDir.toString()+"/" + event.context().toString())
//						);
//					filesAndTheirHistory.put(getFileId(), this);
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("Delete: " + event.context().toString());
						
					deFajl.getValue().setFileStateHistoryEntry(new Date().getTime(), "deleted", "USER ID TO ZMIENIC",0,"");
					
					//Metoda ustawiaj�ca pola obiektu FileState na stan - DELETED
					// To-Do PERSON ID podstawic prawdziwe dane
//					setFileStateHistoryEntry(new Date().getTime(),"deleted","1111",0,"");
					
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					System.out.println("Modify: " + event.context().toString());
					
					deFajl.getValue().setFileStateHistoryEntry(new Date().getTime(),
							event.context().toString(),
							"USER ID TO ZMIENIC",
							new java.io.File(myDir.toString() + "/"	+ event.context().toString()).length(),
							getMD5Checksum(listenedPath.toString() + "\\" + event.context().toString())
							);
					//Metoda ustawiaj�ca pola obiektu FileState
					// To-Do PERSON ID podstawic prawdziwe dane
//					setFileStateHistoryEntry(new Date().getTime(),
//							event.context().toString(),
//							"1111",
//							new java.io.File(myDir.toString() + "/" + event.context().toString()).length(),
//							FileState.generateFileMD5Hash(myDir.toString()+"/" + event.context().toString())
//						);
				}
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}
	
	/**
	 * Metoda ustawia obiekt typu FileState i dodaje go do  
	 * historii zmian danego File
	 *  
	 * @param entryDate
	 * @param fileName
	 * @param userID
	 * @param fileSize
	 * @param fileMD5Hash
	 * 
	 * 
	 */
	
	private void setFileStateHistoryEntry(long entryDate, String fileName, String userID, long fileSize, String fileMD5Hash) {

		FileState fileStateObj = new FileState();
		
		fileStateObj.setData(entryDate);
		fileStateObj.setFileName(fileName);
		fileStateObj.setPersonID(userID);
		fileStateObj.setSize(fileSize);
		fileStateObj.setMd5(fileMD5Hash);
		
		this.getSingleFileHistory().add(fileStateObj);
	}

	private String generateFileId(String userId) {
		return userId+"_"+new Date().getTime();
	}
	
	private static Entry<String, File> searchForFile(String fileName){
		
		 try {
			String currentFileMD5 = getMD5Checksum(listenedPath.toString()+"\\"+fileName);
			for (Entry<String, File> entry : filesAndTheirHistory.entrySet()) {
			    if(currentFileMD5.equals(entry.getValue().singleFileHistory.getLast().getMd5())){
			    	return entry;
			    }
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/*****************************************************/
	
	 public static byte[] createChecksum(String filename) throws Exception {
	       InputStream fis =  new FileInputStream(filename);

	       byte[] buffer = new byte[1024];
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
	
	// Gettery i settery WszystkiePlikiHistorii
	
	public Map<String, File> getFilesAndTheirHistory() {
		return filesAndTheirHistory;
	}

	public void setFilesAndTheirHistory(
			Map<String, File> filesAndTheirHistory) {
		File.filesAndTheirHistory = filesAndTheirHistory;
	}

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

}
