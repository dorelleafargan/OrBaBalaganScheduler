package test;

import data.Lead;
import dao.IDao;
import dao.MyLeadFileImpl;
import model.Customer;
import dao.CustomerFileDao;
import service.CustomerService;
import service.LeadManagementService;
import scheduler.ConsensusScheduler;
import taskscheduler.FCFSScheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class TestLeadManagementService {
    public static void main(String[] args) {
        System.out.println("🧹 Cleaning existing leads...");
        IDao<Lead> dao = new MyLeadFileImpl("data/DataSource.txt");
        dao.clearAll();

        IDao<Customer> customerDao = new CustomerFileDao("data/Customers.txt");
        CustomerService customerService = new CustomerService(customerDao);

        customerDao.clearAll();
        customerService.addCustomer(new Customer(1001, "דניאל", "050-1234567", "תל אביב", ""));
        customerService.addCustomer(new Customer(1002, "יואב", "050-2222222", "חיפה", ""));
        customerService.addCustomer(new Customer(1003, "אורית", "050-3333333", "ירושלים", ""));
        customerService.addCustomer(new Customer(1004, "גיא","050-3333313","מודיעין" ,"לקוח דחוף") );

        LeadManagementService service = new LeadManagementService(dao, new FCFSScheduler());

        List<Lead> leads = new ArrayList<>();
        leads.add(new Lead(1001, "דוראל", "050-1234567", 8, LocalDate.now()));
        leads.add(new Lead(1002, "ניסים", "050-2222222", 6, LocalDate.now()));
        leads.add(new Lead(1003, "ברמי", "050-3333333", 9, LocalDate.now()));
        leads.add(new Lead(1004, "גיא", "050-3333313", 10, LocalDate.now()));

        new ConsensusScheduler().scheduleConsensus(leads);
        service.replaceAll(leads);

        System.out.println("📋 All scheduled leads:");
        for (Lead l : service.getAllLeads()) {
            System.out.println("✅ " + l.getName() + " (Client Number: " + l.getClientNum() + ") → " + l.getScheduledDate());
        }
    }
}