package com.example.ooclock.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.ooclock.model.Alarm;

import java.util.List;

public class AlarmRepository {
    private AlarmDao alarmDao;
    private LiveData<List<Alarm>> alarmsLiveData;

    public AlarmRepository(Application application) {
        AlarmDatabase db = AlarmDatabase.getDatabase(application);
        alarmDao = db.alarmDao();
        alarmsLiveData = alarmDao.getAlarms();
    }

    public void insert(Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.insert(alarm);
        });
    }

    public void deleteAll() {
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.deleteAll();
        });
    }

    public void delete(Alarm alarm){
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.delete(alarm);
        });
    }

    public void update(Alarm alarm) {
        AlarmDatabase.databaseWriteExecutor.execute(() -> {
            alarmDao.update(alarm);
        });
    }

    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }
    public Alarm getAlarmbyId(int id){
        Log.d("An_Test","ALARM: "+id);
        Alarm alarms = alarmDao.getAlarmbyId(id);
        Log.d("An_Test","ALARM: "+alarms.toString());
        return alarms;
    }
}
