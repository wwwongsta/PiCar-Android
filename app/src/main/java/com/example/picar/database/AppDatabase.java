package com.example.picar.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.picar.database.dao.PositionDao;
import com.example.picar.database.dao.TransactionDao;
import com.example.picar.database.dao.TransitDao;
import com.example.picar.database.dao.UserDao;
import com.example.picar.database.dao.UserCarDao;
import com.example.picar.database.entity.Position;
import com.example.picar.database.entity.Transaction;
import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;
import com.example.picar.database.entity.UserCar;

@Database(entities = {  User.class, UserCar.class,
                        Transaction.class, Transit.class, Position.class}
                        ,version = 3, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract UserCarDao user_carDao();

    public abstract TransactionDao transactionDao();

    public abstract TransitDao transitDao();

    public abstract PositionDao positionDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "sample.db")
                            .fallbackToDestructiveMigration()
                            //.addMigrations(Migration1_2.newInstance(), Migration2_3.newInstance())
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}