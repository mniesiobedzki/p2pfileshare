import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


public class File {
	
	// Lista obiektów typu FileState. 
	// Lista ta przechowuje informacje o wszystkich modyfikacjach
	// danego pliku z folderu.
	
	Map<String, LinkedList<FileState>> filesAndTheirHistory = new HashMap<String, LinkedList<FileState>>();
	
	LinkedList<FileState> fileHistory = new LinkedList<FileState>();
	
	private String fileHash = "";	// file hash z pakietu P2PP
	
	public String generateFileHash(String filePath){
		
		return "";
	}
}
