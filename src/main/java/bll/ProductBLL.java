package bll;

import bll.validators.ProductStockValidator;
import bll.validators.Validator;
import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ProductBLL {

    private List<Validator<Product>> validators;
    private ProductDAO productDAO;

    public ProductBLL() {
        validators = new ArrayList<>();
        validators.add(new ProductStockValidator());
        productDAO = new ProductDAO();
    }

    public Product findProductByID(int id) {
        Product product = productDAO.findByID(id);
        if (product == null) {
            throw new NoSuchElementException("The product with id = " + id + " was not found");
        }
        return product;
    }

    public List<Product> findAllProducts() {
        List<Product> productList = productDAO.findAll();
        if (productList == null) {
            throw new NoSuchElementException("There are no products!");
        }
        return productList;
    }

    public int insertProduct(Product product) {
        return productDAO.insert(product);
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    public void deleteProduct(Product product) {
        productDAO.delete(product);
    }

    public void validate(Product product) {
        for (Validator<Product> validator : validators) {
            validator.validate(product);
        }
    }

    public DefaultTableModel initProductsTable(){
        return productDAO.initTable();
    }
}
