package service;

import data.Lead;

import java.util.List;
import java.util.stream.Collectors;

public class AnalyticsService {

    public int countLeads(List<Lead> leads) {
        return leads.size();
    }

    public double averagePriority(List<Lead> leads) {
        return leads.stream()
                .mapToInt(Lead::getPriority)
                .average()
                .orElse(0);
    }

    public double averageDuration(List<Lead> leads) {
        return leads.stream()
                .mapToInt(Lead::getDuration)
                .average()
                .orElse(0);
    }

    public List<Lead> unassignedLeads(List<Lead> leads) {
        return leads.stream()
                .filter(l -> l.getScheduledDate() == null)
                .collect(Collectors.toList());
    }

    public void printSummary(List<Lead> leads) {
        int total = countLeads(leads);
        int unassigned = unassignedLeads(leads).size();
        double avgPriority = averagePriority(leads);
        double avgDuration = averageDuration(leads);

        System.out.println("📊 סיכום לידים:");
        System.out.println("- סך לידים: " + total);
        System.out.println("- לידים לא שובצו: " + unassigned);
        System.out.printf("- ממוצע עדיפות: %.2f%n", avgPriority);
        System.out.printf("- ממוצע משך: %.2f ימים%n", avgDuration);
    }
}
