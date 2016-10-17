package com.omerg.todolist;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EditActivity extends AppCompatActivity {
    private EditText mTaskNameEditText;
    private DatePicker mExpDatePicker;
    private EditText mDescriptionEditText;
    private Spinner mPrioritySpinner;
    private Spinner mStatusSpinner;
/*    private Task.Priority mSelectedPriority;*/
    private Task.Status mSelectedStatus;
    private String mTaskID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_and_edit);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        Task task = (Task) extras.getSerializable("itemSerializable");
        mTaskID = task.getId();

        mTaskNameEditText = (EditText) findViewById(R.id.tasknameEDIT);
        mTaskNameEditText.setText(task.getTaskName());
        mExpDatePicker = (DatePicker) findViewById(R.id.datePICKER);

        if (task.getExpDate() != null) {
            int year = Integer.parseInt(task.getExpDate().substring(0, 4));
            int month = Integer.parseInt(task.getExpDate().substring(5, 7));
            int date = Integer.parseInt(task.getExpDate().substring(8, 10));

            mExpDatePicker.updateDate(year, month - 1 , date);
        }

        mDescriptionEditText = (EditText) findViewById(R.id.descriptionEDIT);
        mDescriptionEditText.setText(task.getDescription());
       /* mPrioritySpinner = (Spinner) findViewById(R.id.prioritySPN);
        mPrioritySpinner.setSelection(Task.Priority.valueOf(task.getPriority().toString()).ordinal());
        mPrioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPriority = Task.Priority.valueOf(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });*/
        mStatusSpinner = (Spinner) findViewById(R.id.statusSPN);
        mStatusSpinner.setSelection(Task.Status.valueOf(task.getStatus().toString()).ordinal());
        mStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedStatus = Task.Status.valueOf(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private boolean isReadyToSave() {
        if ("".equals(mTaskNameEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.tasknameFAIL), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void doSaveTask() {

        Task task = new Task(
                mTaskID,
                mTaskNameEditText.getText().toString(),
                new SimpleDateFormat(com.omerg.todolist.MainActivity.DATE_FORMAT).format(mExpDatePicker.getCalendarView().getDate()),
                mDescriptionEditText.getText().toString(),
               /* mSelectedPriority,   */
                mSelectedStatus
        );
        Log.d("EditActivity on save",
                "task name: " + mTaskNameEditText.getText().toString() + ", "
                        + "date: " + DateFormat.getDateInstance().format(mExpDatePicker.getCalendarView().getDate()) + ", "
                        + "description: " + mDescriptionEditText.getText().toString() + ", "
/*                        + "priority: " + mSelectedPriority.toString() + ", "*/
                        + "status: " + mSelectedStatus.toString());

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.open();
        if (dbAdapter.saveTask(task)) {
            Toast.makeText(this, getString(R.string.saveSUCCESS), Toast.LENGTH_SHORT).show();
        };
        dbAdapter.close();
        mTaskID = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem task) {
        int id = task.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.saveButton) {
            if (isReadyToSave()) {
                doSaveTask();
                finish();
            }
        }
        return super.onOptionsItemSelected(task);
    }
}