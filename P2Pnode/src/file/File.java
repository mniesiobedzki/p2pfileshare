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

	private String fileHash = ""; 					// file hash pliku z pakietu P2PP
	private LinkedList<FileState> singleFileHistory = new LinkedList<FileState>();
	
	// Lista obiekt�w typu FileState.
	// Lista ta przechowuje informacje o wszystkich modyfikacjach
	// danego pliku z folderu.
	
	public static Map<String, File> filesAndTheirHistory = new HashMap<String, File>();

	public static void main(String[] args) {
		// define a folder root
		final Path myDir = Paths.get("C:/Programowanie");
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
		
		FileState fileStateObj = new FileState();

		try {
			WatchService watcher = myDir.getFileSystem().newWatchService();
			myDir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
									StandardWatchEventKinds.ENTRY_DELETE,
									StandardWatchEventKinds.ENTRY_MODIFY);

			WatchKey watchKey = watcher.take();

			List<WatchEvent<?>> events = watchKey.pollEvents();
			for (WatchEvent event : events) {
				if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					System.out.println("Created: " + event.context().toString());
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
					System.out.println("Delete: " + event.context().toString());
				}
				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
					System.out.println("Modify: " + event.context().toString());
					
					java.io.File file = new java.io.File(myDir.toString()+"/"+ event.context().toString());
					long lDateTime = new Date().getTime();
					fileStateObj.setData(lDateTime);
					fileStateObj.setFileName(event.context().toString());
					fileStateObj.setPersonID("1111");
					fileStateObj.setSize(file.length());
					
					fileStateObj.setMd5(
							fileStateObj.generateFileMD5Hash(
									myDir.toString()+"/" + event.context().toString()
									)
							);

				}
			}

		} catch (Exception e) {
			System.out.println("Error: " + e.toString());
		}
	}

	public Map<String, File> getFilesAndTheirHistory() {
		return filesAndTheirHistory;
	}

	public void setFilesAndTheirHistory(
			Map<String, File> filesAndTheirHistory) {
		File.filesAndTheirHistory = filesAndTheirHistory;
	}

	public LinkedList<FileState> getSingleFileHistory() {
		return singleFileHistory;
	}

	public void setSingleFileHistory(LinkedList<FileState> singleFileHistory) {
		this.singleFileHistory = singleFileHistory;
	}

	public String getFileHash() {
		return fileHash;
	}

	public void setFileHash(String fileHash) {
		this.fileHash = fileHash;
	}

}
