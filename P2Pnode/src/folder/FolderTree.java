package folder;

import file.File;
import file.FileClient;
import org.apache.log4j.Logger;
import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncHashMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Klasa przechowująca strukturę plików we współdzielonym folderze.
 *
 * @author Marcin Weiss
 * @version 0.9
 */
public class FolderTree implements Serializable {

    public static final Logger LOG = Logger.getLogger(FolderTree.class);

    /**
     *
     */
    private static final long serialVersionUID = -7730198483613602338L;
    public boolean updated = false;
    public HashMap<String, Nod> folder = new HashMap<String, Nod>();
    public JCSyncHashMap<String, Nod> syncFolder;
    // klucz to dla korzenia "root"
    // dla użytkownika jego ID
    // dla pliku ID użytkownika + ID pliku
    public String localUser;

    /**
     * @param path - ścieżka do folderu ze współdzoelonymi plikami
     * @param usr  - nazwa użytkownika uruchamiającego udział
     * @param tree -
     */
    public FolderTree(String path, String usr, JCSyncHashMap<String, Nod> tree, String ip, int port) {
        LOG.info("FolderTree constructor");
        this.syncFolder = tree;
        LOG.info("Drzewo podpięte");
        this.localUser = usr;
        LOG.info("User dodany");
        LOG.info("Root dodany do drzewa");
        this.folder.put("root", new Nod(path));
        this.addUser(usr, path, ip, port);
        
        if (this.syncFolder != null) {
            LOG.info("pobieram dane do lokalnej struktury");
            this.putAll();
            LOG.info("wkładam lokalne dane do synchronizowanej struktury");
            putAllToSync();
            update();
        } else {
            LOG.info("JCSync jest null-em");
        }
        LOG.info("FolderTree created with path: " + path + " for user: " + usr + " IP:" + ip + ":" + port);
        LOG.info(this);
    }

    public HashMap<String, Nod> getFolder() {
        return folder;
    }

    public void putAllToSync(){
        LOG.info("wszedłem w putAllToSync()");
        LOG.info("syncFolder zawiera "+syncFolder.keySet().toString());
        LOG.info("folder zawiera "+folder.keySet().toString());
    	for (Nod nod : folder.values()) {
            LOG.info("przetwarzam "+nod.getName());
			if(nod.getValue()!=null && nod.getValue().equals("root")){
	            LOG.info("root");
				//synchronized(syncFolder){
					if(syncFolder.get("root")==null){
						syncFolder.put("root", new Nod("root"));
					}
					for (String u : nod.getChildren()) {
						if(!syncFolder.get("root").children.contains(u)){
							syncFolder.get("root").children.add(u);
						}
					}
		            LOG.info("Korzeń zaktualizowano do postaci "+syncFolder.get("root").getChildren());
				//}
			}else{
	            LOG.info("!root ");
				//synchronized(syncFolder){
					if(nod.getHistory()!=null && nod.getHistory().size()>0){
			            LOG.info("dodaje plik ");
			            if(syncFolder.containsKey(nod.getParent()+nod.getName())){
			            	syncFolder.get(nod.getParent()+nod.getName()).history=nod.history;
			            }else{
			            	syncFolder.put(nod.getParent()+nod.getName(), nod);
			            }
			            LOG.info("dodano plik do struktury synchronizowanej "+nod.name);
					}else{
			            LOG.info("dodaje węzeł ");
			            if(syncFolder.containsKey(nod.getName())){
			            	syncFolder.get(nod.getName()).children=nod.children;
			            }else{
							syncFolder.put(nod.getName(), nod);
			            }
			            LOG.info("dodano węzeł do struktury synchronizowanej "+nod.name);
					}
				//}
			}
		}
    }
    /**
     * Metoda dodająca użytkownika
     *
     * @param usr  - ID użytkownika
     * @param path - ścieżka do folderu lokalna dla użytkownika
     */
    public void addUser(String u, String path, String ip, int port) {

    	if(!this.folder.get("root").children.contains(u)){
	        LOG.info("adding new user " + u);
	        Nod n = new Nod(u, folder.get("root"), ip, port);
	        System.out.println(n);
	        System.out.println("Użytkownik :" + u);
	        System.out.println("folder w :" + path);
	        n.setPath(path);
	        folder.put(u, n);
	        
        /*if (this.syncFolder != null) {
        	if(!this.syncFolder.get("root").children.contains(n.name)){
	        	System.out.println("root syncFolder: "+ this.syncFolder.get("root"));
	        	System.out.println("jego dzieci: "+ this.syncFolder.get("root").getChildren());
	        	this.syncFolder.get("root").addChlid(u);
	        	this.syncFolder.put(u, n);
        	}else{
	        	this.syncFolder.put(u, n);
        	}
        }*/

    	}else{

	        LOG.info("updating new user " + u);
	        Nod n = new Nod(u, folder.get("root"), ip, port);
	        System.out.println(n);
	        System.out.println("Użytkownik :" + u);
	        System.out.println("folder w :" + path);
	        n.setPath(path);
	        folder.put(u, n);
	        updated = true;
	        LOG.info("User " +u +" updated");
    	}
    }

