package com.example.ooclock.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ooclock.model.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {
    @Insert
    void insert(Alarm alarm);

    @Query("DELETE FROM alarm_table")
    void deleteAll();

    @Query("SELECT * FROM alarm_table ORDER BY created ASC")
    LiveData<List<Alarm>> getAlarms();

    @Query("SELECT * FROM alarm_table WHERE alarmId=:id")
    Alarm getAlarmbyId(int id);

    @Delete
    void delete(Alarm alarm);

    @Update
    void update(Alarm alarm);
}
