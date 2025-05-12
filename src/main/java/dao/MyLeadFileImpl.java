package dao;
import data.Lead;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * מימוש של IDao לשמירת לידים בקובץ טקסט מקומי.
 * משתמש בסטרים של Java לצורך קריאה וכתיבה של אובייקטים.
 */

public class MyLeadFileImpl implements IDao<Lead> {
    private final File file;

    public MyLeadFileImpl(String path) {
        this.file = new File(path);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); // מוסיף תיקיות חסרות
                file.createNewFile(); // יוצר את הקובץ עצמו
                saveToFile(new ArrayList<>()); // אתחול ריק
            } catch (IOException e) {
                throw new RuntimeException("Failed to create file", e);
            }
        }
    }

    @Override
    public void add(Lead item){
        List<Lead> leads = getAll();
        leads.add(item);
        saveToFile(leads);
    }

    @Override
    public void remove(Lead item){
        List<Lead> leads = getAll();
        leads.remove(item);
        saveToFile(leads);
        System.out.println("✅ Saved lead to file: " + item.getId());
    }

    @Override
    public List<Lead> getAll() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Lead>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // אם הקובץ ריק או שגוי – מחזיר רשימה ריקה
        }
    }

    private void saveToFile(List<Lead> leads) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(leads);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file", e);
        }
    }

    @Override
    public void clearAll(){
        saveToFile(new ArrayList<>());
    }
}
