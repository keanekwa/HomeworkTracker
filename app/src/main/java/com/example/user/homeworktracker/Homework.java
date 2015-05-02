package com.example.user.homeworktracker;

import java.util.GregorianCalendar;

public class Homework {
    //setting up variables
    private String mName;
    private String mSubject;
    private boolean mDone;
    private GregorianCalendar mDueDate = new GregorianCalendar();
    private GregorianCalendar mReminderTime = new GregorianCalendar();

    public Homework(String name, String subject, boolean done, GregorianCalendar dueDate, GregorianCalendar reminderTime) {
        mName = name;
        mSubject = subject;
        mDone = done;
        mDueDate = dueDate;
        mReminderTime = reminderTime;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subject) {
        mSubject = subject;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        mDone = done;
    }

    public GregorianCalendar getDueDate() {
        return mDueDate;
    }

    public void setDueDate(GregorianCalendar dueDate) {
        mDueDate = dueDate;
    }

    public GregorianCalendar getReminderTime() {
        return mReminderTime;
    }

    public void setReminderTime(GregorianCalendar reminderTime) {
        mReminderTime = reminderTime;
    }
}
