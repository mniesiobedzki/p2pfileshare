package folder;

import file.File;
import folder.FolderTree;
import folder.MFolderListener;

public class FolderServer implements Runnable {

	
	String uname;
	String path;
	public FolderTree ft;
	
	@Override
	public void run() {
		try {
			Thread.sleep(1000);
			java.io.File[] listaFajli = File.listAllTheFilesInDir(path);

			for (java.io.File file : listaFajli) {
				if(file==null){
					break;
				}
				File f = new File(file.getName(),file.getPath(), uname);

				System.out.println(f.getFileName());
				
				ft.addFile(f, uname);
				MFolderListener.filesAndTheirHistory.put(uname + f.getFileName(), f);
				System.out.println(f.getFileId());
			}
			MFolderListener.runFolderListener(path, ft, uname);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FolderServer(FolderTree ft, String uname, String path) {
		super();
		this.uname = uname;
		this.path = path;
		this.ft = ft;
	}
}
