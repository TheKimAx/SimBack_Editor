package com.ineuro.simback.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.xml.sax.SAXException;

import com.ineuro.simback.dao.SimBackDao;
import com.ineuro.simback.model.Contact;
import com.ineuro.simback.model.Customer;
import com.ineuro.simback.process.ExportVCard;
import com.ineuro.simback.process.ExportXml;
import com.ineuro.simback.process.ImportVCard;
import com.ineuro.simback.process.ImportXml;

import ezvcard.VCardException;

@SuppressWarnings("serial")
public class SimBackEditor extends JFrame implements ActionListener, ListSelectionListener {

	private JPanel contentPane;
	private JLabel lblCustName;
	private JLabel lbEffContacts;
	
	private Customer curClient;
	private List<Contact> lsContacts;
	private HashMap<String, Contact> mapContacts;
	
	private static boolean btChangeEnabled = true;
	private static boolean btChangeVisible = true;
	private JTable table;
	private static String cols[] = { "id", "Noms & Prénoms", "Téléphone"};
	
	/* MenuItems de l'application */
	private JMenuItem mntmAddContact;
	private JMenuItem mntmEditContact;
	private JMenuItem mntmSupprContact;
	private JMenuItem mntmSynchrOnline;
	private JMenuItem mntmImportXml;
	private JMenuItem mntmImportVCard;
	private	JMenuItem mntmExportXml;
	private JMenuItem mntmExportVCard;
	private JMenuItem mntmAPropos;
	private Component horizontalGlue;
	private Component horizontalGlue_1;
	private Component horizontalGlue_2;
	private JMenu mnExtra;
	private JMenuItem mntmConvToNine;

