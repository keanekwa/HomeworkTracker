package com.example.user.homeworktracker;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

public class HomeworkCollection {
    private static HomeworkCollection sHomeworkCollection;
    private Context mAppContext;
    private ArrayList<Homework> mHomeworkList;

    public static SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    private HomeworkCollection(Context appContext) {
        mAppContext = appContext;
        mHomeworkList = new ArrayList<Homework>(Arrays.asList(
            new Homework("CEP Project", "CEP", false, new GregorianCalendar(2015, 5, 1), new GregorianCalendar(2015, 4, 25)),
            new Homework("Plus and Minus", "Math", false, new GregorianCalendar(2015, 6, 23), new GregorianCalendar(2015, 5, 15)),
            new Homework("Useless Homework", "Useless subject", true, new GregorianCalendar(2015, 7, 20), new GregorianCalendar(2015, 7, 14))
        ));
    }

    public static ArrayList<Homework> getHomeworkList(Context c) {
        if (sHomeworkCollection == null) {
            sHomeworkCollection = new HomeworkCollection(c.getApplicationContext());
        }
        return sHomeworkCollection.mHomeworkList;
    }
}
