package presentation;

import bll.ClientBLL;
import model.Client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ClientsOperationsWindow extends JFrame {
    private JPanel contentPanel;

    private JPanel clientsPanel;
    private JTable clientsTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPanel addPanel;
    private JLabel addNameLabel;
    private JTextField addNameField;
    private JLabel addAddressLabel;
    private JTextField addAddressField;
    private JLabel addEmailLabel;
    private JTextField addEmailField;
    private JButton addButton;

    private JPanel modifyPanel;
    private JLabel modifyNameLabel;
    private JTextField modifyNameField;
    private JLabel modifyAddressLabel;
    private JTextField modifyAddressField;
    private JLabel modifyEmailLabel;
    private JTextField modifyEmailField;
    private JButton modifyButton;

    private JButton deleteButton;
    private JButton backButton;

    public ClientsOperationsWindow(){
        super("Clients Operations");
        initGUI();
    }

    private void initGUI(){
        setSize(650 , 500);
        contentPanel = new JPanel(null);
        setContentPane(contentPanel);
        initTable();
        initAddPanel();
        initModifyPanel();
        initButtons();
        initListeners();
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initListeners(){
        clientsTable.getSelectionModel().addListSelectionListener(e -> {
            int[] rows = clientsTable.getSelectedRows();
            if(rows.length == 1){
                modifyButton.setEnabled(true);
                modifyNameField.setEditable(true);
                modifyAddressField.setEditable(true);
                modifyEmailField.setEditable(true);
                modifyNameField.setText(clientsTable.getValueAt(rows[0], 1).toString());
                modifyAddressField.setText(clientsTable.getValueAt(rows[0], 2).toString());
                modifyEmailField.setText(clientsTable.getValueAt(rows[0], 3).toString());
            }
            else{
                modifyButton.setEnabled(false);
                modifyNameField.setEditable(false);
                modifyAddressField.setEditable(false);
                modifyEmailField.setEditable(false);
                clearModifyFields();
            }
            deleteButton.setEnabled(rows.length >= 1);

        });

        DocumentListener documentListener = new DocumentListener() {

            void validateInput(){
                addButton.setEnabled(!(addNameField.getText().equals("") ||
                        addAddressField.getText().equals("") ||
                        addEmailField.getText().equals("")));
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateInput();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateInput();
            }
        };

        addNameField.getDocument().addDocumentListener(documentListener);
        addAddressField.getDocument().addDocumentListener(documentListener);
        addEmailField.getDocument().addDocumentListener(documentListener);

        addButton.addActionListener(e -> {
            String name = addNameField.getText();
            String address = addAddressField.getText();
            String email = addEmailField.getText();
            ClientBLL clientBLL = new ClientBLL();
            int idValue = clientBLL.insertClient(new Client(name, address, email));
            tableModel.addRow(new Object[]{idValue, name, address, email});
            clearAddFields();
        });

        modifyButton.addActionListener(e -> {
            int row = clientsTable.getSelectedRow();
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String name = modifyNameField.getText();
            String address = modifyAddressField.getText();
            String email = modifyEmailField.getText();
            ClientBLL clientBLL = new ClientBLL();
            clientBLL.updateClient(new Client(id, name, address, email));
            tableModel.setValueAt(name, row, 1);
            tableModel.setValueAt(address, row, 2);
            tableModel.setValueAt(email, row, 3);
            clearModifyFields();
        });

        deleteButton.addActionListener(e -> {
            int[] rows = clientsTable.getSelectedRows();
            int id;
            ClientBLL clientBLL = new ClientBLL();
            for(int i = 0; i < rows.length; i++){
                id = Integer.parseInt(tableModel.getValueAt(rows[i] - i, 0).toString());
                clientBLL.deleteClient(new Client(id));
                tableModel.removeRow(rows[i] - i);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new MainWindow();
        });
    }


    private void initTable(){
        clientsPanel = new JPanel(new BorderLayout());
        tableModel = new ClientBLL().initClientsTable();
        clientsTable = new JTable(tableModel);
        clientsTable.setDefaultEditor(Object.class, null);
        tableScrollPane = new JScrollPane(clientsTable);
        clientsPanel.add(tableScrollPane);
        clientsPanel.setBounds(10, 10, 300, 200);
        contentPanel.add(clientsPanel);
    }

    private void initAddPanel(){
        addPanel = new JPanel(null);
        addNameLabel = new JLabel("Name");
        addNameLabel.setBounds(15, 45, 50, 15);
        addNameField = new JTextField();
        addNameField.setBounds(75, 45, 200, 20);
        addAddressLabel = new JLabel("Address");
        addAddressLabel.setBounds(15, 70, 50, 15);
        addAddressField = new JTextField();
        addAddressField.setBounds(75, 70, 200, 20);
        addEmailLabel = new JLabel("Email");
        addEmailLabel.setBounds(15, 95, 50, 15);
        addEmailField = new JTextField();
        addEmailField.setBounds(75, 95, 200, 20);
        addButton = new JButton("Add Client");
        addButton.setEnabled(false);
        addButton.setBounds(75, 125, 200, 70);
        addPanel.add(addNameLabel);
        addPanel.add(addNameField);
        addPanel.add(addAddressLabel);
        addPanel.add(addAddressField);
        addPanel.add(addEmailLabel);
        addPanel.add(addEmailField);
        addPanel.add(addButton);
        contentPanel.add(addPanel);
        addPanel.setBounds(10, 220,  300, 210);
        addPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void initModifyPanel(){
        modifyPanel = new JPanel(null);
        modifyNameLabel = new JLabel("Name");
        modifyNameLabel.setBounds(15, 45, 50, 15);
        modifyNameField = new JTextField();
        modifyNameField.setBounds(75, 45, 200, 20);
        modifyAddressLabel = new JLabel("Address");
        modifyAddressLabel.setBounds(15, 70, 50, 15);
        modifyAddressField = new JTextField();
        modifyAddressField.setBounds(75, 70, 200, 20);
        modifyEmailLabel = new JLabel("Email");
        modifyEmailLabel.setBounds(15, 95, 50, 15);
        modifyEmailField = new JTextField();
        modifyEmailField.setBounds(75, 95, 200, 20);
        modifyButton = new JButton("Modify Client");
        modifyButton.setBounds(75, 125, 200, 70);
        modifyButton.setEnabled(false);
        modifyPanel.add(modifyNameLabel);
        modifyPanel.add(modifyNameField);
        modifyPanel.add(modifyAddressLabel);
        modifyPanel.add(modifyAddressField);
        modifyPanel.add(modifyEmailLabel);
        modifyPanel.add(modifyEmailField);
        modifyPanel.add(modifyButton);
        contentPanel.add(modifyPanel);
        modifyPanel.setBounds(320, 220,  300, 210);
        modifyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void initButtons(){
        deleteButton = new JButton("Delete Clients");
        deleteButton.setBounds(400, 130, 200, 70);
        deleteButton.setEnabled(false);
        contentPanel.add(deleteButton);
        backButton = new JButton("Back");
        backButton.setBounds(450, 10, 150, 70);
        contentPanel.add(backButton);
    }

    private void clearAddFields(){
        addNameField.setText("");
        addAddressField.setText("");
        addEmailField.setText("");
    }

    private void clearModifyFields(){
        modifyNameField.setText("");
        modifyAddressField.setText("");
        modifyEmailField.setText("");
    }

}
