package com.example.laboratorul_2.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.laboratorul_2.Model.Event;
import com.example.laboratorul_2.R;
import com.example.laboratorul_2.Data.RecyclerViewAdapter;
import com.example.laboratorul_2.Model.Task;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private CalendarView calendarView;
    private String date;
    private Button addButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        addButton = (Button) findViewById(R.id.addButton);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = dayOfMonth + "." + month + "." + year;
                if(XMLDeserialization() != null)
                {
                    createPopupDialog();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }

    public void createPopupDialog()
    {
        dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_event_day, null);
        Button closeButton = (Button) view.findViewById(R.id.closePopupButton);
        Button addNewTask = (Button) view.findViewById(R.id.addNewButtonPopup);

        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        List<Task> taskList;

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = XMLDeserialization().getTaskList();

        adapter = new RecyclerViewAdapter(this, taskList, date, XMLDeserialization());
        recyclerView.setAdapter(adapter);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        addNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public Event XMLDeserialization()
    {

        Log.d("Path: ", getFilesDir() + File.separator + date + ".xml");
        File xmlFile = new File(getFilesDir() + File.separator + date + ".xml");
        if(!xmlFile.exists()) {
            return null;
        }

        Event event = new Event();

        try{
            Serializer serializer = new Persister();
            event = serializer.read(Event.class, xmlFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return event;
    }
}
