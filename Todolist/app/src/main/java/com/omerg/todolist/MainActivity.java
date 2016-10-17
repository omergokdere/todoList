package com.omerg.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_EDIT = 0;
    public  final static String DATE_FORMAT = "yyyy-MM-dd";

    private ArrayList<Task> mTaskList = null;
    private ListView mTaskListView = null;
    private TaskAdapter mTaskListAdapter = null;
    private DBAdapter mDBAdapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_and_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mTaskList = new ArrayList<>();
        mTaskListView = (ListView) findViewById(R.id.lisTODO);
        mTaskListAdapter = new TaskAdapter(this, 0, mTaskList);
        mTaskListView.setAdapter(mTaskListAdapter);
        mTaskListView.setOnItemClickListener(new ListItemClickListener());
        mTaskListView.setOnItemLongClickListener(new ListItemLongClickListener());

        mDBAdapter = new DBAdapter(this);
        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }


    public class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View itemDetailView = layoutInflater.inflate(R.layout.task_detail, null);
            final Task item = (Task) parent.getItemAtPosition(position);

            String formatedDate = "";


            try {
                Date date = new SimpleDateFormat(DATE_FORMAT).parse(item.getExpDate());
                formatedDate = DateFormat.getDateInstance().format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ((TextView) itemDetailView.findViewById(R.id.tasknameID)).setText(item.getTaskName());
            ((TextView) itemDetailView.findViewById(R.id.expdateID)).setText(formatedDate);
            ((TextView) itemDetailView.findViewById(R.id.descriptionID)).setText(item.getDescription());
            /*TextView priorityTextView =(TextView) itemDetailView.findViewById(R.id.priorityID);
            priorityTextView.setText(item.getPriority().toString());
            Task.setColorOnPriority(MainActivity.this, priorityTextView, item); */
            ((TextView) itemDetailView.findViewById(R.id.statusID)).setText(item.getStatus().toString());

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setIcon(R.mipmap.edit);
            dialog.setTitle(getApplicationContext().getString(R.string.taskDESCRIPTION));
            dialog.setView(itemDetailView);
            dialog.setPositiveButton(R.string.editTASK, new DialogInterface.OnClickListener() {  // heh burda positivebutton ekledim. nedir dersen button ama virtual sadece orda dialogda tanimli.
                @Override
                // eger positive buttona basilirsa yapialacak olan activity
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("itemSerializable", item);
                    startActivityForResult(intent, REQUEST_EDIT);
                }
            });
            dialog.show();
        }
    }


    public class ListItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Task item = (Task) parent.getItemAtPosition(position);

            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setIcon(R.mipmap.delete);
            dialog.setTitle(getApplicationContext().getString(R.string.deleteWARNING));
            dialog.setPositiveButton(R.string.deleteTASK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDBAdapter.open();
                    if (mDBAdapter.deleteTask(Integer.parseInt(item.getId()))) {
                        Toast.makeText(getApplicationContext(), getString(R.string.deleteSUCCESS), Toast.LENGTH_SHORT).show();
                    }
                    mDBAdapter.close();
                    loadTasks();
                }
            });
            dialog.show();
            return true;
        }
    }



    private void loadTasks() {
        mTaskList.clear();
        mDBAdapter.open();
       Cursor c = mDBAdapter.getAllitemsInOrder();

        if (c.moveToFirst()) {
            do {
                Task item = new Task(

                        c.getString(c.getColumnIndex(DBAdapter.COLUMN_ID)),
                        c.getString(c.getColumnIndex(DBAdapter.COLUMN_TASKNAME)),
                        c.getString(c.getColumnIndex(DBAdapter.COLUMN_EXPDATE)),
                        c.getString(c.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION)),
                        /*Task.fromOrdinal(Task.Priority.class, c.getInt(c.getColumnIndex(DBAdapter.COLUMN_PRIORITY))),*/
                        Task.fromOrdinal(Task.Status.class, c.getInt(c.getColumnIndex(DBAdapter.COLUMN_STATUS)))
                        );

                mTaskList.add(item);
            } while (c.moveToNext());
        }

        mDBAdapter.close();

        mTaskListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.newTaskButton) {
            Task item = new Task(null, null, null, null, /*Task.Priority.High,*/ Task.Status.Todo);

            Intent intent = new Intent(this, EditActivity.class);
            intent.putExtra("itemSerializable", item);
            startActivityForResult(intent, REQUEST_EDIT);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT) {
            loadTasks();
        }
    }
}
