package folder;
import java.util.LinkedList;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import file.File;
import file.FileState;
public class FolderTree {
	public JCSyncTreeMap<String, Nod> folder = new JCSyncTreeMap<String, Nod>();
	public Nod root;

	public FolderTree(String path) {
		root = new Nod(path, null, "root");
	}
	
	public JCSyncTreeMap<String, Nod> getFolder() {
		return folder;
	}
	/**
	 * Metoda dodaj¹ca u¿ytkownika
	 * @param usr
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
		Nod file = new Nod(usr+f.getFileHash(),folder.get(usr),f.getFileHash(),f.getSingleFileHistory());
		file.setParent(folder.get(usr));
	}
	/**
	 * Metoda aktualizuj¹ca historie pliku 
	 * o najnowszy wpis z listy lokalnej
	 * @param f
	 * @param usr
	 */
	public void updateFile(File f, String usr) {
		Nod file = folder.get(usr+f.getFileHash());
		if(file==null){
			addFile(f,usr);
		}else{
			file.history.add(f.getSingleFileHistory().getLast());
		}
	}
	/**
	 * Metoda przechodzi po liscie lokalnych plików i ich historii 
	 * porównuj¹c je z plikami trzymanymi w drzewie synchronizowanym 
	 * przez JCSync
	 * @param usr
	 */
	public void updateAll(String usr){
		for(File f: File.filesAndTheirHistory.values()){
			if(!f.getSingleFileHistory().getLast().equals(this.folder.get(f.getFileHash()))){
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
		for(Nod u: root.children){
			users.add(u.getValue());
		}
		LinkedList<Nod> conflicts = new LinkedList<Nod>();
		for(String u: users){
			if(!u.equals(usr)){
				for(File f:File.filesAndTheirHistory.values()){
					if(File.filesAndTheirHistory.get(f.getFileHash()).getSingleFileHistory().getLast().getData()<folder.get(usr+f.getFileHash()).getHistory().getLast().getData()){
						conflicts.add(folder.get(usr+f.getFileHash()));
					}
					
				}
			}
		}
		for(Nod n: folder.values()){
			if(n.getHistory()!=null){
				
			}
		}
	}
}
