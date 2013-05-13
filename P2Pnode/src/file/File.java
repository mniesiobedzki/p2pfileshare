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
	// @param String ma postać "user1Nowy dokument tekstowy - Kopia (4).txt" 
	public static Map<String, File> filesAndTheirHistory = new HashMap<String, File>();
	public static Path listenedPath;		// ścieżka folderu który jest nasłuchiwany
	
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
	
	public static void runFolderListener(String path, final FolderTree folderTree, final String userId){
		//final Path myDir = Paths.get("C:/Programowanie"); // define a folder root
		
		final Path myDir = Paths.get(path); // define a folder root
		listenedPath = myDir;
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (true) {
						handleDirectoryChangeEvent(myDir,folderTree,userId);
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
	
	private static void handleDirectoryChangeEvent(Path myDir, FolderTree folderTree, String userId) {
		
		try 
		{
			WatchService watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
									StandardWatchEventKinds.ENTRY_DELETE,
									StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey watchKey = watcher.take();

			List<WatchEvent<?>> events = watchKey.pollEvents();
			
			for (WatchEvent event : events) {
				if(event.context().toString().startsWith("^")){
					continue;
				}

				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					System.out.println("Created: " + event.context().toString());
					
					java.io.File kuku = new java.io.File(event.context().toString());
					
					File newlyCreatedFile = new File(event.context().toString(), userId);
					
					newlyCreatedFile.setFileStateHistoryEntry(
							kuku.lastModified(), event.context().toString(),
							userId, new java.io.File(event.context().toString()).length(),
							getMD5Checksum(listenedPath.toString() + "\\"
									+ event.context().toString()));
					
					System.err.println("putuje: "+userId+newlyCreatedFile.getFileName());
					
					filesAndTheirHistory.put(userId+newlyCreatedFile.getFileName(),
							newlyCreatedFile);
					
					folderTree.addFile(newlyCreatedFile, userId);

					// setFileStateHistoryEntry(new Date().getTime(),
					// event.context().toString(),
					// "1111",
					// new java.io.File(myDir.toString() + "/" +
					// event.context().toString()).length(),
					// FileState.generateFileMD5Hash(myDir.toString()+"/" +
					// event.context().toString())
					// );
					// filesAndTheirHistory.put(getFileId(), this);
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("Delete: " + event.context().toString());
					// Entry<String, File> deFajl =
					// searchForFile(event.context().toString());
					// deFajl.getValue().setFileStateHistoryEntry(new
					// Date().getTime(), "deleted",userId,0,"");
					int licznik = 0;
					
					java.io.File[] listaPlikowWFolderze = File.listAllTheFilesInDir(myDir.toString());
					
					for (String nodek : folderTree.getFolder().get(userId).getChildren()) {
						Nod singleNod = folderTree.getFolder().get(nodek); // Jeden plik u usera
						
						if(filesAndTheirHistory.get(userId+singleNod.getName()).singleFileHistory.getLast() == null){
							break;
						}
						
						boolean exists = false;
						for (java.io.File file : listaPlikowWFolderze) {
							if (file.getName().equals(singleNod.getName())) {
								exists = true;
							}
						}
						if (!exists) {
							System.err.println("BUJA");
							filesAndTheirHistory.get(userId+singleNod.getName()).singleFileHistory.add(null);
							System.err.println("WYKASOWANO " + userId+singleNod.getName() +"\n");
						}
					}

					// Metoda ustawiaj�ca pola obiektu FileState na stan -
					// DELETED
					// To-Do PERSON ID podstawic prawdziwe dane
					// setFileStateHistoryEntry(new
					// Date().getTime(),"deleted","1111",0,"");

				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					System.out.println("Modify: " + event.context().toString());

					Entry<String, File> deFajl = searchForFile(event.context()
							.toString());
					java.io.File kuku = new java.io.File(event.context().toString());
					deFajl.getValue().setFileStateHistoryEntry(
							kuku.lastModified(),
							event.context().toString(),
							userId,
							new java.io.File(myDir.toString() + "/"
									+ event.context().toString()).length(),
							getMD5Checksum(listenedPath.toString() + "\\"
									+ event.context().toString()));
					// Metoda ustawiaj�ca pola obiektu FileState
					// To-Do PERSON ID podstawic prawdziwe dane
					// setFileStateHistoryEntry(new Date().getTime(),
					// event.context().toString(),
					// "1111",
					// new java.io.File(myDir.toString() + "/" +
					// event.context().toString()).length(),
					// FileState.generateFileMD5Hash(myDir.toString()+"/" +
					// event.context().toString())
					// );
				}
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
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
		int counter = 0;
		for (int i = 1; i < listOfFiles.length-counter; i++) {
			if(listOfFiles[i-1].getName().startsWith("^")){
				listOfFiles[i-1]=listOfFiles[i];
				counter++;
			}
		}

		return  Arrays.copyOfRange(listOfFiles, 0, listOfFiles.length-counter-1);
		
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
	
	private void setFileStateHistoryEntry(long entryDate, String fileName, String userID, long fileSize, String fileMD5Hash) {

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

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}
