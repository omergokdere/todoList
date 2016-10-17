package com.omerg.todolist;

import java.io.Serializable;

public class Task implements Serializable {
    public static <E extends Enum<E>> E fromOrdinal(Class<E> enumClass, int ordinal) {
        E[] enumArray = enumClass.getEnumConstants();
        return enumArray[ordinal];
    }


/*    public enum Priority {High,Low;}*/
    public enum Status {Todo,Completed;}

    // variables
    private String id;
    private String taskName;
    private String expDate;
    private String description;
/*    private Priority priority;*/
    private Status status;

    public Task(String id, String taskName, String expDate, String description, /*Priority priority,*/ Status status) {
        this.id = id;
        this.taskName = taskName;
        this.expDate = expDate;
        this.description = description;
/*        this.priority = priority;*/
        this.status = status;
    }


    public String getId() {
        return id;
    }
    public String getTaskName() {
        return this.taskName;
    }

    public String getExpDate() {
        return this.expDate;
    }

    public String getDescription() {
        return this.description;
    }

 /*   public Priority getPriority() {
        return this.priority;
    }*/

    public Status getStatus() {
        return this.status;
    }
    /*static public TextView setColorOnPriority(Context context, TextView textView, Task task) {
        switch (task.getPriority()) {
*//*            case Critical:
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityCritical));
                break;*//*
            case High:
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityHIGH));
                break;
            case Low:
                textView.setTextColor(ContextCompat.getColor(context, R.color.colorPriorityLOW));
                break;
        }
        return textView;
    }*/
}
