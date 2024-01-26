package com.example.tasksmanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddTask extends AppCompatActivity {

    private final Calendar selectedDate = Calendar.getInstance();
    EditText name;
    TextView dueDate;
    Button addTask;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        name = findViewById(R.id.taskName);
        dueDate = findViewById(R.id.dueDate);
        addTask = findViewById(R.id.button2);
        cancel = findViewById(R.id.button5);

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaskButton(v);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoBack(v);
            }
        });

        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectedDate.set(year, monthOfYear, dayOfMonth);

                                Calendar currentDate = Calendar.getInstance();
                                int currentYear = currentDate.get(Calendar.YEAR);
                                int currentMonth = currentDate.get(Calendar.MONTH);
                                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

                                if ((currentYear == year && currentMonth == monthOfYear && currentDay == dayOfMonth) || selectedDate.getTime().after(currentDate.getTime())) {
                                    dueDate.setText(formatDate(selectedDate.getTime()));
                                }
                                else {
                                    Toast.makeText(AddTask.this, "Cannot set a past date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
    }

    public void AddTaskButton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        String taskName = String.valueOf(name.getText());
        String taskDueDate = String.valueOf(dueDate.getText());
        bundle.putString("name", taskName);
        bundle.putString("dueDate", taskDueDate);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public void GoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}