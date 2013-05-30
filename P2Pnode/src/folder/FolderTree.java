package folder;

import file.File;
import file.FileClient;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Klasa przechowująca strukturę plików we współdzielonym folderze.
 *
 * @author Marcin Weiss
 * @version 0.9
 */
public class FolderTree implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7730198483613602338L;
	public boolean updated = false;
    public TreeMap<String, Nod> folder = new TreeMap<String, Nod>();
    public JCSyncTreeMap<String, Nod> syncFolder = new JCSyncTreeMap<String, Nod>();
    // klucz to dla korzenia "root"
    // dla użytkownika jego ID
    // dla pliku ID użytkownika + ID pliku
    public String usr;

    /**
     * @param path - ścieżka do folderu ze współdzoelonymi plikami
     * @param usr  - nazwa użytkownika uruchamiającego udział
     * @param tree -
     */
    public FolderTree(String path, String usr, JCSyncTreeMap<String, Nod> tree, String ip, int port) {
        syncFolder = tree;
        this.usr = usr;
        this.folder.put("root", new Nod(path));
        if (syncFolder != null) {
            syncFolder.put("root", folder.get("root"));
            new Thread(new FolderServer(this, usr, path)).start();
        }
        this.addUser(usr, path, ip, port);
    }

    public TreeMap<String, Nod> getFolder() {
        return folder;
    }

    /**
     * Metoda dodająca użytkownika
     *
     * @param usr  - ID użytkownika
     * @param path - ścieżka do folderu lokalna dla użytkownika
     */
    public void addUser(String usr, String path, String ip, int port) {
        Nod n = new Nod(usr, folder.get("root"), ip, port);
        System.out.println(n);
        System.out.println("Użytkownik :" + usr);
        System.out.println("folder w :" + path);
        n.setPath(path);
        folder.put(usr, n);

        if (syncFolder != null) {
            syncFolder.put(usr, n);
        }
        updated = true;
    }

    public Nod getRoot() {
        return folder.get("root");
    }

    public void setRoot(Nod root) {
    	this.getFolder().put("root",root);
        updated = true;
    }

    @Override
    public String toString() {
        String s = "FolderTree [root=" + this.getFolder().get("root").getName() + "]" + "\n";

        for (String k : this.getFolder().get("root").getChildren()) {
            Nod n = this.getFolder().get(k);
            s += "\n\t";
            s += n.name;
            for (String c : n.getChildren()) {
            	s += "\n\t\t"+folder.get(c).getHistory().getLast().getData();
                s += "\n\t\t";
                s += c;
            }
        }
        return s;
    }

    public void setFolder(JCSyncTreeMap<String, Nod> folder) {
        this.folder = folder;
        updated = true;
    }

    public void addFile(File f, String usr) {
        Nod file = new Nod(usr + f.getFileName(), folder.get(usr), f.getSingleFileHistory(), folder.get(usr), f.getFileName(), f.getFilePath());
        file.setParent(folder.get(usr));
        folder.put(usr + file.getName(), file);

        if (syncFolder != null) {
        	System.out.println("Dodaje do drzewaJCsync: "+usr + file.getName());
            syncFolder.put(usr + file.getName(), file);
        }
        updated = true;
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
        } else if (file.history.getLast().getData() < f.getSingleFileHistory().getLast().getData()) {
            file.history.add(f.getSingleFileHistory().getLast());
        }
        updated = true;
    }

    /**
     * Metoda przechodzi po liscie lokalnych plików i ich historii porównując je
     * z plikami trzymanymi w drzewie synchronizowanym przez JCSync
     *
     * @param usr - ID Użytkownika
     */
    public void updateAll(String usr) {
        for (File f : MFolderListener.filesAndTheirHistory.values()) {
            if (!f.getSingleFileHistory().getLast().equals(this.folder.get(usr + f.getFileName()))) {
                this.updateFile(f, usr);
            }
        }
    }

    public void putAll(TreeMap<String, Nod> f) {
    	LinkedList<String> usrs= new LinkedList<String>();
        for (Nod n : f.values()) {
        	if(f.get("root").getChildren().contains(n.getName())){
        		usrs.add(n.getName());
        		folder.put(n.value, n);
        		if(!folder.get("root").children.contains(n.getName())){
        			folder.get("root").children.add(n.getName());
        		}
        	}else if(!n.getName().equals("root")){
        		addNod(n);
        	}
        }
    }

    public void putAll() {
    	LinkedList<String> usrs= new LinkedList<String>();
        for (Nod n : syncFolder.values()) {
        	if(syncFolder.get("root").getChildren().contains(n.getName())){
        		usrs.add(n.getName());
        		folder.put(n.value, n);
        		if(!folder.get("root").children.contains(n.getName())){
        			folder.get("root").children.add(n.getName());
        		}
        	}else if(!n.getName().equals("root")){
        		addNod(n);
        	}
        }
    }

    public void addNod(Nod n) {
        if (!folder.containsKey(n.value)) {
            folder.put(n.value, n);
        } else if (folder.get(n.value).getHistory().size()>0 && (folder.get(n.value).getHistory().getLast().getData() < n.getHistory().getLast().getData())) {
            folder.get(n.value).getHistory().add(n.getHistory().getLast());
        }
    }

    /**
     * Aktualizacja drzewa
     *
     * @param folder2
     */
    public void update(TreeMap<String, Nod> folder2) {
        this.putAll(folder2);//dodawanie nowych

        LinkedList<String> users = new LinkedList<String>();
        LinkedList<Nod> changes = new LinkedList<Nod>();
        LinkedList<Nod> created = new LinkedList<Nod>();


        Nod rootLocal = folder.get("root");        
        System.out.println("\nusrs "+ rootLocal.getChildren());
        System.out.println(this);
        //plik zaktualizowano
        for (Nod n : folder2.values()) {
            if (folder.get(n.getValue()).getHistory().size()>0){
        		System.out.println(n.getHistory().getLast());
        		System.out.println("data sprawdzanego: "+n.getHistory().getLast().getData());
        		System.out.println("data zewnetrznego: "+folder.get(n.getValue()).getHistory().getLast().getData());
            	if(folder.get(n.getValue()).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
            		System.out.println("zmieniono "+ n.name);
            		changes.add(n);
            	}
            }
        }
        users.addAll(folder.get("root").getChildren());
        
        //pliki utworzone
        for (Nod n : folder2.values()) {
            if (!n.getValue().equals("root") && !users.contains(n.getName())) {
                if (!folder.containsKey(usr+n.getName())) {
                	System.out.println("stworzono "+ n.name);
                	System.out.println(n.getPath());
                	System.out.println(n.getValue());
                	System.out.println(n.getPort());
                	System.out.println(n.getIp());
                    created.add(n);
                }
            }
        }
        for (Nod nod : created) {
            if (nod.getHistory().getLast() != null) {
            	System.out.println("FolderTree: żądanie pliku "+  nod.getParent() + nod.getName());
            	System.out.println("FolderTree: czas zmiany zdalnego: "+  folder.get(nod.getParent() + nod.getName()).getHistory().getLast().getData());
            	System.out.println("przez port: "+  folder.get(nod.getParent()).port);
                FileClient fileClient = new FileClient(this, nod, folder2.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr, folder.get(nod.getParent()).port);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(),nod.getPath(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
        for (Nod nod : changes) {
            if (nod.getHistory().getLast() != null) {
                FileClient fileClient = new FileClient(this, nod, folder2.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr, folder.get(nod.getName()).port);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(),nod.getPath(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
    }

    public void update() {
        this.putAll(syncFolder);//dodawanie nowych

        LinkedList<String> users = new LinkedList<String>();
        LinkedList<Nod> changes = new LinkedList<Nod>();
        LinkedList<Nod> created = new LinkedList<Nod>();


        Nod rootLocal = folder.get("root");        
        System.out.println("\nusrs "+ rootLocal.getChildren());
        System.out.println(this);
        //plik zaktualizowano
        for (Nod n : syncFolder.values()) {
            if (folder.get(n.getValue()).getHistory().size()>0 && folder.get(n.getValue()).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
            	System.out.println("zmieniono "+ n.name);
            	changes.add(n);
            }
        }
        users.addAll(folder.get("root").getChildren());
        System.out.println("usrs: "+users);
        System.out.println("usrs jcsync: "+syncFolder.get("root").getChildren());
        System.out.println("usrs: "+folder.get("root").getChildren());
        //pliki utworzone
        for (Nod n : syncFolder.values()) {
            if (!n.getValue().equals("root") && !users.contains(n.getName())) {
                if (!folder.containsKey(usr+n.getName())) {
                	System.out.println("stworzono "+ n.name);
                	System.out.println(n.getPath());
                	System.out.println(n.getValue());
                	System.out.println(n.getPort());
                	System.out.println(n.getIp());
                    created.add(n);
                }
            }
        }
        
        for (Nod nod : created) {
            if (nod.getHistory().getLast() != null) {
            	System.out.println("FolderTree: rządanie pliku "+  nod.getParent() + nod.getName());
            	System.out.println("FolderTree: czas zmiany zdalneg: "+  folder.get(nod.getParent() + nod.getName()).getHistory().getLast().getData());
                FileClient fileClient = new FileClient(this, nod, syncFolder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr, folder.get(nod.getName()).port);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(),nod.getPath(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
        for (Nod nod : changes) {
            if (nod.getHistory().getLast() != null) {
                FileClient fileClient = new FileClient(this, nod, syncFolder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr, folder.get(nod.getName()).port);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(),nod.getPath(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
        this.syncFolder.putAll(folder);
    }
}
