package com.example.picar.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.picar.database.entity.Transit;
import com.example.picar.database.entity.User;

import java.util.List;

@Dao
public interface TransitDao {

    @Insert
    void insert(Transit transit);

    @Insert
    void insertAll(List<Transit> listTransit);

    @Update
    void update(Transit transit);

    @Delete
    void delete(Transit transit);

    @Query("DELETE FROM transits")
    void alldelete();

    @Query("SELECT * from transits")
    List<Transit> getListTransit();

    @Query("SELECT * FROM transits WHERE driverId LIKE :id LIMIT 1")
    Transit get_Transits_ByDriver_id(int id);

}
