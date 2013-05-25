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

    public boolean updated = false;
    public TreeMap<String, Nod> folder = new TreeMap<String, Nod>();
    public JCSyncTreeMap<String, Nod> syncFolder = new JCSyncTreeMap<String, Nod>();
    // klucz to dla korzenia "root"
    // dla użytkownika jego ID
    // dla pliku ID użytkownika + ID pliku
    public Nod root;// nazwa folderu albo lokalizacja url
    public String usr;

    /**
     * @param path - ścieżka do folderu ze współdzoelonymi plikami
     * @param usr  - nazwa użytkownika uruchamiającego udział
     * @param tree -
     */
    public FolderTree(String path, String usr, JCSyncTreeMap<String, Nod> tree) {
        root = new Nod(path);
        syncFolder = tree;
        this.usr = usr;
        this.folder.put("root", root);
        if (syncFolder != null) {
            this.syncFolder.put("root", root);
        }
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
        Nod n = new Nod(usr, root, ip, port);
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
        return root;
    }

    public void setRoot(Nod root) {
        this.root = root;
        updated = true;
    }

    @Override
    public String toString() {
        String s = "FolderTree [root=" + root.getName() + "]" + "\n";

        for (String k : root.getChildren()) {
            Nod n = this.getFolder().get(k);
            s += "\n\t";
            s += n.name;
            for (String c : n.getChildren()) {
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
        Nod file = new Nod(usr + f.getFileName(), folder.get(usr), f.getSingleFileHistory(), folder.get(usr), f.getFileName());
        file.setParent(folder.get(usr));
        folder.put(usr + file.getName(), file);

        if (syncFolder != null) {
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
        for (Nod n : f.values()) {
            addNod(n);
        }
    }

    public void putAll() {
        for (Nod n : syncFolder.values()) {
            addNod(n);
        }
    }

    public void addNod(Nod n) {
        if (!folder.containsKey(n.value)) {
            folder.put(n.value, n);
        } else if (folder.get(n.value).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
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

        root = folder.get("root");
        for (String u : root.children) {
            if (!this.folder.containsKey(u)) {
                this.folder.put(u, folder.get(u));
            }
            users.add(folder.get(u).getValue());
        }
        //plik zaktualizowano
        for (Nod n : folder2.values()) {
            if (folder.get(n.getValue()).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
                changes.add(n);
            }
        }
        //pliki utworzone
        for (Nod n : folder2.values()) {
            if (n.getValue() != "root" && !users.contains(n.getValue())) {
                if (!folder.containsKey(n.getName())) {
                    changes.add(n);
                }
            }
        }
        for (Nod nod : created) {
            if (nod.getHistory().getLast() != null) {
                FileClient fileClient = new FileClient(this, nod, folder2.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
        for (Nod nod : changes) {
            if (nod.getHistory().getLast() != null) {
                FileClient fileClient = new FileClient(this, nod, folder2.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
    }

    public void update() {
        this.putAll();//dodawanie nowych
        LinkedList<String> users = new LinkedList<String>();
        LinkedList<Nod> changes = new LinkedList<Nod>();
        LinkedList<Nod> created = new LinkedList<Nod>();

        root = folder.get("root");
        for (String u : root.children) {
            if (!this.folder.containsKey(u)) {
                this.folder.put(u, folder.get(u));
            }
            users.add(folder.get(u).getValue());
        }
        //plik zaktualizowano
        for (Nod n : syncFolder.values()) {
            if (folder.get(n.getValue()).getHistory().getLast().getData() < n.getHistory().getLast().getData()) {
                changes.add(n);
            }
        }
        //pliki utworzone
        for (Nod n : syncFolder.values()) {
            if (n.getValue() != "root" && !users.contains(n.getValue())) {
                if (!folder.containsKey(n.getName())) {
                    changes.add(n);
                }
            }
        }
        for (Nod nod : created) {
            if (nod.getHistory().getLast() != null) {
                FileClient fileClient = new FileClient(this, nod, folder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
        for (Nod nod : changes) {
            if (nod.getHistory().getLast() != null) {
                FileClient fileClient = new FileClient(this, nod, folder.get(nod.getParent()).ip, nod.getParent() + nod.getName(), folder.get(usr).getPath(), nod.getName(), usr);
            } else {
                //kod kasujący plik
                MFolderListener.deleteFileFromDisc(folder.get(usr).getPath() + nod.getName());
                if (this.getFolder().containsKey(usr + nod.getName())) {
                    this.getFolder().get(usr + nod.getName()).history.add(null);
                    this.updated = true;
                } else {
                    File deletedFile = new File(nod.getName(), usr);
                    deletedFile.setFileId(nod.history.getFirst().getFileId());
                    addFile(deletedFile, usr);
                }
            }
        }
    }
}
