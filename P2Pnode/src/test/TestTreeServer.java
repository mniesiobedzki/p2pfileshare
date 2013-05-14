package test;

import java.io.*;
import java.net.*;
import java.util.zip.GZIPOutputStream;

import file.File;
import folder.FolderTree;

public class TestTreeServer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(6789);
        
        FolderTree ft = new FolderTree("kuku");
		String uname = "user1";
		ft.addUser(uname, "kuku\\");
		//File fa = new File(name, File.generateFileId(uname));
		java.io.File[] listaFajli = File.listAllTheFilesInDir("kuku\\");
		
		for (java.io.File file : listaFajli) {
			File f = new File(file.getName(),uname);
			ft.addFile(f, uname);
			File.filesAndTheirHistory.put(uname+f.getFileName(),f);
			System.out.println(f.getFileId());
		}
		File.runFolderListener("kuku\\", ft, uname);
		
        
           Socket connectionSocket = welcomeSocket.accept();
           System.err.println("Connected");
           BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
           
           DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
           clientSentence = inFromClient.readLine();
           
           OutputStream socketStream = connectionSocket.getOutputStream();
           GZIPOutputStream objectOutput = new GZIPOutputStream(new ObjectOutputStream(socketStream));
           ((ObjectOutput) objectOutput).writeObject(ft);
           
           System.out.println("Received: " + clientSentence);
           capitalizedSentence = clientSentence.toUpperCase() + '\n';
           outToClient.writeBytes(capitalizedSentence);
	}

}
