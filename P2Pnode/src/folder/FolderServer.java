package folder;

import file.File;
import org.apache.log4j.Logger;

public class FolderServer implements Runnable {

    public static final Logger LOG = Logger.getLogger(FolderServer.class);


    String uname;
    String path;
    public FolderTree ft;

    @Override
    public void run() {
        LOG.info("Run method");
        try {
            Thread.sleep(1000);
            java.io.File[] listaFajli = File.listAllTheFilesInDir(path);
            LOG.info("listaFalji size " + listaFajli.length);
            for (java.io.File file : listaFajli) {
                if (file == null) {
                    LOG.info("File is null");
                    break;
                }
                File f = new File(file.getName(), file.getPath(), uname);

                System.out.println(f.getFileName());

                ft.addFile(f, uname);
                MFolderListener.filesAndTheirHistory.put(uname + f.getFileName(), f);
                System.out.println(f.getFileId());
            }
            MFolderListener.runFolderListener(path, ft, uname);

            LOG.info("FolderServer.run completed with path: " + path + " for user:" + uname);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            LOG.error("BŁĄD");
        }
    }

    public FolderServer(FolderTree ft, String uname, String path) {
        super();

        LOG.info("FolderServer(folderTreeObj, " + uname + ", " + path + ")");

        this.uname = uname;
        this.path = path;
        this.ft = ft;

        LOG.info("FolderServer created !!");
    }
}
