package com.example.picar.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.picar.database.entity.Position;
import com.example.picar.database.entity.Transaction;

import java.util.List;

@Dao
public interface PositionDao {

    @Insert
    void insert(Position position);

    @Insert
    void insertAll(List<Position> listPosition);

    @Update
    void update(Position position);

    @Delete
    void delete(Position position);

    @Query("DELETE FROM positions")
    void alldelete();

    @Query("SELECT * from positions")
    List<Position> getListPosition();

    @Query("SELECT * FROM positions WHERE id LIKE :id LIMIT 1")
    Transaction get_Position_ByPositionid(int id);
}
