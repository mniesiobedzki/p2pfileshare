package folder;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;
import file.File;
import file.FileClient;

public class FolderTree implements Serializable {

	public TreeMap<String, Nod> folder = new TreeMap<String, Nod>();
	public JCSyncTreeMap<String, Nod> syncFolder = new JCSyncTreeMap<String, Nod>();
	// klucz to dla korzenia "root"
	// dla użytkownika jego ID
	// dla pliku ID użytkownika + ID pliku
	public Nod root;// nazwa folderu albo lokalizacja url
	public String usr;
	
	public FolderTree(String path,String usr, JCSyncTreeMap<String, Nod> tree) {
		root = new Nod(path);
		syncFolder = tree;
		this.usr=usr;
		this.folder.put("root", root);
		if(syncFolder!=null){
			this.syncFolder.put("root", root);
		}
	}

	public TreeMap<String, Nod> getFolder() {
		return folder;
	}

	/**
	 * Metoda dodająca użytkownika
	 * 
	 * @param usr - ID użytkownika
	 * @param path - ścieżka do folderu lokalna dla użytkownika
	 */
	public void addUser(String usr, String path, String ip) {
		Nod n = new Nod(usr, root, ip);
		System.out.println(n);
		System.out.println("Użytkownik :"+usr);
		System.out.println("folder w :"+path);
		n.setPath(path);
		folder.put(usr, n);

		if(syncFolder!=null){
			syncFolder.put(usr, n);
		}
	}

	public Nod getRoot() {
		return root;
	}

	public void setRoot(Nod root) {
		this.root = root;
	}

	@Override
	public String toString() {
		String s = "FolderTree [root=" + root.getName() + "]"+"\n";
		
		for (String k: root.getChildren()){
			Nod n = this.getFolder().get(k);
			s+= "\n\t";
			s+=n.name;
			for(String c: n.getChildren()){
				s+="\n\t\t";
				s+=c;
			}
		}
		return s;
	}

	public void setFolder(JCSyncTreeMap<String, Nod> folder) {
		this.folder = folder;
	}

	public void addFile(File f, String usr) {
		Nod file = new Nod(usr+f.getFileName(), folder.get(usr),f.getSingleFileHistory(), folder.get(usr), f.getFileName());
		file.setParent(folder.get(usr));
		folder.put(usr+file.getName(),file);

		if(syncFolder!=null){
			syncFolder.put(usr+file.getName(),file);
		}
	}

	/**
	 * Metoda aktualizująca historie pliku o najnowszy wpis z listy lokalnej
	 * 
	 * @param f
	 * @param usr - ID użytkownika
	 */
	public void updateFile(File f, String usr) {
		Nod file = folder.get(usr + f.getFileName());
		if (file == null) {
			addFile(f, usr);
		} else if(file.history.getLast().getData()<f.getSingleFileHistory().getLast().getData()){
			file.history.add(f.getSingleFileHistory().getLast());
		}
	}

	/**
	 * Metoda przechodzi po liscie lokalnych plików i ich historii porównując je
	 * z plikami trzymanymi w drzewie synchronizowanym przez JCSync
	 * 
	 * @param usr - ID Użytkownika
	 */
	public void updateAll(String usr) {
		for (File f : MFolderListener.filesAndTheirHistory.values()) {
			if (!f.getSingleFileHistory().getLast().equals(this.folder.get(usr+f.getFileName()))) {
				this.updateFile(f, usr);
			}
		}
	}

