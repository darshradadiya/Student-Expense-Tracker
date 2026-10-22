package com.student.expensetracker.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.student.expensetracker.models.StudentProfile;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ProfileDao_Impl implements ProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<StudentProfile> __insertionAdapterOfStudentProfile;

  private final EntityDeletionOrUpdateAdapter<StudentProfile> __updateAdapterOfStudentProfile;

  private final SharedSQLiteStatement __preparedStmtOfUpdateBalances;

  public ProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStudentProfile = new EntityInsertionAdapter<StudentProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `student_profile` (`id`,`name`,`email`,`college`,`course`,`student_id`,`initial_bank_balance`,`monthly_budget`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StudentProfile entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getCollege() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCollege());
        }
        if (entity.getCourse() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCourse());
        }
        if (entity.getStudentId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStudentId());
        }
        statement.bindDouble(7, entity.getInitialBankBalance());
        statement.bindDouble(8, entity.getMonthlyBudget());
      }
    };
    this.__updateAdapterOfStudentProfile = new EntityDeletionOrUpdateAdapter<StudentProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `student_profile` SET `id` = ?,`name` = ?,`email` = ?,`college` = ?,`course` = ?,`student_id` = ?,`initial_bank_balance` = ?,`monthly_budget` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final StudentProfile entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getName() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getName());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEmail());
        }
        if (entity.getCollege() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCollege());
        }
        if (entity.getCourse() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCourse());
        }
        if (entity.getStudentId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getStudentId());
        }
        statement.bindDouble(7, entity.getInitialBankBalance());
        statement.bindDouble(8, entity.getMonthlyBudget());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateBalances = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE student_profile SET initial_bank_balance = ?, monthly_budget = ? WHERE id = 1";
        return _query;
      }
    };
  }

  @Override
  public void insert(final StudentProfile profile) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStudentProfile.insert(profile);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final StudentProfile profile) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfStudentProfile.handle(profile);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateBalances(final double bankBalance, final double monthlyBudget) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateBalances.acquire();
    int _argIndex = 1;
    _stmt.bindDouble(_argIndex, bankBalance);
    _argIndex = 2;
    _stmt.bindDouble(_argIndex, monthlyBudget);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfUpdateBalances.release(_stmt);
    }
  }

  @Override
  public LiveData<StudentProfile> getProfile() {
    final String _sql = "SELECT * FROM student_profile WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"student_profile"}, false, new Callable<StudentProfile>() {
      @Override
      @Nullable
      public StudentProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfCollege = CursorUtil.getColumnIndexOrThrow(_cursor, "college");
          final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
          final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
          final int _cursorIndexOfInitialBankBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "initial_bank_balance");
          final int _cursorIndexOfMonthlyBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthly_budget");
          final StudentProfile _result;
          if (_cursor.moveToFirst()) {
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpCollege;
            if (_cursor.isNull(_cursorIndexOfCollege)) {
              _tmpCollege = null;
            } else {
              _tmpCollege = _cursor.getString(_cursorIndexOfCollege);
            }
            final String _tmpCourse;
            if (_cursor.isNull(_cursorIndexOfCourse)) {
              _tmpCourse = null;
            } else {
              _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
            }
            final String _tmpStudentId;
            if (_cursor.isNull(_cursorIndexOfStudentId)) {
              _tmpStudentId = null;
            } else {
              _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
            }
            final double _tmpInitialBankBalance;
            _tmpInitialBankBalance = _cursor.getDouble(_cursorIndexOfInitialBankBalance);
            final double _tmpMonthlyBudget;
            _tmpMonthlyBudget = _cursor.getDouble(_cursorIndexOfMonthlyBudget);
            _result = new StudentProfile(_tmpName,_tmpEmail,_tmpCollege,_tmpCourse,_tmpStudentId,_tmpInitialBankBalance,_tmpMonthlyBudget);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _result.setId(_tmpId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public StudentProfile getProfileSync() {
    final String _sql = "SELECT * FROM student_profile WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfCollege = CursorUtil.getColumnIndexOrThrow(_cursor, "college");
      final int _cursorIndexOfCourse = CursorUtil.getColumnIndexOrThrow(_cursor, "course");
      final int _cursorIndexOfStudentId = CursorUtil.getColumnIndexOrThrow(_cursor, "student_id");
      final int _cursorIndexOfInitialBankBalance = CursorUtil.getColumnIndexOrThrow(_cursor, "initial_bank_balance");
      final int _cursorIndexOfMonthlyBudget = CursorUtil.getColumnIndexOrThrow(_cursor, "monthly_budget");
      final StudentProfile _result;
      if (_cursor.moveToFirst()) {
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final String _tmpEmail;
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _tmpEmail = null;
        } else {
          _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        }
        final String _tmpCollege;
        if (_cursor.isNull(_cursorIndexOfCollege)) {
          _tmpCollege = null;
        } else {
          _tmpCollege = _cursor.getString(_cursorIndexOfCollege);
        }
        final String _tmpCourse;
        if (_cursor.isNull(_cursorIndexOfCourse)) {
          _tmpCourse = null;
        } else {
          _tmpCourse = _cursor.getString(_cursorIndexOfCourse);
        }
        final String _tmpStudentId;
        if (_cursor.isNull(_cursorIndexOfStudentId)) {
          _tmpStudentId = null;
        } else {
          _tmpStudentId = _cursor.getString(_cursorIndexOfStudentId);
        }
        final double _tmpInitialBankBalance;
        _tmpInitialBankBalance = _cursor.getDouble(_cursorIndexOfInitialBankBalance);
        final double _tmpMonthlyBudget;
        _tmpMonthlyBudget = _cursor.getDouble(_cursorIndexOfMonthlyBudget);
        _result = new StudentProfile(_tmpName,_tmpEmail,_tmpCollege,_tmpCourse,_tmpStudentId,_tmpInitialBankBalance,_tmpMonthlyBudget);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
