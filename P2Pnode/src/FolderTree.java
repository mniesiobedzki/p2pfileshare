import pl.edu.pjwstk.mteam.jcsync.core.implementation.collections.JCSyncTreeMap;


public class FolderTree {
	public static JCSyncTreeMap<String, Nod> folder = new JCSyncTreeMap<String, Nod>();
	public static JCSyncTreeMap<String, Nod> folderOld = new JCSyncTreeMap<String, Nod>();
	public static Nod root;
public FolderTree(String path){
	root=new Nod(path, null);
}
public JCSyncTreeMap<String, Nod> getFolder(){
	return folder;
}
	
}
