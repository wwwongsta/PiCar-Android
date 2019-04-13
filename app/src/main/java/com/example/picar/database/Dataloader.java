package com.example.picar.database;

import android.support.annotation.NonNull;

import com.example.picar.database.entity.Transaction;
import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;
import com.example.picar.database.entity.UserCar;

import java.util.ArrayList;
import java.util.List;

public class Dataloader {

    public static void populateSync(@NonNull final AppDatabase db) {
//
//        db.transitDao().alldelete();
//        db.transactionDao().alldelete();
//        db.user_carDao().alldelete();
//        db.userDao().alldelete();
//
//        final List<User> users = getUserForTest();
//
//        db.userDao().insertAll(users);
//
//        final List<UserCar> users_car = getUsersCarForTest();
//        db.user_carDao().insertAll(users_car);
//
//        final List<Transaction> transactions = getTransactionsForTest();
//        db.transactionDao().insertAll(transactions);
//
//        final List<Transit> transits = getTransitsForTest();
//        db.transitDao().insertAll(transits);
//
//    }
//
//
//    private static List<Transit> getTransitsForTest() {
//        List<Transit> transits = new ArrayList<Transit>();
//        transits.add(new Transit(1, "Métro Cartier à Métro Crémazie"));
//        transits.add(new Transit(2, "Vieux port de Montréal à Métro Cartier"));
//        transits.add(new Transit(3, "Palais de justice de Laval à Jardin botanique de Montréal"));
//
//        return transits;
//    }
//
//    private static List<Transaction> getTransactionsForTest() {
//        List<Transaction> transactions = new ArrayList<Transaction>();
//        transactions.add(new Transaction(1, "Ben10", "Peter", "Crédit", "2018-05-22"));
//        transactions.add(new Transaction(2, "Ness", "Olimar", "Intérac", "2018-08-08"));
//        transactions.add(new Transaction(3, "Naruto", "Sasuke", "Intérac", "2018-02-10"));
//
//        return transactions;
//    }
//
//    private static List<UserCar> getUsersCarForTest() {
//        List<UserCar> users_car = new ArrayList<UserCar>();
//        users_car.add(new UserCar(1, 1, "Path/Photo/Auto/Here", "Rouge", "Toyota", "H7S 123", "Berlin"));
//        users_car.add(new UserCar(2, 3, "Path/Photo/Auto/Here", "Jaune", "Nissan", "1KK LPT", "PICK-UP"));
//        users_car.add(new UserCar(3, 4, "Path/Photo/Auto/Here", "Noir", "Pontiac", "3DF ASD", "SUV"));
//
//        return users_car;
//    }
//
//    private static List<User> getUserForTest() {
//
//        List<User> users = new ArrayList<User>();
//        users.add(new User(1, 123, "Ben10", "Tennyson", "514-0343-3432", "ben@10.com", "mamasita10", "Path/Photo/User/Here",1));
//        users.add(new User(2, 423, "Peter", "Packer", "450-958-9898", "Peter@Packer.com", "papasito12", "Path/Photo/User/Here",2));
//        users.add(new User(3, 567, "Ness", "Smash","438-302-1201", "Naruto@Uzumaki.com", "CelinDion", "Path/Photo/User/Here",2));
//        users.add(new User(4, 876,"Olimar", "Bros", "956-320-1201", "Sasuke@Uchiha.com", "Uselase", "Path/Photo/User/Here",2));
//        users.add(new User(5, 678, "Naruto", "Uzumaki",  "845-320-3020", "Ness@Smash.com", "Smashqw", "Path/Photo/User/Here",2));
//        users.add(new User(6, 889, "Sasuke", "Uchiha",  "620-987-2121", "Olimar@Smash.com", "Poloer", "Path/Photo/User/Here",2));
//
//        return users;
//
//
//    }

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
    }
}