import java.util.LinkedList;


public class Nod {
	String value;
	LinkedList<Nod> children= new LinkedList<Nod>();
	Nod parent;
	
	public Nod(String val, Nod par, String key){
		value=val;
		parent=par;
		FolderTree.folder.put(key, this);
	}
	public void addChlid(Nod ch){
		if(!children.contains(ch)){
			children.add(ch);
		}
	}
	public void removeChild(Nod ch){
		if(!children.contains(ch)){
			children.remove(ch);
		}
	}
}
