
package service;

import data.Lead;
import dao.IDao;

import java.time.LocalDate;
import java.util.List;

/**
 * שירות ניהול לידים – אחראי על הוספה, שליפה, תיעוד ושיבוץ.
 */
public class LeadManagementService {
    private final IDao<Lead> leadDao;

    public LeadManagementService(IDao<Lead> leadDao, Object unusedScheduler) {
        this.leadDao = leadDao;
    }

    public void addLead(Lead lead) {
        leadDao.add(lead);
    }

    public List<Lead> getAllLeads() {
        return leadDao.getAll();
    }

    public void replaceAll(List<Lead> leads) {
        leadDao.clearAll();
        for (Lead l : leads) {
            leadDao.add(l);
        }
    }

    public void clearAll() {
        leadDao.clearAll();
    }
}
