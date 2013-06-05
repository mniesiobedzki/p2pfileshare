package folder;

import java.io.Serializable;
import java.util.LinkedList;

import file.FileState;

public class Nod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6541854418182281268L;
	String value;
	LinkedList<String> children = new LinkedList<String>();// klucze potomkÃ³w
	String parent;// klucz rodzica
	String name;
	String path;
	String ip;
	Nod owner;
	int  port;
	LinkedList<FileState> history = new LinkedList<FileState>();

	
	public Nod(String val, Nod par) {
		value = val;
		parent = par.getValue();
		name= val;
		if (parent != null) {
			par.addChlid(this.value);
		}
	}
	/**
	 * konstruktor dla uï¿½ytkownka
	 * 
	 * @param val
	 * @param par
	 * @param ip
	 */
	public Nod(String val, Nod par, String ip, int port) {
		value = val;
		parent = par.getValue();
		name= val;
		this.ip=ip;
		this.port=port;
		if (parent != null) {
			par.addChlid(this.value);
		}
	}
	/**
	 * konstruktor dla korzenia
	 * 
	 * @param val- folder path
	 */
	public Nod(String val) {
		value = "root";
		parent = "root";
		name= val;
	}

	/**
	 * konstruktor dla plików
	 * 
	 * @param val - ID pliku
	 * @param n - nazwa pliku
	 * @param par - ID rodzica
	 * @param hist - Historia pliku
	 * @param own - ID wï¿½aï¿½ciciela
	 */
	public Nod(String val, Nod par, LinkedList<FileState> hist, Nod own, String n, String path) {
		value = val;
		parent = par.getValue();
		name = n;
		this.path = path;
		if (parent != null) {
			par.addChlid(this.value);
		}
		history = hist;
		owner = own;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Metoda dodaje nastï¿½pnik do listy
	 * 
	 * @param ch - Child
	 */
	public void addChlid(String ch) {
		if (!children.contains(ch)) {
			children.add(ch);
		}
	}

	public Nod getOwner() {
		return owner;
	}

	public void setOwner(Nod owner) {
		this.owner = owner;
	}

	public void removeChild(Nod ch) {
		if (!children.contains(ch)) {
			children.remove(ch);
		}
	}

	/**
	 * Metoda aktualizujï¿½ca historie pliku o nowy stan
	 * 
	 * @param state - nowy stan
	 */
	public void update(FileState state) {
		if (!this.history.getLast().equals(state)) {
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
		for (Nod n : children) {
			ch.add(n.getValue());
		}
		this.children = ch;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(Nod parent, Nod parent2) {
		this.parent = parent.value;
		parent.addChlid(this.value);
		if(parent2!=null){
			parent2.addChlid(this.value);
		}
	}

	public LinkedList<FileState> getHistory() {
		return history;
	}

	public void setHistory(LinkedList<FileState> history) {
		this.history = history;
	}
}
