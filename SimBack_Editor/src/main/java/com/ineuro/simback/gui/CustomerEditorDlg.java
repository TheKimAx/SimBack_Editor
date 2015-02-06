package com.ineuro.simback.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ineuro.simback.dao.SimBackDao;
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
import java.util.Date;
import javax.swing.SwingConstants;

public class CustomerEditorDlg extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private ModeOperation modeEditor = ModeOperation.EDITION;
	private Customer curClient = null;
	private boolean actionPerformed = false;
	private JTextField tfNomCust;
	private JTextField tfPrenomCust;
	private JTextField tfTelCust;
	private JTextField tfNumCNI;
	private JTextField tfEmail;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			CustomerEditorDlg dialog = new CustomerEditorDlg();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public CustomerEditorDlg() {
		setTitle("SIMBack Editor : Création d'un Client");
		setIconImage(Toolkit.getDefaultToolkit().getImage(CustomerEditorDlg.class.getResource("/com/ineuro/simback/resources/LogoSimBack.png")));
		setBounds(100, 100, 450, 325);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblNomDuClient = new JLabel("Nom du Client *:");
			lblNomDuClient.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblNomDuClient, "2, 2, right, default");
		}
		{
			tfNomCust = new JTextField();
			contentPanel.add(tfNomCust, "4, 2, fill, default");
			tfNomCust.setColumns(10);
		}
		{
			JLabel lblPrnomDuClient = new JLabel("Prénom du Client :");
			lblPrnomDuClient.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblPrnomDuClient, "2, 6, right, default");
		}
		{
			tfPrenomCust = new JTextField();
			contentPanel.add(tfPrenomCust, "4, 6, fill, default");
			tfPrenomCust.setColumns(10);
		}
		{
			JLabel lblNumroDeTlphone = new JLabel("Numéro de Téléphone *:");
			lblNumroDeTlphone.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNumroDeTlphone.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblNumroDeTlphone, "2, 10, right, default");
		}
		{
			tfTelCust = new JTextField();
			contentPanel.add(tfTelCust, "4, 10, fill, default");
			tfTelCust.setColumns(10);
		}
		{
			JLabel lblNumroCni = new JLabel("Numéro CNI *:");
			lblNumroCni.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblNumroCni, "2, 14, right, default");
		}
		{
			tfNumCNI = new JTextField();
			tfNumCNI.setColumns(10);
			contentPanel.add(tfNumCNI, "4, 14, fill, default");
		}
		{
			JLabel lblAdresseEmail = new JLabel("Adresse E-Mail *:");
			lblAdresseEmail.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAdresseEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblAdresseEmail, "2, 18, right, default");
		}
		{
			tfEmail = new JTextField();
			tfEmail.setColumns(10);
			contentPanel.add(tfEmail, "4, 18, fill, default");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(this);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(this);
			}
		}
	}

	public ModeOperation getModeEditor() {
		return modeEditor;
	}

	public void setModeEditor(ModeOperation modeEditor) {
		this.modeEditor = modeEditor;
		switch (this.modeEditor) {
		case EDITION:
			this.setTitle("SIMBack Editor - Edition d'un Client");
			break;

		default:
			this.setTitle("SIMBack Editor - Création d'un Client");
			curClient = null;
			break;
		}
	}

	public Customer getCurClient() {
		return curClient;
	}

	public void setCurClient(Customer curClient) {
		this.curClient = curClient;
		if(modeEditor == ModeOperation.EDITION && null != curClient) {
			tfNomCust.setText(curClient.getNomCust());
			tfPrenomCust.setText(curClient.getPrenomCust());
			tfTelCust.setText(curClient.getTelCust());
			tfNumCNI.setText(curClient.getNoCni());
			tfEmail.setText(curClient.getEmailCust());
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		// TODO Auto-generated method stub
		if(ev.getSource() instanceof JButton) {
			switch( ((JButton) ev.getSource()).getActionCommand() ) {
			case "OK":
				if(modeEditor == ModeOperation.EDITION) {
					if(!tfNomCust.getText().isEmpty() && !tfTelCust.getText().isEmpty())
						if(null != curClient && 
							(curClient.getNomCust().equals(tfNomCust.getText())
								|| curClient.getPrenomCust().equals(tfPrenomCust.getText())
								|| curClient.getTelCust().equals(tfTelCust.getText()) )) {
							curClient.setNomCust(tfNomCust.getText());
							curClient.setPrenomCust(tfPrenomCust.getText());
							curClient.setTelCust(tfTelCust.getText());
							curClient.setNoCni(tfNumCNI.getText());
							curClient.setEmailCust(tfEmail.getText());
							
							try {
								SimBackDao.modifyCustomer(curClient);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							setActionPerformed(true);
							setVisible(false);
						} else {
							JOptionPane.showMessageDialog(this, "Aucune modification effectuée sur le client courant !");
							setActionPerformed(false);
							setVisible(true);
						}
					else {
						JOptionPane.showMessageDialog(this, "Certains champs requis sont vides ! Veuillez les remplir afin de pouvoir procéder à la création du client !");
						setActionPerformed(false);
					}
				} else
					if(!tfNomCust.getText().isEmpty() && !tfTelCust.getText().isEmpty()) {
						curClient = new Customer();
						curClient.setNomCust(tfNomCust.getText());
						curClient.setPrenomCust(tfPrenomCust.getText());
						curClient.setTelCust(tfTelCust.getText());
						curClient.setNoCni(tfNumCNI.getText());
						curClient.setEmailCust(tfEmail.getText());
						Date aujourdhui = new Date();
						curClient.setInscrCust(aujourdhui);
						curClient.setLastSubscription(aujourdhui);
						curClient.setExpiredCust(false);
						curClient.setDefault(false);
						
						try {
							SimBackDao.createCustomer(curClient);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						setActionPerformed(true);
						this.setVisible(false);
					} else {
						JOptionPane.showMessageDialog(this, "Certains champs requis sont vides ! Veuillez les remplir afin de pouvoir procéder à la création du client !");
						setActionPerformed(false);
					}
				break;
				
			case "Cancel":
				setActionPerformed(false);
				setVisible(false);
				break;
			}
		}
	}

	public boolean isActionPerformed() {
		return actionPerformed;
	}

	public void setActionPerformed(boolean actionPerformed) {
		this.actionPerformed = actionPerformed;
	}

}
