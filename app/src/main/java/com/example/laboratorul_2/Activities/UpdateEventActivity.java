package com.example.laboratorul_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.laboratorul_2.Alarm.AlertReceiver;
import com.example.laboratorul_2.Model.Event;
import com.example.laboratorul_2.Model.Task;
import com.example.laboratorul_2.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;

public class UpdateEventActivity extends AppCompatActivity {

    private String date;
    private String name;
    private int position;
    private TimePicker timePicker;
    private String time;
    private TextView dateTextView;
    private TextView updateTitle;
    private String additionalInfo;
    private EditText taskEditText;
    private EditText infoText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        date = getIntent().getStringExtra("date");
        name = getIntent().getStringExtra("task");
        additionalInfo = getIntent().getStringExtra("info");

        position = getIntent().getIntExtra("position", 0);

        timePicker = (TimePicker) findViewById(R.id.timePickerUpdate);
        dateTextView = (TextView) findViewById(R.id.dateTextViewUpdate);
        infoText = (EditText) findViewById(R.id.editTextAdditionalInfo);
        taskEditText = (EditText) findViewById(R.id.editTaskUpdate);
        updateTitle = (TextView) findViewById(R.id.updateTheReminder);
        saveButton = (Button) findViewById(R.id.saveButtonUpdate);

        dateTextView.setText(date);
        taskEditText.setText(name);
        infoText.setText(additionalInfo);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = timePicker.getHour() + ":" + timePicker.getMinute();

                Random r = new Random();
                Integer requestCode = r.nextInt();

                Task task = new Task(time, taskEditText.getText().toString(), infoText.getText().toString(), requestCode);

                Event event = XMLDeserialization();

                Log.d("request", "+++");
                //Log.d("requestSize: ", String.valueOf(event.getRequetCodeList().size()));
                Log.d("requestPosition", String.valueOf(position));

                cancelAlarm(event.getTaskList().get(position).getRequestCode());

                String[] date_split_table = date.split(Pattern.quote("."));

                int day = Integer.parseInt(date_split_table[0]);
                int month = Integer.parseInt(date_split_table[1]);
                int year = Integer.parseInt(date_split_table[2]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, timePicker.getHour(), timePicker.getMinute(), 0);

                startAlarm(calendar, requestCode, time, task.getTask());

                //event.updateRequestCode(requestCode, position);
                event.updateTask(task, position);

                XMLSerialization(event);

                Toast.makeText(UpdateEventActivity.this, "Event Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void XMLSerialization(Event event) {
        try {
            File xmlFile = new File(getFilesDir() + File.separator + date + ".xml");
            xmlFile.createNewFile();

            try {
                Serializer serializer = new Persister();
                serializer.write(event, xmlFile);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Event XMLDeserialization() {
        File xmlFile = new File(getFilesDir() + File.separator + date + ".xml");
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

    private void startAlarm(Calendar c, int requestCode, String time, String task)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("time", time);
        intent.putExtra("task", task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        alarmManager.cancel(pendingIntent);
    }
}
