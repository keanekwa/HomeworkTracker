package com.example.user.homeworktracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView mHomeworksList = (ListView) this.findViewById(android.R.id.list);

        //display subjects in listView (in adapted layout)
        ArrayAdapter<Homework> adapter = new HomeworkAdapter(this, R.layout.list_item_homework, HomeworkCollection.getHomeworkList(getApplicationContext()));
        mHomeworksList.setAdapter(adapter);
        mHomeworksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                //start activity to see more details about the homework
                Intent mHomeworkDetailIntent = new Intent(MainActivity.this, HomeworkDetailActivity.class);
                mHomeworkDetailIntent.putExtra(getString(R.string.homeworkpositionclicked), position);  //send position of row as extra to HomeworkDetailActivity
                MainActivity.this.startActivity(mHomeworkDetailIntent);
            }
        });

        //add mAddHomeworkButton to bottom of listView
        final Button mAddHomeworkButton = new Button(this);
        mAddHomeworkButton.setText(getString(R.string.add_new_homework));
        mHomeworksList.addFooterView(mAddHomeworkButton);
        //when add homework button is clicked
        mAddHomeworkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //start activity for adding new subject
                Intent mNewSubjectIntent = new Intent(MainActivity.this, HomeworkDetailActivity.class);
                mNewSubjectIntent.putExtra(getString(R.string.homeworkpositionclicked), -1);
                HomeworkCollection.getHomeworkList(getApplicationContext()).add(new Homework("", "", false, new GregorianCalendar(), new GregorianCalendar()));
                MainActivity.this.startActivity(mNewSubjectIntent);
            }
        });
    }

    //array adapter for listView of subjects
    private class HomeworkAdapter extends ArrayAdapter<Homework> {
        //creating variables
        private int mResource;
        private ArrayList<Homework> mHomeworks;

        public HomeworkAdapter (Context context, int resource, ArrayList<Homework> homeworks) {
            super(context, resource, homeworks);
            mResource = resource;
            mHomeworks = homeworks;
        }

        //display subject data in every row of listView
        @Override
        public View getView(int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getLayoutInflater().inflate(mResource, parent, false);
            }
            //get the subject to be displayed in row
            final Homework mCurrentHomework = mHomeworks.get(position);
            //display data from subject in row
            TextView mHomeworkTextView = (TextView) row.findViewById(R.id.homeworkItemTextView);
            mHomeworkTextView.setText(mCurrentHomework.getName());
            TextView mSubjectTextView = (TextView) row.findViewById(R.id.subjectItemTextView);
            mSubjectTextView.setText(mCurrentHomework.getSubject());
            TextView mDueDateTextView = (TextView) row.findViewById(R.id.dueDateItemTextView);
            mDueDateTextView.setText(getString(R.string.duedate) + getString(R.string.space) + HomeworkCollection.simpledateformat.format(mCurrentHomework.getDueDate().getTime()));

            final CheckBox mIsDoneCheckBox = (CheckBox) row.findViewById(R.id.isDoneItemCheckBox);
            if (mCurrentHomework.isDone()) {
                mIsDoneCheckBox.setChecked(true);
            }
            else {
                mIsDoneCheckBox.setChecked(false);
            }
            mIsDoneCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mIsDoneCheckBox.isChecked()) {
                        mCurrentHomework.setDone(true);
                    }
                    else {
                        mCurrentHomework.setDone(false);
                    }
                }
            });

            return row;
        }
    }

}
