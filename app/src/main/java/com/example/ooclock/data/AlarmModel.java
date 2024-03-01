package com.example.ooclock.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ooclock.model.Alarm;

import java.util.List;

public class AlarmModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private LiveData<List<Alarm>> alarmsLiveData;

    public AlarmModel(@NonNull Application application) {
        super(application);

        alarmRepository = new AlarmRepository(application);
        alarmsLiveData = alarmRepository.getAlarmsLiveData();
    }

    public void update(Alarm alarm) {
        alarmRepository.update(alarm);
    }
    public void delete() {
        alarmRepository.deleteAll();
    }
    public void deleteAlarm(Alarm alarm) {
        alarmRepository.delete(alarm);
    }
    public LiveData<List<Alarm>> getAlarmsLiveData() {
        return alarmsLiveData;
    }
    public void insert(Alarm alarm) {
        alarmRepository.insert(alarm);
    }
    public Alarm getAlarmbyId(int id) {
        return alarmRepository.getAlarmbyId(id);
    }
}
