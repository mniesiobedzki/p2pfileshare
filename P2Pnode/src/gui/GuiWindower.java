package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileSystemView;

import main.Controller;


public class GuiWindower implements PropertyChangeListener {
	
    protected final PopupFrame POPUP_FRAME = new PopupFrame();
    JTextField nodeName, serverIPAdress, portInFieldFilesRequests, portOutField, serverPortInput; 
    String folderSynchronizowany;
    JButton generujIdInput, rozpocznijBt;
    Controller kontroler;
    
    public GuiWindower() {
    	//kontroler = k;
    	createGUI();
    }
    
    public static void main(String[] args) {
    	GuiWindower g = new GuiWindower();
    	g.createGUI();
   	}
    
	private void createGUI() {
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    TrayIcon trayIcon = new TrayIcon(ImageIO.read(new File("folder.png")));
                    trayIcon.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {

                            Point pos = e.getLocationOnScreen();
                            Rectangle screen = getScreenBoundsAt(pos);

                            if (pos.x + POPUP_FRAME.getWidth() > screen.x + screen.width) {
                                pos.x = screen.x + screen.width - POPUP_FRAME.getWidth();
                            }
                            if (pos.x < screen.x) {
                                pos.x = screen.x;
                            }

                            if (pos.y + POPUP_FRAME.getHeight() > screen.y + screen.height) {
                                pos.y = screen.y + screen.height - POPUP_FRAME.getHeight();
                            }
                            if (pos.y < screen.y) {
                                pos.y = screen.y;
                            }

                            POPUP_FRAME.setLocation(pos);
                            POPUP_FRAME.setVisible(true);

                        }
                    });
                    SystemTray.getSystemTray().add(trayIcon);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
	}

    public class PopupFrame extends JDialog {
 
      //  private JTextField serverPortInput;
		//private JTextField ipInput;

		public PopupFrame() throws HeadlessException {
            setLayout(null);
            setUndecorated(true);
            setBackground(new Color(0,0,0,0));
            setBounds(200, 200, 200, 200);
            setPreferredSize(new Dimension(300, 400));
            try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            
            BufferedImage obrazTla = null;
			try {
				obrazTla = ImageIO.read(new File("bg.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JLabel tloRamki = new JLabel(new ImageIcon( obrazTla ));
            tloRamki.setBounds(0, 0, 300, 400);
			add( tloRamki, new Integer(0), 0 );
            	
            JPanel panelPierwszegoUruchomienia = new JPanel();
            panelPierwszegoUruchomienia.setLayout(new FlowLayout());
            panelPierwszegoUruchomienia.setBackground(new Color(0,0,0,0));
            panelPierwszegoUruchomienia.setBounds(0,0,300,400);
            
            JPanel spacer1 = new JPanel();
            spacer1.setPreferredSize(new Dimension(300,20));
            spacer1.setBackground(new Color(0,0,0,0));
            panelPierwszegoUruchomienia.add(spacer1);
            
            RichJLabel tytulPowitania = new RichJLabel("P2PDropbox - Witaj!",0);
            tytulPowitania.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            panelPierwszegoUruchomienia.add(tytulPowitania);
            
            RichJLabel podajIPJLabel = new RichJLabel("IP i port bootstrapa",0);
            podajIPJLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panelPierwszegoUruchomienia.add(podajIPJLabel);

            serverIPAdress = new JTextField();
            serverIPAdress.setPreferredSize(new Dimension(180, 30));
            serverIPAdress.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            serverIPAdress.setHorizontalAlignment(JTextField.CENTER);
            panelPierwszegoUruchomienia.add(serverIPAdress);
          
            serverPortInput = new JTextField();
            serverPortInput.setPreferredSize(new Dimension(50, 30));
            serverPortInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            serverPortInput.setHorizontalAlignment(JTextField.CENTER);
            panelPierwszegoUruchomienia.add(serverPortInput);
            
            RichJLabel podajPortyLabel = new RichJLabel("Porty dla po��cze� przych. i wych.:",0);
            podajPortyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panelPierwszegoUruchomienia.add(podajPortyLabel);

            JPanel spacer2 = new JPanel();
            spacer2.setPreferredSize(new Dimension(300,1));
            spacer2.setBackground(new Color(0,0,0,0));
            panelPierwszegoUruchomienia.add(spacer2);
            
            RichJLabel inLabel = new RichJLabel("in:",0);
            inLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panelPierwszegoUruchomienia.add(inLabel);
            
            portInFieldFilesRequests = new JTextField();
            portInFieldFilesRequests.setPreferredSize(new Dimension(60, 30));
            portInFieldFilesRequests.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            portInFieldFilesRequests.setHorizontalAlignment(JTextField.CENTER);
            panelPierwszegoUruchomienia.add(portInFieldFilesRequests);
            
            RichJLabel outLabel = new RichJLabel("out:",0);
            outLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            panelPierwszegoUruchomienia.add(outLabel);
            
            portOutField = new JTextField();
            portOutField.setPreferredSize(new Dimension(60, 30));
            portOutField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            portOutField.setHorizontalAlignment(JTextField.CENTER);
            panelPierwszegoUruchomienia.add(portOutField);
            
            RichJLabel podajIdJLabel = new RichJLabel("ID lub wygeneruj automatycznie",0);
            podajIdJLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panelPierwszegoUruchomienia.add(podajIdJLabel);
            
            nodeName = new JTextField();
            nodeName.getDocument().addDocumentListener(new DocumentListener() {
				public void changedUpdate(DocumentEvent e) {
					generujIdInput.setEnabled(false);
				}

				public void removeUpdate(DocumentEvent e) {
					generujIdInput.setEnabled(false);
				}

				public void insertUpdate(DocumentEvent e) {
					generujIdInput.setEnabled(false);
				}
			});
            
            nodeName.setPreferredSize(new Dimension(250, 30));
            nodeName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            nodeName.setHorizontalAlignment(JTextField.CENTER);
            panelPierwszegoUruchomienia.add(nodeName);
            
            generujIdInput = new JButton("Generuj ID", null);
            generujIdInput.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Random random = new Random();
					nodeName.setText(random.nextInt(100000)+"");
				}
			});
            panelPierwszegoUruchomienia.add(generujIdInput);
            
            JPanel spacer30 = new JPanel();
            spacer30.setPreferredSize(new Dimension(300,30));
            spacer30.setBackground(new Color(0,0,0,0));
            panelPierwszegoUruchomienia.add(spacer30);
            

//            File root = new File("c:/java/jdk6.7");
//            FileSystemView fsv = new SingleRootFileSystemView( root );
//            JFileChooser chooser = new JFileChooser(fsv);
            
            
            /*FileSystemView view = FileSystemView.getFileSystemView();
            File[] roots = view.getRoots();
            File[] files = view.getFiles(roots[0], true);
            System.out.println(files[0].toString());
            
            FileTree chooser = new FileTree(new File("C:/"));
            chooser.setPreferredSize(new Dimension(300, 150));
            chooser.setSize(300, 150);
            panelPierwszegoUruchomienia.add(chooser);*/
            
            JButton wybierzFolderBt = new JButton("Wybierz folder synchronizacji", null);
            wybierzFolderBt.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            wybierzFolderBt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					wyborFolderu();
					rozpocznijBt.setEnabled(true);
				}
			});
            panelPierwszegoUruchomienia.add(wybierzFolderBt);
             
            JPanel spacer31 = new JPanel();
            spacer31.setPreferredSize(new Dimension(300,40));
            spacer31.setBackground(new Color(0,0,0,0));
            panelPierwszegoUruchomienia.add(spacer31);
            
            rozpocznijBt = new JButton("Rozpocznij");
            rozpocznijBt.setEnabled(false);
            rozpocznijBt.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            /*rozpocznijBt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//kontroler.setFolderTreePath(folderSynchronizowany);
				}
			});*/
            panelPierwszegoUruchomienia.add(rozpocznijBt);
            
            JPanel panelLoga = new JPanel();
            panelLoga.setLayout(null);
            panelLoga.setBackground(new Color(0,0,0,0));
            panelLoga.setBounds(0,0,300,400);
            
            JTextPane logPane = new JTextPane();
            logPane.setBounds(8, 5, 285, 385);
            logPane.setEditable(false);
            logPane.setText("To jest test");
            panelLoga.add(logPane);
            
            add(panelPierwszegoUruchomienia, new Integer(1), 0);            
            //getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
            pack();
        }
    }

    public void wyborFolderu() {
    	JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Wybierz folder do synchronizacji");
        chooser.setFileHidingEnabled(true);
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        Component parentComponent = null; // is not null in the real world 
        int state=chooser.showDialog(parentComponent, "Wybierz");
        if (state == JFileChooser.CANCEL_OPTION) return;

        File dest = chooser.getSelectedFile();

        try {
            folderSynchronizowany = dest.getCanonicalPath();
            	
        } catch (IOException ex) { // getCanonicalPath() threw IOException

        }
    }

    public static Rectangle getScreenBoundsAt(Point pos) {
        GraphicsDevice gd = getGraphicsDeviceAt(pos);
        Rectangle bounds = null;

        if (gd != null) {
            bounds = gd.getDefaultConfiguration().getBounds();
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());

            bounds.x += insets.left;
            bounds.y += insets.top;
            bounds.width -= (insets.left + insets.right);
            bounds.height -= (insets.top + insets.bottom);
        }
        return bounds;
    }

    public static GraphicsDevice getGraphicsDeviceAt(Point pos) {
        GraphicsDevice device = null;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();

        ArrayList<GraphicsDevice> lstDevices = new ArrayList<GraphicsDevice>(lstGDs.length);

        for (GraphicsDevice gd : lstGDs) {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            Rectangle screenBounds = gc.getBounds();

            if (screenBounds.contains(pos)) {
                lstDevices.add(gd);
            }
        }

        if (lstDevices.size() == 1) {
            device = lstDevices.get(0);
        }
        return device;
    }

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("nazwa")) {
			//TODO: coś
		}
	}
	
	/* Action Listeners */
	public void addButtonActionListener(ActionListener listener){
		rozpocznijBt.addActionListener(listener);
	}

	public String getFolderPath() {
		return this.folderSynchronizowany;
		
	}

	public String getServerPort() {
		return this.serverPortInput.getText();
	}

	public void displayError(String string) {
		// TODO Wyświetl infromację o błędzie
		
	}

	public int getPortIn() {
		int p = 0;
		try {
			p = Integer.parseInt(portInFieldFilesRequests.getText());
		} catch (Exception e) {
		}
		return p;
	}

	public int getPortOut() {
		int p = 0;
		try {
			p = Integer.parseInt(portOutField.getText());
		} catch (Exception e) {
		}
		return p;
	}

	public String getClientPort() {
		return portOutField.getText();
	}

	public String getClientName() {
		return nodeName.getText();
	}

	public String getServerAddress() {
		// TODO Auto-generated method stub
		return serverIPAdress.getText();
	}
}