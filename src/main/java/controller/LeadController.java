package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.IDao;
import dao.MyLeadFileImpl;
import data.Lead;
import dao.CustomerFileDao;
import model.LocalDateAdapter;
import model.Request;
import model.Response;
import service.LeadManagementService;
import service.CustomerService;
import scheduler.ConsensusScheduler;
import taskscheduler.FCFSScheduler;
import taskscheduler.ITaskScheduler;
import model.Customer;
import service.AnalyticsService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class LeadController implements BaseController {
    private final LeadManagementService service;
    private final Gson gson;
    private final CustomerService customerService;

    public LeadController() {
        IDao<Lead> dao = new MyLeadFileImpl("data/DataSource.txt");
        ITaskScheduler scheduler = new FCFSScheduler();
        this.service = new LeadManagementService(dao, scheduler);

        IDao<Customer> customerDao = new CustomerFileDao("data/Customers.txt");
        this.customerService = new CustomerService(customerDao);

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    public Response handle(Request request) {
        try {
            String action = request.getHeaders().get("action");

            switch (action) {
                case "lead/add":
                    Lead lead = gson.fromJson(request.getBody(), Lead.class);
                    if (lead == null) {
                        return new Response(400, "Invalid lead data", "");
                    }

                    boolean customerExists = customerService.getAll().stream()
                            .anyMatch(c -> c.getClientNum() == lead.getClientNum());
                    if (!customerExists) {
                        Customer newCustomer = new Customer(
                                lead.getClientNum(),
                                lead.getId(),
                                lead.getPhone(),
                                "לא צוינה כתובת",
                                ""
                        );
                        customerService.addCustomer(newCustomer);
                    }

                    List<Lead> leads = service.getAllLeads();
                    leads.removeIf(l -> l.getClientNum() == lead.getClientNum());
                    leads.add(lead);

                    new ConsensusScheduler().scheduleConsensus(leads);
                    service.replaceAll(leads);

                    Lead scheduled = leads.stream()
                            .filter(l -> l.getClientNum() == lead.getClientNum())
                            .findFirst()
                            .orElse(null);

                    return new Response(200, "Lead added and scheduled", gson.toJson(scheduled));

                case "lead/getAll":
                    List<Lead> allLeads = service.getAllLeads();
                    return new Response(200, "All leads", gson.toJson(allLeads));

                case "lead/updateDate":
                    Map<String, String> updateMap = gson.fromJson(request.getBody(), Map.class);
                    String id = updateMap.get("id");
                    String dateStr = updateMap.get("date");
                    LocalDate newDate = LocalDate.parse(dateStr);

                    List<Lead> modifiable = service.getAllLeads();
                    for (Lead l : modifiable) {
                        if (l.getId().equals(id)) {
                            l.setScheduledDate(newDate);
                            break;
                        }
                    }

                    service.replaceAll(modifiable);
                    return new Response(200, "Scheduled date updated", "");

                case "lead/analytics":
                    List<Lead> analyticsLeads = service.getAllLeads();
                    AnalyticsService analytics = new AnalyticsService();

                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintStream temp = new PrintStream(out);
                    PrintStream original = System.out;
                    System.setOut(temp);

                    analytics.printSummary(analyticsLeads);

                    System.setOut(original);
                    String summary = out.toString("UTF-8");
                    return new Response(200, "Lead analytics summary", summary);

                default:
                    return new Response(400, "Unknown action", "");
            }

        } catch (Exception e) {
            System.err.println("❌ LeadController error: " + e.getMessage());
            e.printStackTrace();
            return new Response(500, "Controller error: " + e.getMessage(), "");
        }
    }
}
