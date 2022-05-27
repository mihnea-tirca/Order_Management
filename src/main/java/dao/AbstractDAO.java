package dao;

import java.awt.*;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import connection.ConnectionFactory;
import model.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    /**
     * @return a String representing the query template for selecting entries by the value of "id"
     */
    private String createSelectQuery() {
        return "SELECT " +
                " * " +
                " FROM " +
                type.getSimpleName() +
                " WHERE id = ?";
    }

    /**
     * @return a String representing the query for selecting all entries
     */
    private String createSelectAllQuery(){
        return "SELECT * FROM `" +
                type.getSimpleName() +
                "`";
    }

    /**
     * @return a String representing the query template for inserting an entry of type T
     */
    private String createInsertQuery(){
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO `" + type.getSimpleName() + "` (");

        for(Field field : type.getDeclaredFields()){
            if(field.getName().equals("id"))
                continue;
            field.setAccessible(true);
            stringBuilder.append(field.getName()).append(", ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(") VALUES (");

        for(Field field: type.getDeclaredFields()){
            if(field.getName().equals("id"))
                continue;
            field.setAccessible(true);
            stringBuilder.append("?, ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    /**
     * @return a String representing the query template for updating an entry of type T
     */
    private String createUpdateQuery(){
        StringBuilder stringBuilder = new StringBuilder("UPDATE `" + type.getSimpleName() + "` SET ");

        for(Field field: type.getDeclaredFields()){
            if(field.getName().equals("id"))
                continue;
            field.setAccessible(true);
            stringBuilder.append(field.getName()).append(" = ?, ");
        }
        stringBuilder.setLength(stringBuilder.length() - 2);
        stringBuilder.append(" WHERE id = ?");

        return stringBuilder.toString();
    }

    /**
     * @return a String representing the query template for deleting an entry of type T
     */
    private String createDeleteQuery(){
        return "DELETE FROM `" +
                type.getSimpleName() +
                "` WHERE id = ?";
    }

    /**
     * @param t the object to be inserted
     * @return the id of the inserted entry
     */
    public int insert(T t) {
        int idValue = 0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createInsertQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int index = 1;
            for (Field field : type.getDeclaredFields() ) {
                if(field.getName().equals("id"))
                    continue;
                field.setAccessible(true);
                Object value = field.get(t);
                String valueType = value.getClass().getSimpleName();
                switch (valueType) {
                    case "String" -> statement.setString(index, value.toString());
                    case "Double" -> statement.setDouble(index, Double.parseDouble(value.toString()));
                    case "Integer" -> statement.setInt(index, Integer.parseInt(value.toString()));
                }
                index++;
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if(rs.next()){
                idValue = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return idValue;
    }

    /**
     * @param t the object to be updated
     */
    public void update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createUpdateQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int index = 1;
            Object idValue = -1;
            for (Field field : type.getDeclaredFields() ) {
                field.setAccessible(true);
                if(field.getName().equals("id")) {
                    idValue = field.get(t);
                    continue;
                }

                Object value = field.get(t);
                String valueType = value.getClass().getSimpleName();

                switch (valueType) {
                    case "String" -> statement.setString(index, value.toString());
                    case "Double" -> statement.setDouble(index, Double.parseDouble(value.toString()));
                    case "Integer" -> statement.setInt(index, Integer.parseInt(value.toString()));
                }
                index++;
            }
            statement.setInt(index, Integer.parseInt(idValue.toString()));
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * @param t the object to be deleted
     */
    public void delete(T t){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createDeleteQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            Field idField = type.getDeclaredFields()[0];
            idField.setAccessible(true);
            int idToDelete = Integer.parseInt(idField.get(t).toString());
            statement.setInt(1, idToDelete);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:deleteClient " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * @return all entries from the database from the table corresponding to type T
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * @param id the searched id
     * @return the entry from the database from the table corresponding to type T with the id equal to the passed parameter
     */
    public T findByID(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * creates a list of objects from the result set passed as parameter
     * @param resultSet the result set from which to create the objects
     * @return a list with the created objects
     */
    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * initializes the table with entries of type T
     * @return the DefaultTableModel of the table
     */
    public DefaultTableModel initTable(){
        String[] columnNames = new String[type.getDeclaredFields().length];
        int idx = 0;
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            columnNames[idx] = field.getName();
            idx++;
        }

        List<T> tableEntryList = findAll();
        Object[][] data = new Object[tableEntryList.size()][idx];

        int index = 0;
        for(T t: tableEntryList){
            ArrayList<Object> rowData = new ArrayList<>();
            for(Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object value = field.get(t);
                    rowData.add(value);
                } catch (Exception e) {e.printStackTrace();}

            }
            data[index] = rowData.toArray();
            index++;
        }
        return new DefaultTableModel(data, columnNames);
    }
}
