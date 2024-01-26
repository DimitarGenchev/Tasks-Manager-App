package com.example.tasksmanagerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditTask extends AppCompatActivity {

    EditText newName;
    TextView newDate;
    Button save;
    Button cancel;
    private final Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        newName = findViewById(R.id.newName);
        newDate = findViewById(R.id.newDate);
        save = findViewById(R.id.button3);
        cancel = findViewById(R.id.button4);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(v);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoBack(v);
            }
        });

        newDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                selectedDate.set(year, monthOfYear, dayOfMonth);

                                Calendar currentDate = Calendar.getInstance();
                                int currentYear = currentDate.get(Calendar.YEAR);
                                int currentMonth = currentDate.get(Calendar.MONTH);
                                int currentDay = currentDate.get(Calendar.DAY_OF_MONTH);

                                if ((currentYear == year && currentMonth == monthOfYear && currentDay == dayOfMonth) || selectedDate.getTime().after(currentDate.getTime())) {
                                    newDate.setText(formatDate(selectedDate.getTime()));
                                }
                                else {
                                    Toast.makeText(EditTask.this, "Cannot set a past date", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        year, month, day);

                datePickerDialog.show();
            }
        });
    }

    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }

    public void Save(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("newName", String.valueOf(newName.getText()));
        bundle.putString("newDate", String.valueOf(newDate.getText()));
        bundle.putInt("position", getIntent().getIntExtra("pos", 0));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void GoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}