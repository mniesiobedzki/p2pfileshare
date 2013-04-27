package folder;
import java.util.LinkedList;

import file.FileState;


public class Nod {
	String value;
	LinkedList<String> children= new LinkedList<String>();
	String parent;
	Nod owner;
	LinkedList<FileState> history = new LinkedList<FileState>();

	public Nod(String val, Nod par, String key){
		value=val;
		parent=par.getValue();
		if(parent!=null){
			par.addChlid(this.value);
		}
	}
	public Nod(String val, Nod par, String key, LinkedList<FileState> hist, Nod own){
		value=val;
		parent=par.getValue();
		if(parent!=null){
			par.addChlid(this.value);
		}
		history=hist;
		owner=own;
	}
	public void addChlid(String ch){
		if(!children.contains(ch)){
			children.add(ch);
		}
	}
	public Nod getOwner() {
		return owner;
	}
	public void setOwner(Nod owner) {
		this.owner = owner;
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
	public LinkedList<String> getChildren() {
		return children;
	}
	public void setChildren(LinkedList<Nod> children) {
		LinkedList<String> ch = new LinkedList<String>();
		for(Nod n : children){
			ch.add(n.getValue());
		}
		this.children = ch;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(Nod parent) {
		this.parent = parent.value;
	}
	public LinkedList<FileState> getHistory() {
		return history;
	}
	public void setHistory(LinkedList<FileState> history) {
		this.history = history;
	}
}
