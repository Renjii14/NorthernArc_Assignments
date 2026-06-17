package dao;

import entity.Customer;

import java.util.Collection;

public interface CustomerDao {

    void save(Customer customer);

    Customer findById(int customerId);

    void updateById(int customerId, Customer customer);

    void deleteById(int customerId);

    void deleteAll();

    Collection<Customer> findAll();

    Collection<Customer> findByName(String customerName);

    Collection<Customer> findByCity(String city);

    Collection<Customer> findByCityAndName(String city, String customerName);

    Collection<Customer> sortByNameAsc();

    Collection<Customer> sortByNameDesc();
}