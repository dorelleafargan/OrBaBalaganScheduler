package scheduler;

import data.Lead;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class ConsensusScheduler {

    public void scheduleConsensus(List<Lead> leads) {
        // מיון לפי עדיפות (מהגבוה לנמוך)
        leads.sort(Comparator.comparingInt(Lead::getPriority).reversed());

        // שמירת כל התאריכים שכבר שובצו
        Set<LocalDate> reservedDates = new HashSet<>();
        LocalDate start = nextWorkingDay(LocalDate.now());

        for (Lead lead : leads) {
            LocalDate candidate = start;

            // ננסה למצוא טווח תאריכים חופשי בהתאם למשך של הליד
            while (true) {
                List<LocalDate> proposedRange = getAvailableRange(candidate, lead.getDuration());

                if (isRangeFree(proposedRange, reservedDates)) {
                    // שיבוץ: נעדכן תאריך התחלה ונשריין את הטווח
                    lead.setScheduledDate(proposedRange.get(0));
                    reservedDates.addAll(proposedRange);
                    break;
                } else {
                    // נתקדם ליום העבודה הבא
                    candidate = nextWorkingDay(candidate.plusDays(1));
                }
            }
        }
    }

    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.FRIDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY;
    }

    private LocalDate nextWorkingDay(LocalDate date) {
        LocalDate next = date;
        while (isWeekend(next)) {
            next = next.plusDays(1);
        }
        return next;
    }

    private List<LocalDate> getAvailableRange(LocalDate start, int duration) {
        List<LocalDate> range = new ArrayList<>();
        LocalDate date = start;
        while (range.size() < duration) {
            if (!isWeekend(date)) {
                range.add(date);
            }
            date = date.plusDays(1);
        }
        return range;
    }

    private boolean isRangeFree(List<LocalDate> range, Set<LocalDate> reserved) {
        for (LocalDate date : range) {
            if (reserved.contains(date)) {
                return false;
            }
        }
        return true;
    }
}
