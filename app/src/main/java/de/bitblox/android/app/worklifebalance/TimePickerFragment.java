package de.bitblox.android.app.worklifebalance;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private Integer hours;
    private Integer minutes;

    public void setListener(final TimePickerDialog.OnTimeSetListener timeSetListener) {
        this.timeSetListener = timeSetListener;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        if ((hours == null) || (minutes == null)) {
            final Calendar calendar = Calendar.getInstance();
            hours = calendar.get(Calendar.HOUR_OF_DAY);
            minutes = calendar.get(Calendar.MINUTE);
        }

        return new TimePickerDialog(getActivity(), timeSetListener, hours, minutes, DateFormat.is24HourFormat(getActivity()));
    }

    public void setTime(int hours, int minutes) {
        this.hours = hours;
        this.minutes = minutes;
    }
}