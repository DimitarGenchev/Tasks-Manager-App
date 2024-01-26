package com.example.tasksmanagerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ListView tasksDisplayer;
    Button addTask;
    ArrayList<String> tasks;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addTask = findViewById(R.id.button);
        tasksDisplayer = findViewById(R.id.tasks);

        tasks = new ArrayList<>();

        if (savedInstanceState != null) {
            tasks = savedInstanceState.getStringArrayList("tasks");
        }
        else {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TaskContent",0);
            int count = sharedPreferences.getInt("count", 0);
            for (int i = 0; i < count; i++) {
                tasks.add(sharedPreferences.getString("task" + i, ""));
            }
        }

        adapter = new ArrayAdapter<>(this, R.layout.activity_list_view, R.id.textView5, tasks);
        tasksDisplayer.setAdapter(adapter);
        setUpListViewListeners();

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToAddTask(v);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        LoadValues();
        AddAtPosition();
    }

    private void setUpListViewListeners() {
        tasksDisplayer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete task");
                builder.setMessage("Are you sure you want to delete this task?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tasks.remove(position);
                        adapter.notifyDataSetChanged();
                        SaveTasks(tasks);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });

        tasksDisplayer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditTask.class);
                intent.putExtra("pos", position);
                startActivity(intent);
            }
        });
    }

    public void GoToAddTask(View view) {
        Intent intent = new Intent(this, AddTask.class);
        startActivity(intent);
    }

    void LoadValues(){
        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getExtras();

        if (receivedBundle != null) {
            String name = receivedBundle.getString("name", null);
            String dueDate = receivedBundle.getString("dueDate", null);

            if (name != null && dueDate != null) {
                if (!name.equals("") && !(dueDate.equals(""))) {
                    tasks.add(name + " - " + dueDate);
                    SaveTasks(tasks);
                }
                else {
                    Toast.makeText(this, "Invalid data!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    void AddAtPosition() {
        Intent receivedIntent = getIntent();
        Bundle receivedBundle = receivedIntent.getExtras();

        if (receivedBundle != null) {
            int position = receivedBundle.getInt("position", -1);

            if (position != -1) {
                String newName = receivedBundle.getString("newName");
                String newDueDate = receivedBundle.getString("newDate");

                if (!Objects.equals(newName, "")) {
                    if (Objects.equals(newDueDate, "")) {
                        String element = tasks.get(position);
                        int len = element.length();
                        newDueDate = tasks.get(position).substring(len - 10);
                    }

                    tasks.remove(position);
                    tasks.add(position, newName + " - " + newDueDate);
                    adapter.notifyDataSetChanged();
                    SaveTasks(tasks);
                }
               else {
                   Toast.makeText(this, "Invalid data!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void SaveTasks(ArrayList<String> tasks){
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TaskContent",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            editor.putString("task" + i, tasks.get(i));
        }
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("tasks", tasks);
    }
}