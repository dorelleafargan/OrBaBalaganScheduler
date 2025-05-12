package dao;

import model.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerFileDao implements IDao<Customer> {
    private final String path;

    public CustomerFileDao(String path) {
        this.path = path;
    }

    @Override
    public void add(Customer c) {
        List<Customer> existing = getAll();
        boolean exists = existing.stream()
                .anyMatch(customer -> customer.getClientNum() == c.getClientNum());
        if (exists) {
            System.out.println("⚠ לקוח כבר קיים, לא נוסף שוב: " + c.getClientNum());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(c.getClientNum() + ";" + c.getName() + ";" + c.getPhone() + ";" + c.getAddress() + ";" + c.getNotes());
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length != 5) {
                    System.err.println("⚠ שורה לא תקינה בקובץ הלקוחות: " + line);
                    continue;
                }
                list.add(new Customer(
                        Integer.parseInt(parts[0]),
                        parts[1], parts[2], parts[3], parts[4]
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void clearAll() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Customer c) {
        List<Customer> all = getAll();
        List<Customer> filtered = new ArrayList<>();

        for (Customer customer : all) {
            if (customer.getClientNum() != c.getClientNum()) {
                filtered.add(customer);
            }
        }

        clearAll();
        for (Customer customer : filtered) {
            add(customer);
        }
    }
}
