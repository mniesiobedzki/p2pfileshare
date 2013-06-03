package file;

import folder.FolderTree;
import folder.Nod;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {
    public FolderTree tree;
    public String uname;
    public int port;

    public static final Logger LOG = Logger.getLogger(FolderTree.class);

    public FileServer(FolderTree ft, String name, int port) {
        // create socket
        this.tree = ft;
        this.uname = name;
        this.port = port;
        this.start();
    }

    /**
     * @param os   - Output Stream
     * @param path - Files path
     * @throws Exception - Nother Goddamn Exception
     */
    public void send(OutputStream os, String path) throws Exception {
        // sendfile
        LOG.info("Sending file " + path);
        java.io.File myFile = new java.io.File(path);
        byte[] mybytearray = new byte[(int) myFile.length() + 1];
        FileInputStream fis = new FileInputStream(myFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        bis.read(mybytearray, 0, mybytearray.length);
        System.out.println("Sending... ");
        os.write(mybytearray, 0, mybytearray.length);
        os.flush();
        os.close();
        bis.close();
    }

    public void run() {
        ServerSocket servsock;
        try {
            System.out.println("Server plików na porcie: " + port);
            servsock = new ServerSocket(port);
            while (true) {
                LOG.info("New connection");
                System.out.println("Waiting...");

                Socket sock = servsock.accept();
                System.out.println("Accepted connection : " + sock);
                OutputStream os = sock.getOutputStream();

                InputStream is = sock.getInputStream();
                java.util.Scanner s = new java.util.Scanner(is)
                        .useDelimiter("\n");
                while (true) {
                    while (!s.hasNext()) {
                    }
                    String msg = s.next();
                    System.out.println("!!!");
                    System.out.println(msg);

                    String path = tree.getFolder().get(uname).getPath();
                    System.out.println(path);
                    // String path = tree.getFolder().get("root").getValue();
                    Nod n = tree.getFolder().get(msg);
                    //System.err.println(n.getValue() + " " + n.getName());
                    System.out.println(n);

                    path += System.getProperty("file.separator");
                    path += n.getName();
                    System.err.println("wysyłam plik ");
                    System.out.println("key: " + msg);
                    System.out.println("path: " + path);
                    this.send(os, path);
                    sock.close();
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}