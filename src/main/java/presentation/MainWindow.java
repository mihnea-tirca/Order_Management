package presentation;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public JPanel contentPanel;
    public JButton viewClients;
    public JButton viewProducts;
    public JButton viewOrders;

    public MainWindow(){
        super("Main Window");
        initGUI();
        addActionListeners();
    }

    private void initGUI(){
        setSize(300, 300);

        contentPanel = new JPanel(new GridLayout(3, 1));
        setContentPane(contentPanel);

        viewClients = new JButton("Clients Operations");
        contentPanel.add(viewClients);

        viewProducts = new JButton("Products Operations");
        contentPanel.add(viewProducts);

        viewOrders = new JButton("Orders Operations");
        contentPanel.add(viewOrders);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addActionListeners(){
        viewClients.addActionListener(e -> {
            setVisible(false);
            new ClientsOperationsWindow().setVisible(true);
        });

        viewProducts.addActionListener(e -> {
            dispose();
            new ProductsOperationsWindow().setVisible(true);
        });

        viewOrders.addActionListener(e -> {
            dispose();
            new OrdersOperationsWindow().setVisible(true);
        });
    }
}
