package folder;

import java.util.LinkedList;

import file.FileState;

public class Nod {
	String value;
	LinkedList<String> children = new LinkedList<String>();// klucze potomków
	String parent;// klucz rodzica
	String name;
	Nod owner;
	LinkedList<FileState> history = new LinkedList<FileState>();

	/**
	 * konstruktor dla korzenia i dla u�ytkownik�w
	 * 
	 * @param val
	 * @param par
	 * @param key
	 */
	public Nod(String val, Nod par, String key) {
		value = val;
		parent = par.getValue();
		name= val;
		if (parent != null) {
			par.addChlid(this.value);
		}
	}

	/**
	 * konstruktor dla plik�w
	 * 
	 * @param val
	 *            - ID pliku
	 * @param n
	 *            - nazwa pliku
	 * @param par
	 *            - ID rodzica
	 * @param key
	 *            - Klucz
	 * @param hist
	 *            - Historia pliku
	 * @param own
	 *            - ID w�a�ciciela
	 */
	public Nod(String val, Nod par, String key, LinkedList<FileState> hist,
			Nod own, String n) {
		value = val;
		parent = par.getValue();
		name = n;
		if (parent != null) {
			par.addChlid(this.value);
		}
		history = hist;
		owner = own;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Metoda dodaje nast�pnik w�z�a
	 * 
	 * @param ch
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
	 * Metoda aktualizuj�ca historie pliku o nowy stan
	 * 
	 * @param state
	 *            - nowy stan
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
