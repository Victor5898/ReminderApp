package com.example.laboratorul_2.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private String date;
    private List<Task> taskList = new ArrayList<>();

    public Event() {
    }

    public Event(String date, List<Task> tasklist) {
        this.date = date;
        this.taskList = tasklist;
    }

    public Event(String date, Task task) {
        this.date = date;
        this.taskList.add(task);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Task getTask(int i) {
        return taskList.get(i);
    }

    public void setTaskList(Task task) {
        this.taskList.add(task);
    }

    public void updateTask(Task task, int i) {
        taskList.set(i, task);
    }

    public int TaskSize() {
        return taskList.size();
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
