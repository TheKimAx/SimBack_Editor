package com.ineuro.simback.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.Toolkit;

public class SimBackAbout extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SimBackAbout dialog = new SimBackAbout();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SimBackAbout() {
		setTitle("SIMBack Editor - A Propos");
		setIconImage(Toolkit.getDefaultToolkit().getImage(SimBackAbout.class.getResource("/com/ineuro/simback/resources/LogoSimBack.png")));
		setBounds(100, 100, 527, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			{
				JLabel lblImage = new JLabel("");
				panel.add(lblImage);
				lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
				lblImage.setIcon(new ImageIcon(SimBackAbout.class.getResource("/com/ineuro/simback/resources/LogoProxy.png")));
			}
			{
				JLabel lblImage_1 = new JLabel("");
				lblImage_1.setIcon(new ImageIcon(SimBackAbout.class.getResource("/com/ineuro/simback/resources/LogoSimBack.png")));
				panel.add(lblImage_1);
			}
		}
		{
			JTextPane txtpnSimbackEstUn = new JTextPane();
			txtpnSimbackEstUn.setText("SIMBack est un produit exclusif de Proxy developpé dans le cadre de la cellule de développement iNeuro.\r\nElle a été entièrement conçue et réalisée sous la directive de LEUNGOUE ABOUEM Dereck Axel.");
			contentPanel.add(txtpnSimbackEstUn);
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
			}
		}
	}

}
