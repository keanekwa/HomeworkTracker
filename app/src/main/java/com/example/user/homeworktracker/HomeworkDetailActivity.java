package com.example.user.homeworktracker;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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

    //variable for current homework
    private Homework mCurrentHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);

        //find elements by their ids
        int mPosition = getIntent().getExtras().getInt(getString(R.string.homeworkpositionclicked));
        final EditText mHomeworkNameEditText = (EditText) findViewById(R.id.homeworkNameEditText);
        final EditText mSubjectEditText = (EditText) findViewById(R.id.subjectEditText);
        final EditText mDueDateEditText = (EditText) findViewById(R.id.dueDateEditText);
        final EditText mReminderEditText = (EditText) findViewById(R.id.reminderEditText);
        final CheckBox mMarkDoneCheckBox = (CheckBox) findViewById(R.id.markDoneCheckBox);
        Button mExportButton = (Button) findViewById(R.id.exportButton);
        Button mSaveNewHomeworkButton = (Button) findViewById(R.id.saveNewHomeworkButton);
        //if this is a new homework
        if (mPosition == -1) {
            mCurrentHomework = HomeworkCollection.getHomeworkList().get(HomeworkCollection.getHomeworkList().size() - 1); //set mCurrentHomework to the newest homework
            mExportButton.setVisibility(View.INVISIBLE);    //make export button invisible
            getSupportActionBar().setDisplayHomeAsUpEnabled(false); //disable the up button
        }
        else {
            mCurrentHomework = HomeworkCollection.getHomeworkList().get(mPosition); //set mCurrentHomework to the selected homework
            //set the texts for the due date and reminder edittexts
            mDueDateEditText.setText(HomeworkCollection.simpledateformat.format(mCurrentHomework.getDueDate().getTime()));
            mReminderEditText.setText(HomeworkCollection.simpledatetimeformat.format(mCurrentHomework.getReminderTime().getTime()));
            mSaveNewHomeworkButton.setVisibility(View.GONE);    //make the mSaveNewHomeworkButton dissapear
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //enable the up button
        }

        mHomeworkNameEditText.setText(mCurrentHomework.getName());  //set the text for the homework name editttext
        //if homework name edittext is changed
        mHomeworkNameEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mCurrentHomework.setName(s.toString()); //save the new homework name
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        mSubjectEditText.setText(mCurrentHomework.getSubject());
        //if subject name edittext is changed
        mSubjectEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                mCurrentHomework.setSubject(s.toString());  //save the new subject
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //set the checkbox to whether the homework has been completed
        if (mCurrentHomework.isDone()) {
            mMarkDoneCheckBox.setChecked(true);
        }
        else {
            mMarkDoneCheckBox.setChecked(false);
        }
        //save the value of whether the homework is done if the user clicks on the checkbox
        mMarkDoneCheckBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mMarkDoneCheckBox.isChecked()) {
                    mCurrentHomework.setDone(true);
                } else {
                    mCurrentHomework.setDone(false);
                }
            }
        });

        //create calendar to save due date
        final Calendar mDueDateCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener mDueDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //convert input from datepicker to calendar
                mDueDateCalendar.set(Calendar.YEAR, year);
                mDueDateCalendar.set(Calendar.MONTH, monthOfYear);
                mDueDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDueDateEditText.setText(HomeworkCollection.simpledateformat.format(mDueDateCalendar.getTime()));   //print the due date
                mCurrentHomework.setDueDate(new GregorianCalendar(year, monthOfYear, dayOfMonth));  //save the due date
            }

        };
        //if user clicks on mDueDateEditText
        mDueDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard if user clicks on mDueDateEditText (there is no need for keyboard since DatePickerDialog shows up)
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mDueDateEditText.getWindowToken(), 0);
                new DatePickerDialog(HomeworkDetailActivity.this, mDueDatePicker, mDueDateCalendar.get(Calendar.YEAR), mDueDateCalendar.get(Calendar.MONTH), mDueDateCalendar.get(Calendar.DAY_OF_MONTH)).show();   //create DatePickerDialog which allows user to enter the due date
            }
        });

        //create calendar to save reminder time
        final Calendar mReminderCalendar = Calendar.getInstance();
        final SlideDateTimeListener listener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                //convert input from datetimeslider to calendar
                mReminderCalendar.setTime(date);
                int mYear = mReminderCalendar.get(Calendar.YEAR);
                int mMonthOfYear = mReminderCalendar.get(Calendar.MONTH);
                int mDayOfMonth = mReminderCalendar.get(Calendar.DAY_OF_MONTH);
                int mHourOfDay = mReminderCalendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = mReminderCalendar.get(Calendar.MINUTE);
                mCurrentHomework.setReminderTime(new GregorianCalendar(mYear, mMonthOfYear, mDayOfMonth, mHourOfDay, mMinute)); //save reminder time
                //create alarm
                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(HomeworkDetailActivity.this, AlarmReceiver.class);
                alarmIntent.putExtra(getString(R.string.ALRT_OBJ_NAME), mCurrentHomework.getName());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeworkDetailActivity.this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                alarmMgr.set(AlarmManager.RTC_WAKEUP, mCurrentHomework.getReminderTime().getTimeInMillis(), pendingIntent);
                mReminderEditText.setText(HomeworkCollection.simpledatetimeformat.format(mCurrentHomework.getReminderTime().getTime()));    //print reminder time
            }
        };
        //if user clicks on mReminderEditText
        mReminderEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SlideDateTimePicker.Builder(getSupportFragmentManager()).setListener(listener).setInitialDate(new Date()).build().show();
            }
        });

        //if user clicks on export button
        mExportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //create intent to allow user to export homework as text
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(getString(R.string.texthtml));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.homeworktrackerexport));   //set the subject (if exporting to email)
                //convert true/false boolean to yes/no
                String isDoneExport;
                if (mCurrentHomework.isDone()) {
                    isDoneExport = getString(R.string.yes);
                } else {
                    isDoneExport = getString(R.string.no);
                }
                //export homework as text in the format below
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.homework) + getString(R.string.space) + mCurrentHomework.getName() + "\n"
                        + getString(R.string.isDone) + getString(R.string.space) + isDoneExport + "\n"
                        + getString(R.string.subject) + getString(R.string.space) + mCurrentHomework.getSubject() + "\n"
                        + getString(R.string.duedate) + getString(R.string.space) + HomeworkCollection.simpledateformat.format(mCurrentHomework.getDueDate().getTime()) + "\n"
                        + getString(R.string.reminder) + getString(R.string.space) + HomeworkCollection.simpledatetimeformat.format(mCurrentHomework.getReminderTime().getTime()));
                startActivity(Intent.createChooser(intent, getString(R.string.exporthomeworkastext))); //set title of the chooser intent (will appear in heading)
            }
        });

        //check for errors when user clicks on the mSaveNewHomeworkButton
        mSaveNewHomeworkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //check if any of the fields are empty, and creates a toast if it is empty
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
                    //if there are no errors, return back to homepage
                    Intent mBackToHomepageIntent = new Intent(HomeworkDetailActivity.this, MainActivity.class);
                    HomeworkDetailActivity.this.startActivity(mBackToHomepageIntent);
                }
            }
        });
    }
}
