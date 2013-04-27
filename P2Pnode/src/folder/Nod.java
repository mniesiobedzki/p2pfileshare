package folder;
import java.util.LinkedList;

import file.FileState;


public class Nod {
	String value;
	LinkedList<Nod> children= new LinkedList<Nod>();
	Nod parent;
	LinkedList<FileState> history = null;

	public Nod(String val, Nod par, String key){
		value=val;
		parent=par;
	}
	public Nod(String val, Nod par, String key, LinkedList<FileState> hist){
		value=val;
		parent=par;
		history=hist;
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
	public void update(FileState state){
		if(!this.history.getLast().equals(state)){
			history.add(state);
		}
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public LinkedList<Nod> getChildren() {
		return children;
	}
	public void setChildren(LinkedList<Nod> children) {
		this.children = children;
	}
	public Nod getParent() {
		return parent;
	}
	public void setParent(Nod parent) {
		this.parent = parent;
	}
	public LinkedList<FileState> getHistory() {
		return history;
	}
	public void setHistory(LinkedList<FileState> history) {
		this.history = history;
	}
}
