package test;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3974693005784158632L;
	public String name;
	public String ip;
	public int filePort;
	public int treePort;
	
	public User(String name, String ip, int filePort, int treePort) {
		super();
		this.name = name;
		this.ip = ip;
		this.filePort = filePort;
		this.treePort = treePort;
	}
}
