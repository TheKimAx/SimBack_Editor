package com.ineuro.simback.gui;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.ineuro.simback.dao.SimBackDao;
import com.ineuro.simback.model.Contact;
import com.ineuro.simback.process.HeightToNineConv;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class Conv8To9Dlg extends JDialog implements ActionListener {

	private JPanel contentPane;

	private static String cols[] = { "id", "Noms & Prénoms", "Ancien Numero", "Nouveau Numero"};

	private HashMap<String, Contact> mapContacts;
	private JPanel panButtons;
	private JButton btApply;
	private JButton btCancel;
	
	private boolean stateResult;
	private JScrollPane scrollPane;
	private JTable tabConv;
	private JPanel panBottom;
	private JPanel panel;
	private JLabel lblNombreDeContacts;
	private JLabel lbEff;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Conv8To9Dlg frame = new Conv8To9Dlg();
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
	public Conv8To9Dlg() {
		setFont(new Font("Tahoma", Font.PLAIN, 14));
		setTitle("SIMBack Editor - Conversion des Numeros de 8 à 9 chiffres");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("max(250dlu;default):grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),}));
		
		JLabel lblListesDesNumeros = new JLabel("Listes des numeros du répertoire courant convertibles :");
		lblListesDesNumeros.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblListesDesNumeros, "2, 2");
		
		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "2, 4, fill, fill");
		
		tabConv = new JTable();
		scrollPane.setViewportView(tabConv);
		
		panBottom = new JPanel();
		contentPane.add(panBottom, "2, 6, fill, fill");
		panBottom.setLayout(new BorderLayout(0, 0));
		
		panButtons = new JPanel();
		panBottom.add(panButtons, BorderLayout.EAST);
		FlowLayout fl_panButtons = (FlowLayout) panButtons.getLayout();
		fl_panButtons.setAlignment(FlowLayout.RIGHT);
		
		btApply = new JButton("Appliquer");
		btApply.setActionCommand("APPLY");
		btApply.addActionListener(this);
		panButtons.add(btApply);
		
		btCancel = new JButton("Annuler");
		btCancel.setActionCommand("CANCEL");
		btCancel.addActionListener(this);
		panButtons.add(btCancel);
		
		panel = new JPanel();
		panBottom.add(panel, BorderLayout.WEST);
		
		lblNombreDeContacts = new JLabel("Nombre de Contacts à convertir : ");
		lblNombreDeContacts.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(lblNombreDeContacts);
		
		lbEff = new JLabel("[Eff]");
		lbEff.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 12));
		panel.add(lbEff);
	}

	/**
	 * Define the list of Contact to treat
	 */
	public void setMapContact(HashMap<String, Contact> mapCtcs) {
		this.mapContacts = mapCtcs;
		fillTable();
	}
	
	/**
	 * Fill the table with the contacts number who are eligible to the conversion process
	 */
	public void  fillTable() {
		DefaultTableModel dm = new DefaultTableModel(cols, 0);
		
		if(mapContacts.size()>0) {
			String[] ligne = new String[4];
			
			/* Remplissage du tableau avec la liste des Contacts */
			for(Contact aCtc : mapContacts.values()) {
				if(HeightToNineConv.isConvertible(aCtc.getTelContact())) {
					ligne[0] = Integer.toString(aCtc.getIdContact());
					ligne[1] = aCtc.getFullnameContact();
					ligne[2] = aCtc.getTelContact();
					ligne[3] = HeightToNineConv.convHeightToNine(aCtc.getTelContact());
					
					dm.addRow(ligne);
				}
			}

			/* Remplissage du tableau de contacts et Mise en forme du tableau */
			tabConv.setModel(dm);
			tabConv.validate();
			TableColumnModel curTableColModel = tabConv.getColumnModel();
			TableColumn aCol = curTableColModel.getColumn(0);
			aCol.setMaxWidth(0);
			aCol = curTableColModel.getColumn(1);
			aCol.setMinWidth(100);
			aCol = curTableColModel.getColumn(2);
			aCol.setMinWidth(30);
			aCol = curTableColModel.getColumn(3);
			aCol.setMinWidth(30);
			
			/* Show the number of contacts concerned */
			lbEff.setText(dm.getRowCount() + " contact(s).");
		}
	}
	
	
	/**
	 * Apply the change on every contact concerned
	 */
	public void applyConv8To9() {
		for(int row=0; row<tabConv.getRowCount(); row++) {
			try {
				Contact curContact = mapContacts.get(tabConv.getValueAt(row, 0));
				
				curContact.setTelContact( tabConv.getValueAt(row, 3).toString() );
				SimBackDao.modifyContact(curContact);
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage(),
					"SIMBack Editor - Erreur lors de la sauvegarde d'un contact", 
					JOptionPane.ERROR_MESSAGE);
				stateResult |= false;
			}
		}
		stateResult |= true;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		/** Management of events coming from buttons of this Dialog **/
		if(ev.getSource()  instanceof JButton) {
			JButton button = (JButton) ev.getSource();
			switch(button.getActionCommand()) {
			case "APPLY":
				applyConv8To9();
				this.setVisible(false);
				break;
				
			case "CANCEL":
				stateResult = false;
				this.setVisible(false);
				break;
				
			}
		}
	}
	
	/**
	 * Allow us to know if there was some change in Contacts list
	 */
	public boolean isStateResult() {
		return stateResult;
	}
}
