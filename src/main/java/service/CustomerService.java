package service;

import dao.IDao;
import model.Customer;

import java.util.List;

public class CustomerService {
    private final IDao<Customer> dao;

    public CustomerService(IDao<Customer> dao) {
        this.dao = dao;
    }

    public void addCustomer(Customer customer) {
        dao.add(customer);
    }

    public List<Customer> getAll() {
        return dao.getAll();
    }

    public void clearAll() {
        dao.clearAll();
    }

    public void removeCustomer(Customer customer) {
        dao.remove(customer);
    }
}
