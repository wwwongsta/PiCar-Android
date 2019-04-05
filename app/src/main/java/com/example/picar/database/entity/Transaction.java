package com.example.picar.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "transactions")
public class Transaction {


    public Transaction() {
    }
    @Ignore
    public Transaction(int id, String pay_to, String by, String type, String date) {
        this.id = id;
        this.pay_to = pay_to;
        this.by = by;
        this.type = type;
        this.date = date;
    }

    @NonNull
    @ColumnInfo(name = "transaction_id")
    private int id;

    @ColumnInfo(name = "pay_to")
    private String pay_to;

    @ColumnInfo(name = "by")
    private String by;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "date")
    private String date;


    public String getPay_to() {
        return pay_to;
    }

    public void setPay_to(String pay_to) {
        this.pay_to = pay_to;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