    public Nod getRoot() {
        return folder.get("root");
    }

    public void setRoot(Nod root) {
        this.getFolder().put("root", root);
        updated = true;
    }

    @Override
    public String toString() {
        String s = "\nFolderTree [root=" + this.getFolder().get("root").getName() + "]" + "\n";

        for (String k : this.getFolder().get("root").getChildren()) {
            Nod n = this.getFolder().get(k);
            s += "\n\t";
            s += n.name;
            s += " ";
            s += n.ip;
            for (String c : n.getChildren()) {
                if (folder.get(c).getHistory().getLast() != null) {
                    s += "\n\t\t" + folder.get(c).getHistory().getLast().getData();
                } else {
                    s += "\n\t\twykasowany";
                }
                s += "\n\t\t";
                s += c;
            }
        }
        s+="\n\nsyncFolder"+this.syncFolder.keySet().toString()+"\n";
        s+="\n\nfolder"+this.folder.keySet().toString()+"\n";
        return s;
    }

    public void setFolder(JCSyncHashMap<String, Nod> folder) {
        this.folder = folder;
        updated = true;
    }

    public void addFile(File f, String usr) {
        LOG.info("Method addFile(" + f.getFilePath() + "," + usr);
        Nod file = new Nod(usr + f.getFileName(), folder.get(usr), f.getSingleFileHistory(), folder.get(usr), f.getFileName(), f.getFilePath());
        file.setParent(folder.get(usr));
        //synchronized(folder){
        	folder.put(usr + file.getName(), file);
        //}

        if (this.syncFolder != null) {
            putAllToSync();
        }
        System.out.println(this);
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

    public void putAll(HashMap<String, Nod> f) {
        LinkedList<String> usrs = new LinkedList<String>();
        for (Nod n : f.values()) {
            if (f.get("root").getChildren().contains(n.getName())) {
                usrs.add(n.getName());
                folder.put(n.value, n);
                if (!folder.get("root").children.contains(n.getName())) {
                    folder.get("root").children.add(n.getName());
                }
            } else if (!n.getName().equals("root")) {
                addNod(n);
            }
        }
    }

    public void putAll() {
        LinkedList<String> usrs = new LinkedList<String>();
        for (Nod n : this.syncFolder.values()) {
            if (this.syncFolder.get("root").getChildren().contains(n.getName()) || this.folder.get("root").getChildren().contains(n.getName())) {
                usrs.add(n.getName());
                this.addUser(n.name, n.path, n.ip, n.port);
                this.getFolder().get(n.name).children.clear();
                this.getFolder().get(n.name).children.addAll(n.getChildren());
	            LOG.info("pobrano użytkownika "+n.getName()+" i dodano do korzenia ");
            } else if (!n.getValue().equals("root")) {
                addNod(n);
                if(n.getHistory()!=null && n.getHistory().size()>0){
                	LOG.info("pobrano dane o pliku "+ n.getName() + " nalezacym do "+n.getParent());
                }else if(n.getParent().equals("root")){
                	LOG.info("pobrano dane o uzytkowniku "+ n.getName() + " nalezacym do "+n.getParent());
                	folder.get("root").children.add(n.name);
                }else{
                	LOG.info("pobrano dane o wezle "+ n.getName() + " nalezacym do "+n.getParent());
                }
            }else{
            	for(String c: syncFolder.get("root").getChildren()){
            		if(!folder.get("root").children.contains(c)){
            			folder.get("root").addChlid(c);
            		}
            	}
            }
        }
    }

    public void addNod(Nod n) {
        if (!folder.containsKey(n.value)) {
            folder.put(n.value, n);
        } else if (n.getHistory().size() > 0 && n.getHistory().getLast() == null) {
            folder.get(n.getValue()).getHistory().add(null);
        } else if (folder.get(n.value).getHistory().size() > 0 && (folder.get(n.value).getHistory().getLast().getData() < n.getHistory().getLast().getData())) {
            folder.get(n.value).getHistory().add(n.getHistory().getLast());
        }
    }

    /**
     * Aktualizacja drzewa
     *
     * @param folder2
     */
    public void update(HashMap<String, Nod> folder2) {
        this.putAll(folder2);//dodawanie nowych

        LinkedList<String> users = new LinkedList<String>();
        LinkedList<Nod> changes = new LinkedList<Nod>();
        LinkedList<Nod> created = new LinkedList<Nod>();


        Nod rootLocal = folder.get("root");
        System.out.println("\nusrs " + rootLocal.getChildren());
        System.out.println(this);
        //plik zaktualizowano
        for (Nod n : folder.values()) {
            if (!n.getParent().equals(localUser) && folder.get(n.getValue()).getHistory().size() > 0 && folder.get(localUser + n.getName()) != null) {

                if (n.getHistory().getLast() == null && folder.get(localUser + n.getName()).getHistory().getLast() != null) {
                    changes.add(n);
                } else if (folder.get(localUser + n.getName()).getHistory().getLast() == null) {
                    //raz wykasowany jest ignorowany
                } else if (folder.get(localUser + n.getName()).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
                    System.out.println("zmieniono " + n.name);
                    changes.add(n);
                }
            }
        }
        users.addAll(folder.get("root").getChildren());

        //pliki utworzone
        for (Nod n : folder.values()) {
            if (!n.getValue().equals("root") && !users.contains(n.getName())) {
                if (!folder.containsKey(localUser + n.getName())) {
                    System.out.println("stworzono " + n.name);
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
                System.out.println("FolderTree: żądanie pliku " + nod.getParent() + nod.getName());
                System.out.println("FolderTree: czas zmiany zdalnego: " + folder.get(nod.getParent() + nod.getName()).getHistory().getLast().getData());
                System.out.println("przez port: " + folder.get(nod.getParent()).port);
                @SuppressWarnings("unused")
                FileClient fileClient = new FileClient(this, nod, folder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(localUser).getPath(), nod.getName(), localUser, folder.get(nod.getParent()).port);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(localUser).getPath() + nod.getName());
                if (this.getFolder().containsKey(localUser + nod.getName())) {
                    this.getFolder().get(localUser + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), nod.getPath(), localUser);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, localUser);
                }
            }
        }
        for (Nod nod : changes) {
            if (nod.getHistory().getLast() != null) {
                System.out.println("FolderTree: żądanie pliku " + nod.getParent() + nod.getName());
                System.out.println("FolderTree: czas zmiany zdalnego: " + folder.get(nod.getParent() + nod.getName()).getHistory().getLast().getData());
                System.out.println("przez port: " + folder.get(nod.getParent()).port);
                System.err.println(this);
                System.err.println(nod);
                System.err.println(folder.get(nod.getParent()).ip);
                System.err.println(nod.getParent() + nod.getName());
                System.err.println(folder.get(localUser).getPath());
                System.err.println(nod.getName());
                System.err.println(localUser);
                System.err.println(folder.get(nod.parent).getPort());
                @SuppressWarnings("unused")
                FileClient fileClient = new FileClient(this, nod, folder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(localUser).getPath(), nod.getName(), localUser, folder.get(nod.getParent()).port);
            } else {
                //kod kasujący plik
                System.out.println("Kasuje plik: " + nod.name);
                System.out.println("Ścieżka: " + folder.get(localUser).getPath() + System.getProperty("file.separator") + nod.getName());
                MFolderListener.deleteFileFromDisc(folder.get(localUser).getPath() + System.getProperty("file.separator") + nod.getName());
                if (this.getFolder().containsKey(localUser + nod.getName())) {
                    this.getFolder().get(localUser + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), nod.getPath(), localUser);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, localUser);
                }
            }
        }
    }

    public void update() {
    	System.out.println("userzy z jcsync "+syncFolder.get("root").getChildren());
    	synchronized(folder){
    		//synchronized(syncFolder){
    			this.putAll();//dodawanie nowych
    		//}
        LinkedList<String> users = new LinkedList<String>();
        LinkedList<Nod> changes = new LinkedList<Nod>();
        LinkedList<Nod> created = new LinkedList<Nod>();

        Nod rootLocal = folder.get("root");
        System.out.println("\nusrs " + rootLocal.getChildren());
        System.out.println(this);
        //plik zaktualizowano
        for (Nod n : this.folder.values()) {
            if (!n.getParent().equals(localUser) && folder.get(n.getValue()).getHistory().size() > 0 && folder.get(localUser + n.getName()) != null) {
                if (n.getHistory().getLast() == null && folder.get(localUser + n.getName()).getHistory().getLast() != null) {
                    changes.add(n);
                } else if (folder.get(localUser + n.getName()).getHistory().getLast() == null) {
                    //raz wykasowany jest ignorowany
                } else if (folder.get(localUser + n.getName()).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
                    System.out.println("zmieniono " + n.name);
                    changes.add(n);
                }
            }
        }
        users.addAll(folder.get("root").getChildren());
        System.out.println("jcsynctree: "+syncFolder.keySet());
        System.out.println("usrs jcsync: " + folder.get("root").getChildren());
        System.out.println("usrs: " + folder.get("root").getChildren());
        //pliki utworzone
        for (Nod n : this.folder.values()) {
            if (!n.getValue().equals("root") && !users.contains(n.getName())) {
                if (!folder.containsKey(localUser + n.getName())) {
                    System.out.println("stworzono " + n.name);
                    System.out.println(n.getPath());
                    System.out.println(n.getValue());
                    System.out.println(n.getPort());
                    System.out.println(n.getIp());
                    created.add(n);
                }
            }
        }

        for (Nod nod : created) {
            //if (nod.getHistory().getLast() != null) {
        	System.out.println("przerabiam nod "+nod.name);
            if (nod.getHistory()!=null && nod.getHistory().getLast() != null) {
                System.out.println("FolderTree: rządanie pliku " + nod.getParent() + nod.getName());
                System.out.println("FolderTree: czas zmiany zdalneg: " + folder.get(nod.getParent() + nod.getName()).getHistory().getLast().getData());
                @SuppressWarnings("unused")
                FileClient fileClient = new FileClient(this, nod, this.folder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(localUser).getPath(), nod.getName(), localUser, folder.get(nod.getParent()).port);
            } else{
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(localUser).getPath() + nod.getName());
                if (this.getFolder().containsKey(localUser + nod.getName())) {
                    this.getFolder().get(localUser + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), nod.getPath(), localUser);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, localUser);
                }
            }
        }
        
        for (Nod nod : changes) {
            if (nod.getHistory().getLast() != null) {
                System.out.println("FolderTree: żądanie pliku " + nod.getParent() + nod.getName());
                System.out.println("FolderTree: czas zmiany zdalnego: " + folder.get(nod.getParent() + nod.getName()).getHistory().getLast().getData());
                System.out.println("przez port: " + folder.get(nod.getParent()).port);
                System.err.println(this);
                System.err.println(nod);
                System.err.println(folder.get(nod.getParent()).ip);
                System.err.println(nod.getParent() + nod.getName());
                System.err.println(folder.get(localUser).getPath());
                System.err.println(nod.getName());
                System.err.println(localUser);
                System.err.println(folder.get(nod.parent).getPort());
                @SuppressWarnings("unused")
                FileClient fileClient = new FileClient(this, nod, folder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(localUser).getPath(), nod.getName(), localUser, folder.get(nod.getParent()).port);
            } else {
                //kod kasujący plik
                System.out.println("Kasuje plik: " + nod.name);
                System.out.println("Ścieżka: " + folder.get(localUser).getPath() + System.getProperty("file.separator") + nod.getName());
                MFolderListener.deleteFileFromDisc(folder.get(localUser).getPath() + System.getProperty("file.separator") + nod.getName());
                if (this.getFolder().containsKey(localUser + nod.getName())) {
                    this.getFolder().get(localUser + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), nod.getPath(), localUser);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, localUser);
                }
            }
        }
        putAllToSync();
        

    	}
    }
}
