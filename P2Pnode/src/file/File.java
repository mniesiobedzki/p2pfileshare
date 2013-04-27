package file;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class File {
	
	// Lista obiektów typu FileState. 
	// Lista ta przechowuje informacje o wszystkich modyfikacjach
	// danego pliku z folderu.
	
	private Map<String, LinkedList<FileState>> filesAndTheirHistory = new HashMap<String, LinkedList<FileState>>();
	
	private LinkedList<FileState> singleFileHistory = new LinkedList<FileState>();

	private String fileHash = "";	// file hash z pakietu P2PP
	
	public String generateFileHash(String filePath){
		
		return "";
	}
	
	public Map<String, LinkedList<FileState>> getFilesAndTheirHistory() {
		return filesAndTheirHistory;
	}

	public void setFilesAndTheirHistory(
			Map<String, LinkedList<FileState>> filesAndTheirHistory) {
		this.filesAndTheirHistory = filesAndTheirHistory;
	}

	public LinkedList<FileState> getSingleFileHistory() {
		return singleFileHistory;
	}

	public void setSingleFileHistory(LinkedList<FileState> singleFileHistory) {
		this.singleFileHistory = singleFileHistory;
	}
}
