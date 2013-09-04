package com.LynchSoftwareEngineering.ImEzServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TestFrameOutPut extends JFrame implements Runnable {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea txtrMasterTextarea;

	/**
	 * Create the frame.
	 */
	public void run() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtrMasterTextarea = new JTextArea();
		txtrMasterTextarea.setText("Master TextArea");
		txtrMasterTextarea.setBounds(6, 6, 438, 231);
		contentPane.add(txtrMasterTextarea);
				
		textField = new JTextField();
		textField.setBounds(6, 244, 438, 28);
		contentPane.add(textField);
		textField.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        System.out.println("Enter key pressed");
		    }
		});
		textField.setColumns(10);
		
		setVisible(true);
		
	}
	public TestFrameOutPut() {

	}
	public JPanel getContentPane() {
		return contentPane;
	}

	public void setContentPane(JPanel contentPane) {
		this.contentPane = contentPane;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextArea getTxtrMasterTextarea() {
		return txtrMasterTextarea;
	}

	public void setTxtrMasterTextarea(JTextArea txtrMasterTextarea) {
		this.txtrMasterTextarea = txtrMasterTextarea;
	}

}
