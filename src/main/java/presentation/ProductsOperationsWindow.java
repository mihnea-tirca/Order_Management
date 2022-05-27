package presentation;

import bll.ClientBLL;
import bll.ProductBLL;
import model.Product;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductsOperationsWindow extends JFrame {
    private JPanel contentPanel;

    private JPanel productsPanel;
    private JTable productsTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;

    private JPanel addPanel;
    private JLabel addNameLabel;
    private JTextField addNameField;
    private JLabel addStockLabel;
    private JTextField addStockField;
    private JLabel addPriceLabel;
    private JTextField addPriceField;
    private JButton addButton;

    private JPanel modifyPanel;
    private JLabel modifyNameLabel;
    private JTextField modifyNameField;
    private JLabel modifyStockLabel;
    private JTextField modifyStockField;
    private JLabel modifyPriceLabel;
    private JTextField modifyPriceField;
    private JButton modifyButton;

    private JButton deleteButton;
    private JButton backButton;

    public ProductsOperationsWindow(){
        super("Products Operations");
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
        productsTable.getSelectionModel().addListSelectionListener(e -> {
            int[] rows = productsTable.getSelectedRows();
            if(rows.length == 1){
                modifyButton.setEnabled(true);
                modifyNameField.setEditable(true);
                modifyStockField.setEditable(true);
                modifyPriceField.setEditable(true);
                modifyNameField.setText(productsTable.getValueAt(rows[0], 1).toString());
                modifyStockField.setText(productsTable.getValueAt(rows[0], 2).toString());
                modifyPriceField.setText(productsTable.getValueAt(rows[0], 3).toString());
            }
            else{
                modifyButton.setEnabled(false);
                modifyNameField.setEditable(false);
                modifyStockField.setEditable(false);
                modifyPriceField.setEditable(false);
                clearModifyFields();
            }
            deleteButton.setEnabled(rows.length >= 1);

        });

        DocumentListener documentListener = new DocumentListener() {

            void validateInput(){
                addButton.setEnabled(!(addNameField.getText().equals("") ||
                        addStockField.getText().equals("") ||
                        addPriceField.getText().equals("")));
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
        addStockField.getDocument().addDocumentListener(documentListener);
        addPriceField.getDocument().addDocumentListener(documentListener);

        addButton.addActionListener(e -> {
            String name = addNameField.getText();
            int stock;
            double price;
            try {
                stock = Integer.parseInt(addStockField.getText());
                price = Double.parseDouble(addPriceField.getText());
            } catch(NumberFormatException ex){
                new FailureWindow("<html>Invalid Input!<br/> Stock must be integer, Price must be double!</html>");
                return;
            }
            ProductBLL productBLL = new ProductBLL();
            int idValue = productBLL.insertProduct(new Product(name, stock, price));
            tableModel.addRow(new Object[]{idValue, name, stock, price});
            clearAddFields();
        });

        modifyButton.addActionListener(e -> {
            int row = productsTable.getSelectedRow();
            int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
            String name = modifyNameField.getText();
            int stock = Integer.parseInt(modifyStockField.getText());
            double price = Double.parseDouble(modifyPriceField.getText());
            ProductBLL productBLL = new ProductBLL();
            productBLL.updateProduct(new Product(id, name, stock, price));
            tableModel.setValueAt(name, row, 1);
            tableModel.setValueAt(stock, row, 2);
            tableModel.setValueAt(price, row, 3);
            clearModifyFields();
        });

        deleteButton.addActionListener(e -> {
            int[] rows = productsTable.getSelectedRows();
            int id;
            ProductBLL productBLL = new ProductBLL();
            for(int i = 0; i < rows.length; i++){
                id = Integer.parseInt(tableModel.getValueAt(rows[i] - i, 0).toString());
                productBLL.deleteProduct(new Product(id));
                tableModel.removeRow(rows[i] - i);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new MainWindow();
        });
    }


    private void initTable(){
        productsPanel = new JPanel(new BorderLayout());
        tableModel = new ProductBLL().initProductsTable();
        productsTable = new JTable(tableModel);
        productsTable.setDefaultEditor(Object.class, null);
        tableScrollPane = new JScrollPane(productsTable);
        productsPanel.add(tableScrollPane);
        productsPanel.setBounds(10, 10, 300, 200);
        contentPanel.add(productsPanel);
    }

    private void initAddPanel(){
        addPanel = new JPanel(null);
        addNameLabel = new JLabel("Name");
        addNameLabel.setBounds(15, 45, 50, 15);
        addNameField = new JTextField();
        addNameField.setBounds(75, 45, 200, 20);
        addStockLabel = new JLabel("Stock");
        addStockLabel.setBounds(15, 70, 50, 15);
        addStockField = new JTextField();
        addStockField.setBounds(75, 70, 200, 20);
        addPriceLabel = new JLabel("Price");
        addPriceLabel.setBounds(15, 95, 50, 15);
        addPriceField = new JTextField();
        addPriceField.setBounds(75, 95, 200, 20);
        addButton = new JButton("Add Product");
        addButton.setEnabled(false);
        addButton.setBounds(75, 125, 200, 70);
        addPanel.add(addNameLabel);
        addPanel.add(addNameField);
        addPanel.add(addStockLabel);
        addPanel.add(addStockField);
        addPanel.add(addPriceLabel);
        addPanel.add(addPriceField);
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
        modifyStockLabel = new JLabel("Stock");
        modifyStockLabel.setBounds(15, 70, 50, 15);
        modifyStockField = new JTextField();
        modifyStockField.setBounds(75, 70, 200, 20);
        modifyPriceLabel = new JLabel("Price");
        modifyPriceLabel.setBounds(15, 95, 50, 15);
        modifyPriceField = new JTextField();
        modifyPriceField.setBounds(75, 95, 200, 20);
        modifyButton = new JButton("Modify Product");
        modifyButton.setBounds(75, 125, 200, 70);
        modifyButton.setEnabled(false);
        modifyPanel.add(modifyNameLabel);
        modifyPanel.add(modifyNameField);
        modifyPanel.add(modifyStockLabel);
        modifyPanel.add(modifyStockField);
        modifyPanel.add(modifyPriceLabel);
        modifyPanel.add(modifyPriceField);
        modifyPanel.add(modifyButton);
        contentPanel.add(modifyPanel);
        modifyPanel.setBounds(320, 220,  300, 210);
        modifyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private void initButtons(){
        deleteButton = new JButton("Delete Products");
        deleteButton.setBounds(400, 130, 200, 70);
        deleteButton.setEnabled(false);
        contentPanel.add(deleteButton);
        backButton = new JButton("Back");
        backButton.setBounds(450, 10, 150, 70);
        contentPanel.add(backButton);
    }

    private void clearAddFields(){
        addNameField.setText("");
        addStockField.setText("");
        addPriceField.setText("");
    }

    private void clearModifyFields(){
        modifyNameField.setText("");
        modifyStockField.setText("");
        modifyPriceField.setText("");
    }

}
