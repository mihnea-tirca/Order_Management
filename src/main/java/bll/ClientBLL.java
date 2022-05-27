package bll;

import bll.validators.Validator;
import dao.ClientDAO;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientBLL {

    private List<Validator<Client>> validators;
    private ClientDAO clientDAO;

    public ClientBLL(){
        validators = new ArrayList<>();
        clientDAO = new ClientDAO();
    }

    public Client findClientByID(int id){
        Client client = clientDAO.findByID(id);
        if(client == null){
            throw new NoSuchElementException("The client with id = " + id + " was not found");
        }
        return client;
    }

    public List<Client> findAllClients(){
        List<Client> clientList = clientDAO.findAll();
        if(clientList == null){
            throw new NoSuchElementException("There are no clients!");
        }
        return clientList;
    }

    public int insertClient(Client client){
        return clientDAO.insert(client);
    }

    public void updateClient(Client client){
        clientDAO.update(client);
    }

    public void deleteClient(Client client){
        clientDAO.delete(client);
    }

    public void validate(Client client){
        for(Validator<Client> validator : validators){
            validator.validate(client);
        }
    }

    public DefaultTableModel initClientsTable(){
        return clientDAO.initTable();
    }
}
