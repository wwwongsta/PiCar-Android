package com.example.picar.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.picar.database.entity.Transaction;
import com.example.picar.database.entity.Transit;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Insert
    void insertAll(List<Transaction> listTransaction);

    @Update
    void update(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("DELETE FROM transactions")
    void alldelete();

    @Query("SELECT * from transactions")
    List<Transaction> getListTransaction();

    @Query("SELECT * FROM transactions WHERE transaction_id LIKE :id LIMIT 1")
    Transaction get_Transaction_ByTransaction_id(int id);

}
