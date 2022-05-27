package bll;

import bll.validators.Validator;
import dao.OrderDAO;
import model.Client;
import model.Order;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class OrderBLL {

    private List<Validator<Order>> validators;
    private OrderDAO orderDAO;

    public OrderBLL(){
        validators = new ArrayList<>();
        orderDAO = new OrderDAO();
    }

    public Order findOrderByID(int id){
        Order order = orderDAO.findByID(id);
        if(order == null){
            throw new NoSuchElementException("The order with id = " + id + " was not found");
        }
        return order;
    }

    public List<Order> findAllOrders(){
        List<Order> orderList = orderDAO.findAll();
        if(orderList == null){
            throw new NoSuchElementException("There are no orders!");
        }
        return orderList;
    }

    public int insertOrder(Order order){
        return orderDAO.insert(order);
    }

    public void deleteOrder(Order order){
        orderDAO.delete(order);
    }
    
    public void validate(Order order){
        for(Validator<Order> validator : validators){
            validator.validate(order);
        }
    }

    public DefaultTableModel initOrdersTable(){
        return orderDAO.initTable();
    }
}