	/**
	 * Aktualizacja drzewa
	 * 
	 * @param folder2
	 */
	public void update(TreeMap<String, Nod> folder2) {
		folder.putAll(folder2);
		LinkedList<String> users = new LinkedList<String>();
		root = folder.get("root");
		for (String u : root.children) {
			if(!this.folder.containsKey(u)){
				this.folder.put(u, folder.get(u));
			}
			users.add(folder.get(u).getValue());
		}
		LinkedList<Nod> conflicts = new LinkedList<Nod>();
		for (String u : users) {
			if (!u.equals(usr)) {
				for (File f : MFolderListener.filesAndTheirHistory.values()) {					
					 
					System.out.println(folder.get(u + f.getFileName()).getHistory().getLast().getData());
					if (MFolderListener.filesAndTheirHistory.get(f.getFileId())
							.getSingleFileHistory().getLast().getData() < folder
							.get(u + f.getFileName()).getHistory().getLast()
							.getData()) {
						folder.get(u + f.getFileName()).setParent(u);
						conflicts.add(folder.get(u + f.getFileName()));
					}

				}
			}
		}
		for (Nod n : folder.values()) {
			if (n.getHistory().size() > 0) {
				if (!MFolderListener.filesAndTheirHistory.containsKey(n.getParent()+n.getName())) {
					conflicts.add(folder.get(n.getOwner().value + n.name));
				}
			}
		}
		System.out.println("conflicts:");
		for (Nod nod : conflicts) {
			System.out.println(nod);
			//System.out.println(nod.getParent());
			System.out.println("\t"+nod.getName());
			System.out.println("\t"+nod.getValue()+"\n");
			System.out.println("parent: "+nod.getParent());
			System.out.println("Parent obj: "+folder2.get(nod.getParent()));
			System.out.println(folder2.get(nod.getParent()).ip);
			FileClient fileClient = new FileClient(folder2.get(nod.getParent()).ip, nod.getParent()+nod.getName(), folder.get(usr).getPath(), nod.getName());
			File f = new File(nod.getName(),usr);
			f.setFileId(nod.getValue());
			this.addFile(f, usr);
			
		}
		// tu dodać kod wyłapujący zmiany wymagające dodania
	}
	
	public void update() {
		folder.putAll(syncFolder);
		LinkedList<String> users = new LinkedList<String>();
		root = folder.get("root");
		for (String u : root.children) {
			if(!this.folder.containsKey(u)){
				this.folder.put(u, folder.get(u));
			}
			users.add(folder.get(u).getValue());
		}
		LinkedList<Nod> conflicts = new LinkedList<Nod>();
		for (String u : users) {
			if (!u.equals(usr)) {
				for (File f : MFolderListener.filesAndTheirHistory.values()) {					
					 
					System.out.println(folder.get(u + f.getFileName()).getHistory().getLast().getData());
					if (MFolderListener.filesAndTheirHistory.get(f.getFileId())
							.getSingleFileHistory().getLast().getData() < folder
							.get(u + f.getFileName()).getHistory().getLast()
							.getData()) {
						folder.get(u + f.getFileName()).setParent(u);
						conflicts.add(folder.get(u + f.getFileName()));
					}

				}
			}
		}
		for (Nod n : folder.values()) {
			if (n.getHistory().size() > 0) {
				if (!MFolderListener.filesAndTheirHistory.containsKey(n.getParent()+n.getName())) {
					conflicts.add(folder.get(n.getOwner().value + n.name));
				}
			}
		}
		System.out.println("conflicts:");
		for (Nod nod : conflicts) {
			System.out.println(nod);
			//System.out.println(nod.getParent());
			System.out.println("\t"+nod.getName());
			System.out.println("\t"+nod.getValue()+"\n");
			System.out.println("parent: "+nod.getParent());
			System.out.println("Parent obj: "+folder.get(nod.getParent()));
			System.out.println(folder.get(nod.getParent()).ip);
			FileClient fileClient = new FileClient(folder.get(nod.getParent()).ip, nod.getParent()+nod.getName(), folder.get(usr).getPath(), nod.getName());
			File f = new File(nod.getName(),usr);
			f.setFileId(nod.getValue());
			this.addFile(f, usr);
			
		}
		// tu dodać kod wyłapujący zmiany wymagające dodania
	}
}
