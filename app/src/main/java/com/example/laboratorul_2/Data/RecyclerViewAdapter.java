package com.example.laboratorul_2.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorul_2.Activities.UpdateEventActivity;
import com.example.laboratorul_2.Model.Event;
import com.example.laboratorul_2.Model.Task;
import com.example.laboratorul_2.R;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{

    private Context context;
    private List<Task> taskList;
    private String date;
    private Event event;

    public RecyclerViewAdapter(Context context, List<Task> taskList, String date, Event event) {
        this.context = context;
        this.taskList = taskList;
        this.date = date;
        this.event = event;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position)
    {
        final Task task = taskList.get(position);
        holder.timeView.setText("Time: " + task.getTime());
        holder.taskView.setText("Task: " + task.getTask());
        holder.info.setText("Info: " + task.getInfo());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateEventActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("task", task.getTask());
                intent.putExtra("info", task.getInfo());
                Log.d("requestPositionRec: ", String.valueOf(position));
                intent.putExtra("position", position);
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskList.remove(position);
                if(taskList.size() == 0) {
                    File xmlFile = new File("/data/user/0/com.example.laboratorul_2/files/" + date + ".xml");
                    xmlFile.delete();
                } else {
                    Event newEvent = new Event(date, taskList);
                    XMLSerialization(newEvent);
                }
                Toast.makeText(context, "Event Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView timeView;
        public TextView taskView;
        public TextView info;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(this);

            timeView = (TextView) itemView.findViewById(R.id.timeTextViewList);
            taskView = (TextView) itemView.findViewById(R.id.taskTextViewList);
            info = (TextView) itemView.findViewById(R.id.descriptionTextViewList);
            editButton = (Button) itemView.findViewById(R.id.editButtonList);
            deleteButton = (Button) itemView.findViewById(R.id.deleteButtonList);
        }

        @Override
        public void onClick(View v)
        {

        }
    }

    public void XMLSerialization(Event event) {
        try {
            File xmlFile = new File("/data/user/0/com.example.laboratorul_2/files/" + date + ".xml");
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
}
