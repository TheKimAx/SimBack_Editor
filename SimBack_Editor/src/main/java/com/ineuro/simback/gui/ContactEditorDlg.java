package com.ineuro.simback.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ineuro.simback.dao.SimBackDao;
import com.ineuro.simback.model.Contact;
import com.ineuro.simback.model.Customer;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ContactEditorDlg extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfNoms;
	private JTextField tfPhone;
		
	private static ModeOperation modeEditor = ModeOperation.EDITION;
	
	private Customer curClient;
	private Contact curContact;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			ContactEditorDlg dialog = new ContactEditorDlg();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public ContactEditorDlg() {
		setTitle("SIMBack Editor - Edition de contact :");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ContactEditorDlg.class.getResource("/com/ineuro/simback/resources/LogoSimBack.png")));
		setBounds(100, 100, 450, 190);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblNomsPrnoms = new JLabel("Noms & Prénoms :");
			lblNomsPrnoms.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblNomsPrnoms, "2, 2");
		}
		{
			tfNoms = new JTextField();
			tfNoms.setFont(new Font("Tahoma", Font.PLAIN, 13));
			contentPanel.add(tfNoms, "6, 2, fill, default");
			tfNoms.setColumns(10);
		}
		{
			JLabel lblTlphone = new JLabel("Téléphone :");
			lblTlphone.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblTlphone, "2, 6");
		}
		{
			tfPhone = new JTextField();
			tfPhone.setFont(new Font("Tahoma", Font.PLAIN, 13));
			contentPanel.add(tfPhone, "6, 6, fill, default");
			tfPhone.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btOk = new JButton("OK");
				btOk.setActionCommand("OK");
				buttonPane.add(btOk);
				getRootPane().setDefaultButton(btOk);
				btOk.addActionListener(this);
			}
			{
				JButton btCancel = new JButton("Cancel");
				btCancel.setActionCommand("Cancel");
				buttonPane.add(btCancel);
				btCancel.addActionListener(this);
			}
		}
	}

	public static ModeOperation getModeEditor() {
		return modeEditor;
	}

	public static void setModeEditor(ModeOperation modeEditor) {
		ContactEditorDlg.modeEditor = modeEditor;
	}

	public Customer getCurClient() {
		return curClient;
	}

	public void setCurClient(Customer curClient) {
		this.curClient = curClient;
	}

	public Contact getCurContact() {
		return curContact;
	}

	public void setCurContact(Contact curContact) {
		this.curContact = curContact;
		tfNoms.setText(curContact.getFullnameContact());
		tfPhone.setText(curContact.getTelContact());
	}

	public void saveContact() {
		switch (modeEditor) {
		case EDITION:
			/* Vérification de la différence entre les champs */
			if(tfNoms.getText().equals(curContact.getFullnameContact())
					|| tfPhone.getText().equals(curContact.getTelContact())) {
				/* Réalisation proprement dit de l'enregistrement des modifications effectuées */
				curContact.setFullnameContact(tfNoms.getText());
				curContact.setTelContact(tfPhone.getText());
				try {
					SimBackDao.modifyContact(curContact);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.setVisible(false);
			}
			break;

		default:
			/* Réalisation proprement dit de l'enregistrement du nouveau contact */
			curContact = new Contact();
			curContact.setFullnameContact(tfNoms.getText());
			curContact.setTelContact(tfPhone.getText());
			curContact.setOwnerContact(curClient);
			try {
				SimBackDao.createContact(curContact);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.setVisible(false);
			break;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		/* Vérification de la nature de la source */
		if(ev.getSource() instanceof JButton) {
			JButton src = (JButton) ev.getSource();
			switch(src.getActionCommand()) {
			case "OK":
				/* Vérification de la présence de valeur dans les champs */
				if(!tfNoms.getText().isEmpty() && !tfPhone.getText().isEmpty())
					/* Sauvegarde du contact */
					saveContact();
				break;
				
			case "Cancel":
				break;
			}
		}
	}
}
