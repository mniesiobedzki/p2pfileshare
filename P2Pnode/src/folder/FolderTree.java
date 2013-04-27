package folder;
import java.util.LinkedList;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import file.File;
import file.FileState;
public class FolderTree {
	public JCSyncTreeMap<String, Nod> folder = new JCSyncTreeMap<String, Nod>();
	public JCSyncTreeMap<String, Nod> folderOld = new JCSyncTreeMap<String, Nod>();
	public Nod root;

	public FolderTree(String path) {
		root = new Nod(path, null, "root");
	}

	public JCSyncTreeMap<String, Nod> getFolder() {
		return folder;
	}

	public void addUser(String usr) {
		Nod n = new Nod(usr, root, usr);
		folder.put(usr, n);
	}

	public JCSyncTreeMap<String, Nod> getFolderOld() {
		return folderOld;
	}

	public void setFolderOld(JCSyncTreeMap<String, Nod> folderOld) {
		this.folderOld = folderOld;
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

	public void updateFile(File f, String usr) {
		Nod file = folder.get(usr+f.getFileHash());
		if(file==null){
			addFile(f,usr);
		}else{
			file.history.add(f.getSingleFileHistory().getLast());
		}
	}
	
	public void updateAll(String usr){
		for(File f: File.filesAndTheirHistory.values()){
			if(!f.getSingleFileHistory().getLast().equals(this.folder.get(f.getFileHash()))){
				this.updateFile(f, usr);
			}
		}
	}
}
