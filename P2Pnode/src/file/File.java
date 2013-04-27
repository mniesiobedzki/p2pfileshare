package file;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

public class File {

	// Lista obiektów typu FileState.
	// Lista ta przechowuje informacje o wszystkich modyfikacjach
	// danego pliku z folderu.
	//
	// TO-DO trzeba przerzuciæ to do g³ównej klasy
	
	public static Map<String, File> filesAndTheirHistory = new HashMap<String, File>();
	
	// file hash pliku z pakietu P2PP
	private String fileId = generateFileId(); 

	// historia zmian pliku 
	private LinkedList<FileState> singleFileHistory = new LinkedList<FileState>();
	
	public void main(String[] args) {
		
		final Path myDir = Paths.get("C:/Programowanie"); // define a folder root
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

	
	private String generateFileId() {
		
		return null;
	}

	private void handleDirectoryChangeEvent(Path myDir) {
		
		try 
		{
			WatchService watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
									StandardWatchEventKinds.ENTRY_DELETE,
									StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey watchKey = watcher.take();

			List<WatchEvent<?>> events = watchKey.pollEvents();
			for (WatchEvent event : events) {
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					System.out.println("Created: " + event.context().toString());
					
					// fileId = ""; // to-do ustawianie file hasha
					filesAndTheirHistory.put(getFileId(), this);
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("Delete: " + event.context().toString());
					
					//Metoda ustawiaj¹ca pola obiektu FileState na stan - DELETED
					setFileStateHistoryEntry(new Date().getTime(),"deleted","1111",0,"");
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					System.out.println("Modify: " + event.context().toString());
					
					//Metoda ustawiaj¹ca pola obiektu FileState
					// To-Do PERSON ID podstawic prawdziwe dane
					setFileStateHistoryEntry(new Date().getTime(),
							event.context().toString(),
							"1111",
							new java.io.File(myDir.toString() + "/" + event.context().toString()).length(),
							FileState.generateFileMD5Hash(myDir.toString()+"/" + event.context().toString())
						);
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
	
	private void setFileStateHistoryEntry(long entryDate, String fileName,
			String userID, long fileSize, String fileMD5Hash) {

		FileState fileStateObj = new FileState();
		
		fileStateObj.setData(entryDate);
		fileStateObj.setFileName(fileName);
		fileStateObj.setPersonID(userID);
		fileStateObj.setSize(fileSize);
		fileStateObj.setMd5(fileMD5Hash);
		
		this.getSingleFileHistory().add(fileStateObj);
	}

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

}
