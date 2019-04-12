package com.example.picar.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.picar.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);
    @Insert
    void insertAll(List<User> listUser);
    @Update
    void update(User user);
    @Delete
    void delete(User user);
    @Query("DELETE FROM users")
    void alldelete();
    @Query("SELECT * from users ORDER BY name ASC")
    List<User> getListUser();
    @Query("SELECT * FROM users WHERE name LIKE :email LIMIT 1")
    User get_User_ByEmail(String email);

}
