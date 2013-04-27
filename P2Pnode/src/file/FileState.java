
// Obiekt który jest dodawany do LinkedListy folderu który obserwujemy.
// FileState jest po prostu wygodn¹ wersj¹ wiersza w historii danego pliku 

public class FileState {
	
	private	String personID = ""; 	// kto zmienil plik
	private long   data 	= 0;	// kiedy plik by³ zmieniany		
	private String fileName = "";	// nazwa zmienianego pliku
	private double size		= 0;	// rozmiar pliku
	private String md5		= "";	// suma kontrolna
	
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}


	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getPersonID() {
		return personID;
	}

	public void setPersonID(String personID) {
		this.personID = personID;
	}
	
}
