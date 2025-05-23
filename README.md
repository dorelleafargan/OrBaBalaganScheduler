# 🌟 אור בבלגן - מערכת ניהול לידים

מערכת לניהול לידים עבור שירותי סידור, ארגון ופתרונות אחסון בבית.  
המערכת כוללת ממשק משתמש גרפי (GUI) ב־Java Swing, תקשורת לקוח-שרת, שיבוץ לידים חכם, ויכולת ניתוח נתונים.

---

## 🧰 טכנולוגיות:
- Java 17+
- Java Swing (GUI)
- Socket (TCP)
- JSON באמצעות GSON
- ארכיטקטורת MVC
- קבצי טקסט לשמירת נתונים (`DataSource.txt`, `Customers.txt`)

---

## 📦 מבנה הפרויקט:

```
OrBaBalaganProject/
│
├── src/
│   ├── client/                 # צד לקוח (UI + תקשורת)
│   │   ├── view/               # פאנלים גרפיים (Add, View, Analytics)
│   │   ├── network/            # שליחת בקשות לשרת
│   │   └── ClientApp.java      # נקודת כניסה של הלקוח
│   │
│   ├── controller/             # שליטה ובקרה על פעולות השרת
│   ├── data/                   # מודל Lead (יורש מ-Task)
│   ├── dao/                    # קריאה וכתיבה לקבצי טקסט
│   ├── model/                  # Request/Response + Customer
│   ├── scheduler/              # שיבוץ לפי אלגוריתם Consensus
│   ├── network/                # צד השרת: Server, HandleRequest
│   └── ServerDriver.java       # נקודת כניסה של השרת
│
├── data/
│   ├── DataSource.txt          # נתוני לידים
│   └── Customers.txt           # נתוני לקוחות
│
└── assets/
    ├── background.jpg          # רקע מותאם ללוגו
    └── logo.png                # לוגו "אור בבלגן"
```

---

## 🧪 תכונות עיקריות:

### ✅ הוספת ליד
- שם ושם משפחה, טלפון, כתובת, עדיפות, משך בימים, הערות
- הליד נשלח לשרת, משובץ באופן אוטומטי ומתווסף לקובץ הנתונים

### ✅ צפייה בלידים
- טבלה המציגה את כל הלידים, כולל מידע מלא
- לחצן עריכה מאפשר עדכון פרטי ליד קיים

### ✅ אנליטיקה
- הצגת סיכום סטטיסטי (כמות, ממוצעים, תאריכי שיבוץ וכו') לפי נתוני השרת

### ✅ תמיכה מלאה בעברית (RTL)
- יישור רכיבים, טקסטים, טבלאות ותפריטים לימין
- ממשק נוח ומעוצב בהתאמה ללוגו "אור בבלגן"

---

## 🖼️ מיתוג
- לוגו מקורי מוצג בראש החלון
- רקע עיצובי עם התמונה העסקית שלך
- שימוש באייקונים בתפריטים לשיפור חוויית משתמש

---

## 🚀 הרצה

### הרצת השרת:
```bash
java controller.ServerDriver
```

### הרצת הלקוח:
```bash
java client.ClientApp
```

ודא שכל הקבצים (`DataSource.txt`, `Customers.txt`, התמונות) זמינים באותו פרויקט או נגישים דרך ClassPath.

---

## 👩‍💼 פרויקט נבנה עבור:
**אור בבלגן** – ארגון הבית, פתרונות אחסון, מעבר דירות  
✉️ דף עסקי, פייסבוק ואינסטגרם: @or.babalagan

---

## 📄 רישוי
כל הזכויות שמורות © 2025 לאור בבלגן  
הפרויקט פותח לצורך שימוש פנימי ואינו להפצה מסחרית ללא אישור מפורש.
