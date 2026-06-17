package dao;

import entity.Customer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class CustomerDaoImpl implements CustomerDao {

    Map<Integer, Customer> map = new LinkedHashMap<>();

    @Override
    public void save(Customer customer) {
        map.put(customer.getCustomerId(), customer);
    }

    @Override
    public Customer findById(int customerId) {
        return map.get(customerId);
    }

    @Override
    public void updateById(int customerId, Customer customer) {
        map.put(customerId, customer);
    }

    @Override
    public void deleteById(int customerId) {
        map.remove(customerId);
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public Collection<Customer> findAll() {
        return map.values();
    }

    @Override
    public Collection<Customer> findByName(String customerName) {
        return map.values()
                .stream()
                .filter(customer ->
                        customer.getCustomerName()
                                .equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Customer> findByCity(String city) {
        return map.values()
                .stream()
                .filter(customer ->
                        customer.getCity()
                                .equalsIgnoreCase(city))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Customer> findByCityAndName(String city,
                                                  String customerName) {
        return map.values()
                .stream()
                .filter(customer ->
                        customer.getCity().equalsIgnoreCase(city)
                                &&
                                customer.getCustomerName()
                                        .equalsIgnoreCase(customerName))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Customer> sortByNameAsc() {
        return map.values()
                .stream()
                .sorted(Comparator.comparing(
                        Customer::getCustomerName))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Customer> sortByNameDesc() {
        return map.values()
                .stream()
                .sorted(Comparator.comparing(
                        Customer::getCustomerName).reversed())
                .collect(Collectors.toList());
    }
}