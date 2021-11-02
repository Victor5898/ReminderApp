package com.example.laboratorul_2.Model;

public class Task
{
    private String time;
    private String task;
    private int requestCode;
    private String info;

    public Task()
    {

    }

    public Task(String time, String task, String info, int requestCode)
    {
        this.time = time;
        this.task = task;
        this.requestCode = requestCode;
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }


}
