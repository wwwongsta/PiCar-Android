package com.example.picar.database;

import android.support.annotation.NonNull;

import com.example.picar.database.entity.Transaction;
import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;
import com.example.picar.database.entity.UserCar;

import java.util.List;

public class Dataloader {

    public static void populateSync(@NonNull final AppDatabase db) {

        final List<User> users = getUserForTest();
        db.userDao().insertAll(users);

        final List<UserCar> users_car = getUsersCarForTest();
        db.user_carDao().insertAll(users_car);

        final List<Transaction> transactions = getTransactionsForTest();
        db.transactionDao().insertAll(transactions);

        final List<Transit> transits = getTransitsForTest();
        db.transitDao().insertAll(transits);

    }
    private static List<Transit> getTransitsForTest() {
        
        return null;
    }

    private static List<Transaction> getTransactionsForTest() {
        return null;
    }

    private static List<UserCar> getUsersCarForTest() {
        return null;
    }

    private static List<User> getUserForTest() {
        return null;
    }

//
//    private static List<Carrier> getCarriersForTest(){
//        List<Carrier> carriers = new ArrayList<Carrier>();
//
//        carriers.add(new Carrier(1, "Air Canada"));
//        carriers.add(new Carrier(2, "Air France"));
//        carriers.add(new Carrier(3, "Air Transat"));
//        carriers.add(new Carrier(4, "Delta"));
//        carriers.add(new Carrier(5, "Sunwing"));
//
//
//        return carriers;
//    }
//
//    private static List<Travel> getTravelsForTest(){
//        List<Travel> travels = new ArrayList<Travel>();
//
//        travels.add(new Travel(1, 1, "2018-04-02", "Montreal", "Paris", "1200$", "7 jours"));
//        travels.add(new Travel(2, 1, "2018-04-21", "Toronto", "Lyon", "800$", "14 jours"));
//        travels.add(new Travel(3, 1, "2018-05-07", "Vancouver", "Pekin", "2100$", "10 jours"));
//        travels.add(new Travel(4, 2, "2018-04-01", "Lyon", "Barcelone", "400$", "7 jours"));
//        travels.add(new Travel(5, 2, "2018-04-12", "Marseille", "Belfast", "900$", "14 jours"));
//        travels.add(new Travel(6, 3, "2018-05-17", "Londres", "Mosc
//}
}