	static {
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName()
					);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * Allow us to load data relative to the current client
	 */
	private void loadCurClient() {
		try {
			curClient = SimBackDao.getCurrentCustomer();
			// TODO Change de 
			if(null != curClient) {
				lblCustName.setText( curClient.getNomCust() 
						+ " " + curClient.getPrenomCust()
						+ " [" + curClient.getTelCust() + "]");
				/* Load of related contacts */
				loadCurContacts();
			}
			else
				openDlgChgCustomer();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Allows us to load already saved contacts related to the current client
	 */
	private void loadCurContacts() {
			try {
				if(null != curClient)
					lsContacts = SimBackDao.getContactsForClient(curClient);
				fillTable(lsContacts);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimBackEditor frame = new SimBackEditor();
					frame.loadCurClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimBackEditor() {
		setTitle("SIMBack Editor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnContacts = new JMenu("Contacts");
		menuBar.add(mnContacts);
		
		mntmAddContact = new JMenuItem("Ajouter un Contact");
		mnContacts.add(mntmAddContact);
		mntmAddContact.addActionListener(this);
		mntmAddContact.setActionCommand("AddContact");
		
		mntmEditContact = new JMenuItem("Editer un Contact");
		mntmEditContact.setEnabled(false);
		mnContacts.add(mntmEditContact);
		mntmEditContact.addActionListener(this);
		mntmEditContact.setActionCommand("ModContact");
		
		mntmSupprContact = new JMenuItem("Supprimer un Contact");
		mntmSupprContact.setEnabled(false);
		mnContacts.add(mntmSupprContact);
		mntmSupprContact.addActionListener(this);
		mntmSupprContact.setActionCommand("DelContact");
		
		mntmSynchrOnline = new JMenuItem("Synchroniser en ligne");
		mnContacts.add(mntmSynchrOnline);
		mntmSynchrOnline.addActionListener(this);
		mntmSynchrOnline.setActionCommand("SyncOnline");
		
		JMenu mnTraitements = new JMenu("Traitements");
		menuBar.add(mnTraitements);
		
		mntmImportXml = new JMenuItem("Importer depuis un fichier XML");
		mnTraitements.add(mntmImportXml);
		mntmImportXml.addActionListener(this);
		mntmImportXml.setActionCommand("ImportXML");
		
		mntmImportVCard = new JMenuItem("Importer depuis un fichier vCard");
		mnTraitements.add(mntmImportVCard);
		mntmImportVCard.addActionListener(this);
		mntmImportVCard.setActionCommand("ImportVCard");
		
		mntmExportXml = new JMenuItem("Exporter vers un fichier XML");
		mnTraitements.add(mntmExportXml);
		mntmExportXml.addActionListener(this);
		mntmExportXml.setActionCommand("ExportXML");
		
		mntmExportVCard = new JMenuItem("Exporter vers un fichier vCard");
		mnTraitements.add(mntmExportVCard);
		mntmExportVCard.addActionListener(this);
		mntmExportVCard.setActionCommand("ExportVCard");
		
		mnExtra = new JMenu("Extra");
		menuBar.add(mnExtra);
		
		mntmConvToNine = new JMenuItem("Convertir les numeros à 9 Chiffres");
		mnExtra.add(mntmConvToNine);
		mntmConvToNine.addActionListener(this);
		mntmConvToNine.setActionCommand("ConvToNine");
		
		mntmAPropos = new JMenuItem("A Propos");
		menuBar.add(mntmAPropos);
		mntmAPropos.setActionCommand("About");
		
		horizontalGlue_2 = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue_2);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue_1);
		
		horizontalGlue = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		mntmAPropos.addActionListener(this);
		
		JPanel panCtc = new JPanel();
		contentPane.add(panCtc, BorderLayout.CENTER);
		panCtc.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panCtc.add(scrollPane, BorderLayout.CENTER);
		
		Object data[][] = {};
		table = new JTable( data, cols);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		table.getSelectionModel().addListSelectionListener(this);
//		table.setSelectionMode(JTable.);
		
		TableColumnModel curTableColModel = table.getColumnModel();
		TableColumn aCol = curTableColModel.getColumn(0);
		aCol.setMaxWidth(0);
		aCol = curTableColModel.getColumn(1);
		aCol.setMinWidth(300);
		
		JPanel panCust = new JPanel();
		contentPane.add(panCust, BorderLayout.NORTH);
		
		JLabel lblNomDuClient = new JLabel("Nom du Client :");
		panCust.add(lblNomDuClient);
		
		lblCustName = new JLabel("[SAMPLE Customer]");
		panCust.add(lblCustName);
		
		JButton btnChangeCust = new JButton("Change ...");
		btnChangeCust.setVisible(btChangeVisible);
		btnChangeCust.setEnabled(btChangeEnabled);
		panCust.add(btnChangeCust);
		btnChangeCust.addActionListener(this);
		
		JPanel panSts = new JPanel();
		contentPane.add(panSts, BorderLayout.SOUTH);
		
		JLabel lblNombresDeContacts = new JLabel("Nombres de contacts :");
		panSts.add(lblNombresDeContacts);
		
		lbEffContacts = new JLabel("[lbEffContacts]");
		panSts.add(lbEffContacts);

		/* Remplissage du tableau avec des données test */
//		fillContacts();				
		
		/* Loading of data from current client */
		loadCurClient();
	}

	
	/**
	 * Allow to create sample data and fill table with it 
	 */
	public void fillContacts() {
		List<Contact> lsContacts = new ArrayList<Contact>();
		
		Contact aContact = new Contact();
		aContact.setIdContact(1);
		aContact.setFullnameContact("Sample Customer1");
		aContact.setTelContact("6 66 66 66 61");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(2);
		aContact.setFullnameContact("Sample Customer2");
		aContact.setTelContact("6 66 66 66 62");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(3);
		aContact.setFullnameContact("Sample Customer3");
		aContact.setTelContact("6 66 66 66 63");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(4);
		aContact.setFullnameContact("Sample Customer4");
		aContact.setTelContact("6 66 66 66 64");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(5);
		aContact.setFullnameContact("Sample Customer5");
		aContact.setTelContact("6 66 66 66 65");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(6);
		aContact.setFullnameContact("Sample Customer6");
		aContact.setTelContact("6 66 66 66 66");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(7);
		aContact.setFullnameContact("Sample Customer7");
		aContact.setTelContact("6 66 66 66 67");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(8);
		aContact.setFullnameContact("Sample Customer8");
		aContact.setTelContact("6 66 66 66 68");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(9);
		aContact.setFullnameContact("Sample Customer9");
		aContact.setTelContact("6 66 66 66 69");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		aContact = new Contact();
		aContact.setIdContact(10);
		aContact.setFullnameContact("Sample Customer10");
		aContact.setTelContact("6 66 66 66 70");
		aContact.setOwnerContact(curClient);
		lsContacts.add(aContact);
		
		fillTable(lsContacts);
	}
	
	/**
	 * Allow to fill Table with defined list of contacts
	 * @param lsContacts
	 */
	public void fillTable( List<Contact> lsContacts) {
		DefaultTableModel dm = new DefaultTableModel(cols, 0);
		
		if(null!=lsContacts) {
			/* Réinitialise la table associative */
			mapContacts =  new HashMap<String, Contact>();
			
			/* Mise en forme de la liste des contacts */
			for(Contact aContact : lsContacts) {
				String[] ligne = new String[3];

				ligne[0] = Integer.toString(aContact.getIdContact() );
				ligne[1] = aContact.getFullnameContact();
				ligne[2] = aContact.getTelContact();

				String key = Integer.toString( aContact.getIdContact() );
				mapContacts.put(key, aContact);
				dm.addRow(ligne);
			}

			/* Affichage du nombre de contacts */
			lbEffContacts.setText( lsContacts.size() + " contact" + (lsContacts.size()!=0 ? "s" : ""));
		}

		/* Remplissage du tableau de contacts et Mise en forme du tableau */
		table.setModel(dm);
		table.validate();
		TableColumnModel curTableColModel = table.getColumnModel();
		TableColumn aCol = curTableColModel.getColumn(0);
		aCol.setMaxWidth(0);
		aCol = curTableColModel.getColumn(1);
		aCol.setMinWidth(300);
	}
	
	public void openDlgChgCustomer() {
		/* Call here the dialog for switching client */
		SelectCustomerDlg dlgChgCustomer = new SelectCustomerDlg();
		dlgChgCustomer.setSelectedClient(curClient);
		dlgChgCustomer.setModal(true);
		dlgChgCustomer.setVisible(true);
		
		/* Check if the dialog box result in a positive manner (selection of a new client) */
		if(dlgChgCustomer.isActionPerformed())
			if(curClient != dlgChgCustomer.getSelectedClient()) {
				/* Change effectively in database the current client */
				try {
					SimBackDao.changeCurrentCustomer(curClient, dlgChgCustomer.getSelectedClient());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/* Change in current window the current client */
				curClient = dlgChgCustomer.getSelectedClient();
				/* Load informations about new current client and then his directory  */
				loadCurClient();
				/* Load the repertory of the new current client */
//				loadCurContacts();
			}
	}
	
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		
		/* Action sur le bouton de bascule de client */
		if(src instanceof JButton) {
			JButton button = (JButton) src;
			if(button.getText() == "Change ...")
				openDlgChgCustomer();
		}
		
		/* Action sur un menu de la fenêtre */
		if(src instanceof JMenuItem) {
			JMenuItem menu = (JMenuItem) src;
			FileDialog fileDlg;
			JFileChooser fileChooser;
			
			System.out.println("SimBackEditor.actionPerformed() Menu sélectionné : " + menu.getText());
			switch(menu.getActionCommand()) {
			case "AddContact":
				ContactEditorDlg dlgAddContact = new ContactEditorDlg();
				ContactEditorDlg.setModeEditor(ModeOperation.CREATION);
				dlgAddContact.setCurClient(curClient);
				dlgAddContact.setCurContact(null);
				dlgAddContact.setModal(true);
				dlgAddContact.setVisible(true);
				this.loadCurContacts();
				break;
			
			case "ModContact":
				ContactEditorDlg dlgModContact = new ContactEditorDlg();
				ContactEditorDlg.setModeEditor(ModeOperation.EDITION);
				dlgModContact.setCurClient(curClient);
				// TODO Récupérer la bonne valeur du contact courant pour pouvoir procéder à son édition
				if(-1 == table.getSelectedRow())
					break;
				String key = table.getValueAt(table.getSelectedRow(), 0).toString();
				Contact selContact = mapContacts.get(key);
				dlgModContact.setCurContact(selContact);
				dlgModContact.setModal(true);
				dlgModContact.setVisible(true);
				this.loadCurContacts();
				break;
				
			case "DelContact":
				int res =  JOptionPane.showConfirmDialog(this, "Etes-vous sur de vouloir supprimer ce contact ?", 
						"SIMBack Editor - Suppression d'un contact", JOptionPane.YES_NO_OPTION);
				if(-1 == table.getSelectedRow())
					break;
				if(JOptionPane.YES_OPTION == res) {
					String keyString = table.getValueAt(table.getSelectedRow(), 0).toString();
					Contact selContact2 = mapContacts.get(keyString);
					try {
						SimBackDao.deleteContact(selContact2);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					this.loadCurContacts();
				}
				break;
				
			case "ImportXML":
				fileDlg = new FileDialog(this, "SimBack Editor - Import d'un fichier XML", FileDialog.LOAD);
				fileDlg.setDirectory("%HOMEPATH%");
//				fileDlg.setFilenameFilter();
				fileDlg.setVisible(true);
				File[] files = fileDlg.getFiles();
				if(0 < files.length) {
					ImportXml impObj = new ImportXml();
					impObj.setCurClient(curClient);
					try {
						impObj.parseFile(new FileInputStream( files[0] ));
						loadCurContacts();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				
			case "ImportVCard":
				fileDlg = new FileDialog(this, "SimBack Editor - Import d'un fichier vCard", FileDialog.LOAD);
				fileDlg.setDirectory("%HOMEPATH%");
				fileDlg.setMultipleMode(true);
				fileDlg.setVisible(true);
				files = fileDlg.getFiles();
				if(0 < files.length) {
					/* Traitement des différents fichiers VCF */
					for(int i = 0; i < files.length; i++) {
						System.out.println("SimBackEditor.actionPerformed() Fichier sélectionné : " + files[i].getAbsolutePath());
						try {
							ImportVCard.setCurClient(curClient);
							ImportVCard.parseFile(files[i].getAbsolutePath());
						} catch (VCardException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					loadCurContacts();
				}
				break;
			
			case "ExportXML":
				String fileName = "SimBack_" + curClient.getNomCust() + "_" + curClient.getTelCust() + "_" + getCurDateStamp() + ".xml";
				
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory( new File("%HOMEPATH%") );
				fileChooser.setDialogTitle("SimBack Editor - Export des contacts au format XML : Choix du répertoire de destination");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int result = fileChooser.showOpenDialog(this);
				if(JFileChooser.APPROVE_OPTION == result) {
					String dirPath = fileChooser.getSelectedFile().getAbsolutePath();
					System.out.println("SimBackEditor.actionPerformed() Répertoire : " + dirPath);
					System.out.println("SimBackEditor.actionPerformed() Nom du fichier [Before Transform] : " + fileName);
					fileName = dirPath + "\\" + fileName;
					System.out.println("SimBackEditor.actionPerformed() Nom du fichier [After Transform] : " + fileName);
					
					try {
						ExportXml.saveContactsToXmlFile(fileName, lsContacts, curClient);
						Desktop.getDesktop().open(new File(dirPath));
					} catch (ParserConfigurationException
							| TransformerFactoryConfigurationError
							| TransformerException
							| IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			
			case "ExportVCard":
				fileName = "SimBack_" + curClient.getNomCust() + "_" + curClient.getTelCust() + "_" + getCurDateStamp() + ".vcf";
				
				fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory( new File("%HOMEPATH%") );
				fileChooser.setDialogTitle("SimBack Editor - Export des contacts au format vCard : Choix du répertoire de destination");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				result = fileChooser.showOpenDialog(this);
				if(JFileChooser.APPROVE_OPTION == result) {
					String dirPath = fileChooser.getSelectedFile().getAbsolutePath();
					System.out.println("SimBackEditor.actionPerformed() Répertoire : " + dirPath);
					System.out.println("SimBackEditor.actionPerformed() Nom du fichier [Before Transform] : " + fileName);
					fileName = dirPath + "\\" + fileName;
					System.out.println("SimBackEditor.actionPerformed() Nom du fichier [After Transform] : " + fileName);
					
					// TODO Appel de la méthode de sauvegarde des contacts au format XML
					try {
						ExportVCard.saveContactsToVcardFile(fileName, lsContacts);
						Desktop.getDesktop().open(new File(dirPath));
					} catch (IOException | VCardException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
				
			case "ConvToNine":
				Conv8To9Dlg dlgConv8To9 = new Conv8To9Dlg();
				dlgConv8To9.setMapContact(mapContacts);
				dlgConv8To9.setModal(true);
				dlgConv8To9.setVisible(true);
				loadCurContacts();
				break;
			
			case "About":
				SimBackAbout dlgAbout = new SimBackAbout();
				dlgAbout.setModal(true);
				dlgAbout.setVisible(true);
				break;
			}
			
			switch(menu.getText()) {
			case "A Propos":
				SimBackAbout dlgAbout = new SimBackAbout();
				dlgAbout.setModal(true);
				dlgAbout.setVisible(true);
				break;
			}
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		boolean status = table.getSelectedRow()!=-1;
		mntmEditContact.setEnabled(status);
		mntmSupprContact.setEnabled(status);
	}
	
	private String getCurDateStamp() {
		SimpleDateFormat simpleDf = new SimpleDateFormat("ddMMyyyy_hhmmss");
		return simpleDf.format(new Date());
	}

}
