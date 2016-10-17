package com.omerg.todolist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class TaskAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public TaskAdapter(Context context, int id, List<Task> items) {
        super(context, id, items);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task item = (Task) getItem(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.view_of_task, null);  }

        String formatedDate = "";
        try {
            Date date = new SimpleDateFormat(com.omerg.todolist.MainActivity.DATE_FORMAT).parse(item.getExpDate());
            formatedDate = DateFormat.getDateInstance().format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        TextView taskNameTextView = (TextView) convertView.findViewById(R.id.tasknameID);
        taskNameTextView.setText(item.getTaskName());

        TextView statusTextView = (TextView) convertView.findViewById(R.id.statusID);
        statusTextView.setText(item.getStatus().toString() + "  ");



        if (item.getStatus() == Task.Status.Completed) {
            (convertView.findViewById(R.id.rowItemID)).setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorCOMPLETED));
        } else {
            (convertView.findViewById(R.id.rowItemID)).setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorTODO));
        }


        /*TextView priorityTextView = (TextView) convertView.findViewById(R.id.priorityID);
        priorityTextView.setText("< " + item.getPriority().toString() + " >");
        Task.setColorOnPriority(mContext, priorityTextView, item);         */

        TextView expTextView = (TextView) convertView.findViewById(R.id.expdateID);
        expTextView.setText("Exp.Date: " + formatedDate);

        return convertView;
    }
}
