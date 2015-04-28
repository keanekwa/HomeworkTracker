package com.example.user.homeworktracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class HomeworkDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int mPosition = getIntent().getExtras().getInt(getString(R.string.homeworkpositionclicked));
        final Homework mCurrentHomework = HomeworkCollection.getHomeworkList(getApplicationContext()).get(mPosition);

        EditText mHomeworkNameEditText = (EditText) findViewById(R.id.homeworkNameEditText);
        final EditText mDueDateEditText = (EditText) findViewById(R.id.dueDateEditText);
        TextView mDueDateTextView = (TextView) findViewById(R.id.dueDateTextView);
        final EditText mReminderEditText = (EditText) findViewById(R.id.reminderEditText);
        TextView mReminderTextView = (TextView) findViewById(R.id.reminderTextView);
        final CheckBox mMarkDoneCheckBox = (CheckBox) findViewById(R.id.markDoneCheckBox);
        Button mExportButton = (Button) findViewById(R.id.exportButton);

        mHomeworkNameEditText.setText(mCurrentHomework.getName());
        TextWatcher textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mCurrentHomework.setName(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
        mHomeworkNameEditText.addTextChangedListener(textWatcher);

        if (mCurrentHomework.isDone()) {
            mMarkDoneCheckBox.setChecked(true);
        }
        else {
            mMarkDoneCheckBox.setChecked(false);
        }
        mMarkDoneCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mMarkDoneCheckBox.isChecked()) {
                    mCurrentHomework.setDone(true);
                }
                else {
                    mCurrentHomework.setDone(false);
                }
            }
        });

        mDueDateTextView.setText(getString(R.string.duedate));
        mDueDateEditText.setText(HomeworkCollection.simpledateformat.format(mCurrentHomework.getDueDate().getTime()));
        final Calendar mDueDateCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener mDueDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDueDateCalendar.set(Calendar.YEAR, year);
                mDueDateCalendar.set(Calendar.MONTH, monthOfYear);
                mDueDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mDueDateEditText.setText(HomeworkCollection.simpledateformat.format(mDueDateCalendar.getTime()));
                mCurrentHomework.setDueDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));
            }

        };
        mDueDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HomeworkDetailActivity.this, mDueDatePicker, mDueDateCalendar.get(Calendar.YEAR), mDueDateCalendar.get(Calendar.MONTH), mDueDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mDueDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(HomeworkDetailActivity.this, mDueDatePicker, mDueDateCalendar.get(Calendar.YEAR), mDueDateCalendar.get(Calendar.MONTH), mDueDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        //todo allow reminder set time.
        mReminderTextView.setText(getString(R.string.reminder));
        mReminderEditText.setText(HomeworkCollection.simpledateformat.format(mCurrentHomework.getReminderTime().getTime()));
        final Calendar mReminderCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener mReminderDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mReminderCalendar.set(Calendar.YEAR, year);
                mReminderCalendar.set(Calendar.MONTH, monthOfYear);
                mReminderCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mReminderEditText.setText(HomeworkCollection.simpledateformat.format(mReminderCalendar.getTime()));
                mCurrentHomework.setReminderTime(new GregorianCalendar(year, monthOfYear, dayOfMonth));
            }

        };
        mReminderEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HomeworkDetailActivity.this, mReminderDatePicker, mReminderCalendar.get(Calendar.YEAR), mReminderCalendar.get(Calendar.MONTH), mReminderCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mReminderEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(HomeworkDetailActivity.this, mReminderDatePicker, mReminderCalendar.get(Calendar.YEAR), mReminderCalendar.get(Calendar.MONTH), mReminderCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        mExportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //todo export homework as text to gmail, notes, etc
            }
        });
    }

}
