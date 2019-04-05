package com.example.picar.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.picar.database.entity.UserCar;

import java.util.List;
@Dao
public interface UserCarDao {

    @Insert
    void insert(UserCar userCar);

    @Insert
    void insertAll(List<UserCar> listUserCar);

    @Update
    void update(UserCar userCar);

    @Delete
    void delete(UserCar userCar);

    @Query("DELETE FROM users_car")
    void alldelete();

    @Query("SELECT * from users_car ORDER BY brand ASC")
    List<UserCar> getListUser_car();

    @Query("SELECT * FROM users_car WHERE brand LIKE :brand LIMIT 1")
    UserCar get_UserCar_ByBrand(String brand);

}
