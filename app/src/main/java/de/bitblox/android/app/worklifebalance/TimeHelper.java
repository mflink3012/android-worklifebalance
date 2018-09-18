package de.bitblox.android.app.worklifebalance;

import java.util.Calendar;

public class TimeHelper {
    private int hours;
    private int minutes;
    private boolean valid = false;

    public TimeHelper() {
        this(0, 0);
        this.valid = false;
    }

    public TimeHelper(final int hours, final int minutes) {
        set(hours, minutes);
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean isValid() {
        return valid;
    }

    public void set(final int hours, final int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.valid = true;
        onChange();
    }

    public void set(final TimeHelper other) {
        set(other.hours, other.minutes);
    }

    public void add(final TimeHelper toAdd) {
        int h = hours + toAdd.hours;
        int m = minutes + toAdd.minutes;

        if (m > 59) {
            h = h + m / 60;
            m = m % 60;
        }

        set(h, m);
    }

    public void subtract(final TimeHelper toSubtract) {
        int h = hours - toSubtract.hours;
        int m = minutes - toSubtract.minutes;

        if (m < 0) {
            h = h + m / 60;
            m = (m % 60) * -1;
        }

        set(h, m);
    }

    @Override
    public String toString() {
        String h = Integer.toString(hours);

        if (h.length() < 2) {
            h = "0" + h;
        }

        String m = Integer.toString(minutes);
        if (m.length() < 2) {
            m = "0" + m;
        }

        return h + ":" + m;
    }

    public void set(final String s) {
        final String[] sf = s.split(":");
        final int h, m;

        if (sf[0].startsWith("0")) {
            h = Integer.valueOf(sf[0].substring(1));
        } else {
            h = Integer.valueOf(sf[0]);
        }

        if (sf[1].startsWith("0")) {
            m = Integer.valueOf(sf[1].substring(1));
        } else {
            m = Integer.valueOf(sf[1]);
        }

        set(h, m);
    }

    public void set(final Calendar calendar) {
        set(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public void onChange() {
    }
}