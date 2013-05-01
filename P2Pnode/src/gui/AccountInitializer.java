package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class AccountInitializer {
	
    protected static final PopupFrame POPUP_FRAME = new PopupFrame();
    static JTextField idInput; 
    static String folderSynchronizowany;
    
    public static void main(String[] args) {

        buildGUI();
    }

	private static void buildGUI() {
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

    public static class PopupFrame extends JFrame {

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
            
            RichJLabel podajIdJLabel = new RichJLabel("Podaj ID lub wygeneruj automatycznie",0);
            podajIdJLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panelPierwszegoUruchomienia.add(podajIdJLabel);
            
            idInput = new JTextField();
            idInput.setPreferredSize(new Dimension(250, 30));
            idInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            idInput.setHorizontalAlignment(JTextField.CENTER);
            panelPierwszegoUruchomienia.add(idInput);
            
            JButton generujIdInput = new JButton("Generuj ID", null);
            generujIdInput.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					Random random = new Random();
					idInput.setText(random.nextInt(100000)+"");
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
				}
			});
            panelPierwszegoUruchomienia.add(wybierzFolderBt);
             
            JPanel spacer3 = new JPanel();
            spacer3.setPreferredSize(new Dimension(300,40));
            spacer3.setBackground(new Color(0,0,0,0));
            panelPierwszegoUruchomienia.add(spacer3);
            
            JButton rozpocznijBt = new JButton("Rozpocznij");
            rozpocznijBt.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            panelPierwszegoUruchomienia.add(rozpocznijBt);
            
            add(panelPierwszegoUruchomienia, new Integer(1), 0);            
            //getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
            pack();
        }
    }

    public static void wyborFolderu() {
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
}