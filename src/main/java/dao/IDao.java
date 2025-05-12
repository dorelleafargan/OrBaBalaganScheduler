package dao;
import model.Customer;
import java.util.List;

/** ממשק לגישה לנתונים, הוספה, מחיקה, שליפה.
 *
 * @param <T>  למשל LEAD
 */
public interface IDao<T> {
    void add(T item);
    void remove(T item);
    List<T> getAll();
    // הוספתי לצורך בדיקות
    void clearAll();
    }
