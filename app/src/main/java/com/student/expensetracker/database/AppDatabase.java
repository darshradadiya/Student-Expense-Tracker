package com.student.expensetracker.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.student.expensetracker.models.Expense;
import com.student.expensetracker.models.StudentProfile;
import com.student.expensetracker.utils.DateTimeUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Expense.class, StudentProfile.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract ExpenseDao expenseDao();
    public abstract ProfileDao profileDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "student_expense_tracker.db")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                populateSampleData(INSTANCE);
            });
        }
    };

    /**
     * Seeds sample student profile and expenses for MAD project demonstration.
     */
    public static void populateSampleData(AppDatabase db) {
        if (db == null) return;
        ProfileDao profileDao = db.profileDao();
        ExpenseDao expenseDao = db.expenseDao();

        // 1. Seed Student Profile
        StudentProfile student = new StudentProfile(
                "Rahul Patel",
                "rahul.patel@abc.edu",
                "ABC College",
                "BCA",
                "STU001",
                20000.0, // Initial Bank Balance ₹20,000
                12500.0  // Monthly Budget ₹12,500
        );
        profileDao.insert(student);

        // 2. Clear old expenses if resetting
        expenseDao.deleteAll();

        // 3. Seed Sample Expenses with current month dates
        String today = DateTimeUtils.getCurrentDateString();
        String yesterday = DateTimeUtils.getPastDateString(1);
        String twoDaysAgo = DateTimeUtils.getPastDateString(2);
        String fourDaysAgo = DateTimeUtils.getPastDateString(4);
        String sixDaysAgo = DateTimeUtils.getPastDateString(6);

        long now = System.currentTimeMillis();

        // Food: ₹1,500
        expenseDao.insert(new Expense("Food", 1500.0, "Food", today, "Hostel mess & canteen food", now));
        // Travel: ₹900
        expenseDao.insert(new Expense("Travel", 900.0, "Travel", yesterday, "Monthly bus pass & metro ticket", now - 86400000L));
        // Books: ₹1,200
        expenseDao.insert(new Expense("Books", 1200.0, "Books", twoDaysAgo, "Database Management & Java Books", now - 172800000L));
        // Shopping: ₹2,000
        expenseDao.insert(new Expense("Shopping", 2000.0, "Shopping", fourDaysAgo, "College uniform & stationery items", now - 345600000L));
        // Entertainment: ₹800
        expenseDao.insert(new Expense("Entertainment", 800.0, "Entertainment", sixDaysAgo, "Weekend movie & snacks with batchmates", now - 518400000L));
    }
}
