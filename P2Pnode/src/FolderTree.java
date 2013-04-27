import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;


public class FolderTree {
	public static JCSyncTreeMap<String, Nod> folder = new JCSyncTreeMap<String, Nod>();
	public static JCSyncTreeMap<String, Nod> folderOld = new JCSyncTreeMap<String, Nod>();
	public static Nod root;
public FolderTree(String path){
	root=new Nod( path, null, "root");
}
public JCSyncTreeMap<String, Nod> getFolder(){
	return folder;
}
public void addUser(String usr){
	new Nod( usr, FolderTree.root, usr);
}
public void addFile(File f, String usr){
	
}
public void updateFile(File f, String usr){
	
}
}
