package de.bitblox.android.app.worklifebalance;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private EditText startTimeInput = null;
    private TimeHelper startTime = new TimeHelper();
    private EditText pauseLengthInput = null;
    private TimeHelper pauseLength = new TimeHelper();
    private EditText workLengthInput = null;
    private TimeHelper workLength = new TimeHelper();
    private EditText endTimeOutput = null;

    class TextChangedWatcher implements TextWatcher {
        final int toastResId;
        TimeHelper timeHelper;

        public TextChangedWatcher(final int toastResId, final TimeHelper timeHelper) {
            this.toastResId = toastResId;
            this.timeHelper = timeHelper;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Not interesting.
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Not interesting.
        }

        @Override
        public void afterTextChanged(final Editable editable) {
            final String timeString = editable.toString();
            timeHelper.copy(checkTimeInput(timeString, toastResId));

            if (startTime.isValid() && pauseLength.isValid() && workLength.isValid()) {
                calculateEndTime();
                save();
            } else {
                endTimeOutput.setText("??:??");
            }
        }
    }

    private void calculateEndTime() {
        final TimeHelper endTime = new TimeHelper();

        endTime.copy(startTime);
        endTime.subtract(pauseLength);
        endTime.add(workLength);

        if (endTime.getHours() > 23) {
            endTime.subtract(new TimeHelper(24, 0));
        }

        endTimeOutput.setText(endTime.getHours() + ":" + endTime.getMinutes());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTimeInput = (EditText) findViewById(R.id.input_starttime);
        startTimeInput.addTextChangedListener(new TextChangedWatcher(R.string.toast_starttime, startTime));

        ((Button) findViewById(R.id.button_now)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimeInput.setText(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE));
            }
        });

        pauseLengthInput = (EditText) findViewById(R.id.input_pauselength);
        pauseLengthInput.addTextChangedListener(new TextChangedWatcher(R.string.toast_pauselength, pauseLength));

        workLengthInput = (EditText) findViewById(R.id.input_worklength);
        workLengthInput.addTextChangedListener(new TextChangedWatcher(R.string.toast_worklength, workLength));

        endTimeOutput = (EditText) findViewById(R.id.output_endtime);

        load();
    }

    private void load() {
        final SharedPreferences sharedPref = getSharedPreferences("settings", MODE_PRIVATE);

        if (sharedPref.contains("startTime")) {
            startTimeInput.setText(sharedPref.getString("startTime", "07:45"));
        }
        if (sharedPref.contains("pauseLength")) {
            pauseLengthInput.setText(sharedPref.getString("pauseLength", "00:30"));
        }
        if (sharedPref.contains("workLength")) {
            workLengthInput.setText(sharedPref.getString("workLength", "08:15"));
        }
    }

    private void save() {
        final SharedPreferences sharedPref = getSharedPreferences("settings", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("startTime", startTimeInput.getText().toString());
        editor.putString("pauseLength", pauseLengthInput.getText().toString());
        editor.putString("workLength", workLengthInput.getText().toString());
        editor.commit();
    }

    private TimeHelper checkTimeInput(final String timeString, final int toastResId) {
        final TimeHelper timeHelper;
        try {
            timeHelper = TimeHelper.parse(timeString);
        } catch (ParseException e) {
            if (e.getErrorOffset() > -1) {
                Toast.makeText(getApplicationContext(), getResources().getString(toastResId, e.getMessage(), e.getErrorOffset() + 1), Toast.LENGTH_SHORT).show();
            }

            // Don't do anything on -1.
            return null;
        }

        if (timeHelper.getHours() > 23) {
            Toast.makeText(getApplicationContext(), getResources().getString(toastResId, R.string.toast_invalidhours, 1), Toast.LENGTH_SHORT).show();
            return null;
        }

        if (timeHelper.getMinutes() > 59) {
            Toast.makeText(getApplicationContext(), getResources().getString(toastResId, R.string.toast_invalidminutes, timeString.length()), Toast.LENGTH_SHORT).show();
            return null;
        }

        return timeHelper;
    }
}
