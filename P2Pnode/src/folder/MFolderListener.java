package folder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import file.File;

public class MFolderListener {
	
	/***
	 * TUTAJ M***** WSTAWI KOMENTARZ
	 */
	
	public static boolean fileCreated 	= false;
	public static boolean fileDeleted	= false;
	public static boolean fileModified 	= false;
	
	public static LinkedList<String> fileCreatedList 	= new LinkedList<String>();
	public static LinkedList<String> fileDeletedList 	= new LinkedList<String>();
	public static LinkedList<String> fileModifiedList 	= new LinkedList<String>();
	
	
	// Lista obiektów typu FileState.
	// Lista ta przechowuje informacje o wszystkich modyfikacjach
	// danego pliku z folderu.
	// @param String ma postać "user1Nowy dokument tekstowy - Kopia (4).txt" 
	public static Map<String, File> filesAndTheirHistory = new HashMap<String, File>();
	public static Path listenedPath;		// ścieżka folderu który jest nasłuchiwany
	
	public static void runFolderListener(String path, final FolderTree folderTree, final String userId){
		
		final Path myDir = Paths.get(path); // define a folder root
		listenedPath = myDir;
		try {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					while (true) {
						handleDirectoryChangeEvent(myDir,folderTree,userId);
						try {
							Thread.sleep(6000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};

			Thread t = new Thread(r);
			t.start();
			//t.join();
		} catch (Exception e) {
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
					
					fileCreated = true;
					
					
					System.out.println("Created: " + event.context().toString());
					
					java.io.File kuku = new java.io.File(event.context().toString());
					
					File newlyCreatedFile = new File(event.context().toString(), userId);
					
					newlyCreatedFile.setFileStateHistoryEntry(
							kuku.lastModified(), event.context().toString(),
							userId, new java.io.File(event.context().toString()).length(),
							File.getMD5Checksum(listenedPath.toString() + "\\"
									+ event.context().toString()));
					
					System.err.println("putuje: "+userId+newlyCreatedFile.getFileName());
					fileCreatedList.push(userId+newlyCreatedFile.getFileName());
					
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
						
						if(filesAndTheirHistory.get(userId+singleNod.getName()).getSingleFileHistory().getLast() == null){
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
							filesAndTheirHistory.get(userId+singleNod.getName()).getSingleFileHistory().add(null);
							fileDeleted = true;
							fileDeletedList.push(userId+singleNod.getName());
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

					Entry<String, File> deFajl = searchForFile(event.context().toString());
					java.io.File kuku = new java.io.File(event.context().toString());
					deFajl.getValue().setFileStateHistoryEntry(
							kuku.lastModified(),
							event.context().toString(),
							userId,
							new java.io.File(myDir.toString() + "/"
									+ event.context().toString()).length(),
							File.getMD5Checksum(listenedPath.toString() + "\\"
									+ event.context().toString()));
					
					fileModified = true;
					fileModifiedList.push(event.context().toString());
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
	private static Entry<String, File> searchForFile(String fileName){
		
		 try {
			String currentFileMD5 = File.getMD5Checksum(listenedPath.toString()+"\\"+fileName);
			for (Entry<String, File> entry : filesAndTheirHistory.entrySet()) {
			    if(currentFileMD5.equals(entry.getValue().getSingleFileHistory().getLast().getMd5())){
			    	return entry;
			    }
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}