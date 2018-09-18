package de.bitblox.android.app.worklifebalance;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends Activity implements TimePickerDialog.OnTimeSetListener {
    private TimeHelper startTime;
    private TimeHelper pauseLength;
    private TimeHelper workLength;
    private TimeHelper endTime;
    private TimeHelper timeHelper;
    private TextView endTimeOutput;

    private void calculateEndTime() {
        endTime.set(startTime);
        endTime.add(pauseLength);
        endTime.add(workLength);

        if (endTime.getHours() > 23) {
            endTime.subtract(new TimeHelper(24, 0));
        }

        save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView startTimeInput = (TextView) findViewById(R.id.input_starttime);
        startTime = new TimeHelper(7, 45) {
            @Override
            public void onChange() {
                super.onChange();
                startTimeInput.setText(toString());
            }
        };
        startTimeInput.setOnClickListener(createTimePickerListener(startTimeInput, startTime));

        ((Button) findViewById(R.id.button_now)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime.set(Calendar.getInstance());
                calculateEndTime();
            }
        });

        final TextView pauseLengthInput = (TextView) findViewById(R.id.input_pauselength);
        pauseLength = new TimeHelper(0, 30) {
            @Override
            public void onChange() {
                super.onChange();
                pauseLengthInput.setText(this.toString());
            }
        };
        pauseLengthInput.setOnClickListener(createTimePickerListener(pauseLengthInput, pauseLength));

        final TextView workLengthInput = (TextView) findViewById(R.id.input_worklength);
        workLength = new TimeHelper(8, 15) {
            @Override
            public void onChange() {
                super.onChange();
                workLengthInput.setText(this.toString());
            }
        };
        workLengthInput.setOnClickListener(createTimePickerListener(workLengthInput, workLength));

        endTimeOutput = (TextView) findViewById(R.id.output_endtime);
        endTime = new TimeHelper() {
            @Override
            public void onChange() {
                super.onChange();
                endTimeOutput.setText(this.toString());
            }
        };

        load();
    }

    private View.OnClickListener createTimePickerListener(final TextView textView, final TimeHelper timeHelper) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setListener(MainActivity.this);

                if (timeHelper.isValid()) {
                    timePickerFragment.setTime(timeHelper.getHours(), timeHelper.getMinutes());
                }

                MainActivity.this.timeHelper = timeHelper;

                timePickerFragment.show(MainActivity.this.getFragmentManager(), String.valueOf(textView.getId()));
            }
        };
    }

    private void load() {
        final SharedPreferences sharedPref = getSharedPreferences("settings", MODE_PRIVATE);

        if (sharedPref.contains("startTime")) {
            startTime.set(sharedPref.getString("startTime", "07:45"));
        }
        if (sharedPref.contains("pauseLength")) {
            pauseLength.set(sharedPref.getString("pauseLength", "00:30"));
        }
        if (sharedPref.contains("workLength")) {
            workLength.set(sharedPref.getString("workLength", "08:15"));
        }
    }

    private void save() {
        final SharedPreferences sharedPref = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("startTime", startTime.toString());
        editor.putString("pauseLength", pauseLength.toString());
        editor.putString("workLength", workLength.toString());
        editor.commit();
    }

    @Override
    public void onTimeSet(final TimePicker timePicker, final int hour, final int minute) {
        timeHelper.set(hour, minute);

        if (startTime.isValid() && pauseLength.isValid() && workLength.isValid()) {
            calculateEndTime();
        } else {
            endTimeOutput.setText("--:--");
        }
    }
}
