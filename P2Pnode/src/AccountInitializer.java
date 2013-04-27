import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class AccountInitializer {

	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {

		JFrame frame = new JFrame("P2PDropbox Start");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	    
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    final JLabel directoryLabel = new JLabel("Witaj, wybierz folder do synchronizacji ");
	    directoryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));
	    frame.add(directoryLabel, BorderLayout.NORTH);

	    final JLabel filenameLabel = new JLabel(" ");
	    filenameLabel.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 36));
	    frame.add(filenameLabel, BorderLayout.SOUTH);

	    JFileChooser fileChooser = new JFileChooser(".");
	    fileChooser.setControlButtonsAreShown(false);
	    frame.add(fileChooser, BorderLayout.CENTER);

	//  Create ActionListener
	    ActionListener actionListener = new ActionListener() {
	      public void actionPerformed(ActionEvent actionEvent) {
	        JFileChooser theFileChooser = (JFileChooser)actionEvent.getSource();
	        String command = actionEvent.getActionCommand();
	        if (command.equals(JFileChooser.APPROVE_SELECTION)) {
	          File selectedFile = theFileChooser.getSelectedFile();
	          directoryLabel.setText(selectedFile.getParent());
	          filenameLabel.setText(selectedFile.getName());
	        }  else if (command.equals(JFileChooser.CANCEL_SELECTION)) {
	          directoryLabel.setText(" ");
	          filenameLabel.setText(" ");
	        }
	      }
	    };
	    fileChooser.addActionListener(actionListener);

	    int width = 750;
	    int height = 500;
	    frame.setBounds( (Toolkit.getDefaultToolkit().getScreenSize().width-width)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-height)/2, width, height);

	    frame.pack();
	    frame.setVisible(true);

	}

}
