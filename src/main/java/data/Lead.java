package data;
import taskscheduler.Task;
import java.io.Serializable;
import java.time.LocalDate;
import model.Customer;

/**
 * מייצג ליד (לקוח פוטנציאלי) במערכת "אור בבלגן".
 */
public class Lead extends Task implements Serializable {
    private int clientNum;
    private String phone;
    private LocalDate creationDate;
    private LocalDate scheduledDate;
    private Customer customer;
    private String notes;

    public Lead(int clientNum, String name, String phone, int qualityScore, LocalDate creationDate) {
        super(name, 1, qualityScore); // id = name, duration = 1, priority = qualityScore
        this.clientNum = clientNum;
        this.phone = phone;
        this.creationDate = creationDate;
        this.scheduledDate = null;
    }

    public int getClientNum() { return clientNum; }
    public void setClientNum(int clientNum) { this.clientNum = clientNum; }

    public String getPhone() { return phone; }
    public LocalDate getCreationDate() { return creationDate; }
    public LocalDate getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDate scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getName() { return getId(); }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Lead{name='" + getId() + '\'' +
                ", phone='" + phone + '\'' +
                ", priority=" + getPriority() +
                ", creationDate=" + creationDate +
                ", scheduledDate=" + scheduledDate +
                '}';
    }
}
