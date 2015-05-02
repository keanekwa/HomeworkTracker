package com.example.user.homeworktracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;

//singleton for all common variables/functions in
public class HomeworkCollection {
    private static HomeworkCollection sHomeworkCollection;
    private static ArrayList<Homework> mHomeworkList;

    //formatting for dates
    public static SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    //formatting for date and time
    public static SimpleDateFormat simpledatetimeformat = new SimpleDateFormat("dd/MM/yyyy hh:mma", Locale.US);

    //create collection which has an arraylist of homeworks in it
    private HomeworkCollection() {
        mHomeworkList = new ArrayList<>(Arrays.asList(
            new Homework("CEP Project", "CEP", false, new GregorianCalendar(2015, 5, 1), new GregorianCalendar(2015, 4, 25, 12, 0)),
            new Homework("Plus and Minus", "Math", false, new GregorianCalendar(2015, 6, 23), new GregorianCalendar(2015, 5, 15, 5, 40)),
            new Homework("Useless Homework", "Useless subject", true, new GregorianCalendar(2015, 7, 20), new GregorianCalendar(2015, 7, 14, 20, 35))
        ));
    }

    //function for getting the homework list in this collection
    public static ArrayList<Homework> getHomeworkList() {
        if (sHomeworkCollection == null) {
            sHomeworkCollection = new HomeworkCollection();
        }
        return mHomeworkList;
    }

    /* Tried to make json work but failed badly. A for effort???
    public static void setHomeworkList(ArrayList<Homework> homeworkList) {
        mHomeworkList = homeworkList;
    }*/
}
