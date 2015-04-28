package com.example.user.homeworktracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HomeworkDetailActivity extends AppCompatActivity {

    private boolean mIsNewHomework;
    private Homework mCurrentHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);

        int mPosition = getIntent().getExtras().getInt(getString(R.string.homeworkpositionclicked));
        final EditText mHomeworkNameEditText = (EditText) findViewById(R.id.homeworkNameEditText);
        final EditText mSubjectEditText = (EditText) findViewById(R.id.subjectEditText);
        final EditText mDueDateEditText = (EditText) findViewById(R.id.dueDateEditText);
        final EditText mReminderEditText = (EditText) findViewById(R.id.reminderEditText);
        final CheckBox mMarkDoneCheckBox = (CheckBox) findViewById(R.id.markDoneCheckBox);
        Button mExportButton = (Button) findViewById(R.id.exportButton);
        Button mSaveNewHomeworkButton = (Button) findViewById(R.id.saveNewHomeworkButton);
        if (mPosition == -1) {
            mCurrentHomework = HomeworkCollection.getHomeworkList(getApplicationContext()).get(HomeworkCollection.getHomeworkList(getApplicationContext()).size() - 1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        else {
            mCurrentHomework = HomeworkCollection.getHomeworkList(getApplicationContext()).get(mPosition);
            mDueDateEditText.setText(HomeworkCollection.simpledateformat.format(mCurrentHomework.getDueDate().getTime()));
            mReminderEditText.setText(HomeworkCollection.simpledatetimeformat.format(mCurrentHomework.getReminderTime().getTime()));
            mSaveNewHomeworkButton.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mHomeworkNameEditText.setText(mCurrentHomework.getName());
        TextWatcher mHomeworkNameTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s){
                mCurrentHomework.setName(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
        mHomeworkNameEditText.addTextChangedListener(mHomeworkNameTextWatcher);

        mSubjectEditText.setText(mCurrentHomework.getSubject());
        TextWatcher mSubjectTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s){
                mCurrentHomework.setSubject(s.toString());
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
        mSubjectEditText.addTextChangedListener(mSubjectTextWatcher);

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
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mDueDateEditText.getWindowToken(), 0);
                new DatePickerDialog(HomeworkDetailActivity.this, mDueDatePicker, mDueDateCalendar.get(Calendar.YEAR), mDueDateCalendar.get(Calendar.MONTH), mDueDateCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final Calendar mReminderCalendar = Calendar.getInstance();
        final SlideDateTimeListener listener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                mReminderCalendar.setTime(date);
                int mYear = mReminderCalendar.get(Calendar.YEAR);
                int mMonthOfYear = mReminderCalendar.get(Calendar.MONTH);
                int mDayOfMonth = mReminderCalendar.get(Calendar.DAY_OF_MONTH);
                int mHourOfDay = mReminderCalendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = mReminderCalendar.get(Calendar.MINUTE);
                mCurrentHomework.setReminderTime(new GregorianCalendar(mYear, mMonthOfYear, mDayOfMonth, mHourOfDay, mMinute));
                mReminderEditText.setText(HomeworkCollection.simpledatetimeformat.format(mCurrentHomework.getReminderTime().getTime()));
            }
        };
        mReminderEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager()).setListener(listener).setInitialDate(new Date()).build().show();
            }
        });

        mExportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //todo export homework as text to gmail, notes, etc
            }
        });

        mSaveNewHomeworkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mHomeworkNameEditText.getText().toString().matches(getString(R.string.nothing))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nohomeworknamewarning), Toast.LENGTH_SHORT).show();
                }
                else if (mSubjectEditText.getText().toString().matches(getString(R.string.nothing))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.nosubjectwarning), Toast.LENGTH_SHORT).show();
                }
                else if (mDueDateEditText.getText().toString().matches(getString(R.string.nothing))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noduedatewarning), Toast.LENGTH_SHORT).show();
                }
                else if (mReminderEditText.getText().toString().matches(getString(R.string.nothing))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.noreminderwarning), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent mBackToHomepageIntent = new Intent(HomeworkDetailActivity.this, MainActivity.class);
                    HomeworkDetailActivity.this.startActivity(mBackToHomepageIntent);
                }
            }
        });
    }
}
