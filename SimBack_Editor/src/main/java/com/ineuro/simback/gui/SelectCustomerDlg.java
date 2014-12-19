package com.ineuro.simback.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ineuro.simback.dao.SimBackDao;
import com.ineuro.simback.model.Customer;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SelectCustomerDlg extends JDialog implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	
	private JComboBox<String> cbClient;
	private JButton btModify;
	private JButton btNew;
	
	private Customer selectedClient = null, lastClient = null;
	private List<Customer> lsCustomers = null;
	private HashMap<String, Customer> mapCustomers = null;
	private boolean actionPerformed = false;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			SelectCustomerDlg dialog = new SelectCustomerDlg();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public SelectCustomerDlg() {
		setTitle("SIMBack Editor : Changer de client");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(SelectCustomerDlg.class.getResource("/com/ineuro/simback/resources/LogoSimBack.png")));
		setBounds(100, 100, 475, 140);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(189dlu;default)"),
				ColumnSpec.decode("max(15dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(60dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		{
			JLabel lblClient = new JLabel("Client :");
			lblClient.setFont(new Font("Tahoma", Font.PLAIN, 14));
			contentPanel.add(lblClient, "2, 2, right, default");
		}
		{
			/* Load of all customers */
//			try {
//				lsCustomers = SimBackDao.getActiveCustomers();
//				for(Customer aClient : lsCustomers)
//					mapCustomers.put(aClient.getNomCust() + " - " + aClient.getPrenomCust(), aClient);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			cbClient = new JComboBox<String> ();
			loadCustomers();
			contentPanel.add(cbClient, "4, 2, fill, default");
		}
		{
			btModify = new JButton("Modifier ...");
			btModify.setActionCommand("Modify");
			contentPanel.add(btModify, "7, 2");
			btModify.addActionListener(this);
		}
		{
			btNew = new JButton("Nouveau ...");
			btNew.setActionCommand("New");
			contentPanel.add(btNew, "7, 4");
			btNew.addActionListener(this);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btOk = new JButton("OK");
				btOk.setActionCommand("Ok");
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

	public void loadCustomers() {
		/* Vidage de la zone de sélection */
		cbClient.removeAllItems();
		/* Récupération de la liste des clients */
		try {
			lsCustomers = SimBackDao.getActiveCustomers();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Remplissage de la zone de sélection */
		if(null==mapCustomers)
			mapCustomers = new HashMap<String, Customer>();
		mapCustomers.clear();
		for(Customer aClient : lsCustomers) {
			mapCustomers.put(aClient.getNomCust() + " - " + aClient.getTelCust(), aClient);
			cbClient.addItem(aClient.getNomCust() + " - " + aClient.getTelCust());
		}
//		lsCustomers
		cbClient.validate();
	}
	
	public Customer getLastClient() {
		return lastClient;
	}

	public void setLastClient(Customer lastClient) {
		this.lastClient = lastClient;
	}

	public Customer getSelectedClient() {
		return selectedClient;
	}

	public void setSelectedClient(Customer selectedClient) {
		this.selectedClient = selectedClient;
		cbClient.setSelectedItem(selectedClient);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() instanceof JButton) {
			JButton src = (JButton) ev.getSource();
			switch( src.getActionCommand() ) {
			case "Ok":
				this.selectedClient = (Customer) mapCustomers.get( cbClient.getSelectedItem() );
				setActionPerformed(true);
				setVisible(false);
				break;
				
			case "Cancel":
				setActionPerformed(false);
				setVisible(false);
				break;
				
			case "Modify":
				CustomerEditorDlg dlgModClient = new CustomerEditorDlg();
				dlgModClient.setModeEditor(ModeOperation.EDITION);
				dlgModClient.setCurClient((Customer) cbClient.getSelectedItem());
				dlgModClient.setModal(true);
				dlgModClient.setVisible(true);
				// TODO		Vérification de l'effectivité de la création afin d'effectuer une recharge totale de la liste des clients
				if(dlgModClient.isActionPerformed())
					// TODO Load new items
					loadCustomers();
				System.out.println("SelectCustomerDlg.actionPerformed()" + cbClient.getSelectedItem());
				break;
				
			case "New":
				CustomerEditorDlg dlgNewClient = new CustomerEditorDlg();
				dlgNewClient.setModeEditor(ModeOperation.CREATION);
				dlgNewClient.setModal(true);
				dlgNewClient.setVisible(true);
				// TODO		Vérification de l'effectivité de la création afin d'effectuer une recharge totale de la liste des clients
				if(dlgNewClient.isActionPerformed())
					// TODO Load new items
					loadCustomers();
				System.out.println("SelectCustomerDlg.actionPerformed()" + cbClient.getSelectedItem());
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
