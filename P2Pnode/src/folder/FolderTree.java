package folder;
import java.util.LinkedList;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import file.File;
public class FolderTree {
	public JCSyncTreeMap<String, Nod> folder = new JCSyncTreeMap<String, Nod>();
	//klucz to dla korzenia "root"
	// dla użytkownika jego ID
	//dla pliku ID użytkownika + ID pliku
	public Nod root;// nazwa folderu albo lokalizacja url
	
	public FolderTree(String path) {
		root = new Nod(path, null, "root");
	}
	
	public JCSyncTreeMap<String, Nod> getFolder() {
		return folder;
	}
	/**
	 * Metoda dodająca użytkownika
	 * @param usr - ID użytkownika
	 */
	public void addUser(String usr) {
		Nod n = new Nod(usr, root, usr);
		folder.put(usr, n);
	}

	public Nod getRoot() {
		return root;
	}

	public void setRoot(Nod root) {
		this.root = root;
	}

	public void setFolder(JCSyncTreeMap<String, Nod> folder) {
		this.folder = folder;
	}

	public void addFile(File f, String usr) {
		Nod file = new Nod(usr+f.getFileId(),folder.get(usr),f.getFileId(),f.getSingleFileHistory(), folder.get(usr));
		file.setParent(folder.get(usr));
	}
	/**
	 * Metoda aktualizująca historie pliku 
	 * o najnowszy wpis z listy lokalnej
	 * @param f
	 * @param usr - ID użytkownika
	 */
	public void updateFile(File f, String usr) {
		Nod file = folder.get(usr+f.getFileId());
		if(file==null){
			addFile(f,usr);
		}else{
			file.history.add(f.getSingleFileHistory().getLast());
		}
	}
	/**
	 * Metoda przechodzi po liscie lokalnych plików i ich historii 
	 * porównując je z plikami trzymanymi w drzewie synchronizowanym 
	 * przez JCSync
	 * @param usr - ID Użytkownika
	 */
	public void updateAll(String usr){
		for(File f: File.filesAndTheirHistory.values()){
			if(!f.getSingleFileHistory().getLast().equals(this.folder.get(f.getFileId()))){
				this.updateFile(f, usr);
			}
		}
	}
	/**
	 * Aktualizacja drzewa
	 * @param folder2
	 */
	public void update(JCSyncTreeMap<String, Nod> folder2, String usr){
		folder.putAll(folder2);
		LinkedList<String> users = new LinkedList<String>();
		root=folder.get("root");
		for(String u: root.children){
			users.add(folder.get(u).getValue());
		}
		LinkedList<Nod> conflicts = new LinkedList<Nod>();
		for(String u: users){
			if(!u.equals(usr)){
				for(File f:File.filesAndTheirHistory.values()){
					if(File.filesAndTheirHistory.get(f.getFileId()).getSingleFileHistory().getLast().getData()<folder.get(usr+f.getFileId()).getHistory().getLast().getData()){
						conflicts.add(folder.get(usr+f.getFileId()));
					}
					
				}
			}
		}
		for(Nod n: folder.values()){
			if(n.getHistory().size()>0){
				if(!File.filesAndTheirHistory.containsKey(n.getValue())){
					conflicts.add(folder.get(n.getOwner().value+n.getValue()));
				}
			}
		}
		//tu dodać kod wyłapujący zmiany wymagające dodania
	}
}
