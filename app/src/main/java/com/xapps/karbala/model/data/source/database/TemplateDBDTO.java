package com.xapps.karbala.model.data.source.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TemplateDBDTO {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
