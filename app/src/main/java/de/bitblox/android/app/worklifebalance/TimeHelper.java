package de.bitblox.android.app.worklifebalance;

import android.text.format.Time;

import java.text.ParseException;

public class TimeHelper {
    private int hours;
    private int minutes;
    private boolean valid = false;

    public TimeHelper() {
        this(0, 0);
        this.valid = false;
    }

    public TimeHelper(final int hours, final int minutes) {
        this.hours = hours;
        this.minutes = minutes;
        this.valid = true;
    }

    public static TimeHelper parse(final String timeString) throws ParseException {
        if (timeString == null) {
            throw new ParseException("Value is null!", -1);
        }

        final String trimmedTimeString = timeString.trim();
        if (trimmedTimeString.length() == 0) {
            throw new ParseException("Value is empty!", -1);
        }

        final int indexOfColon = timeString.indexOf(":");
        if ((indexOfColon < 1) || (indexOfColon > 2) || (indexOfColon == timeString.length() - 1)) {
            throw new ParseException("Colon is not found at index 1 or 2 or is at the end!", -1);
        }

        final String[] splittedTimeString = timeString.split(":");

        if ((splittedTimeString[0].length() < 1) || (splittedTimeString[1].length() < 2)) {
            throw new ParseException("Value is too short!", -1);
        }

        if ((splittedTimeString[0].length() > 2) || (splittedTimeString[1].length() > 2)) {
            throw new ParseException("Value is too long!", -1);
        }

        if ((splittedTimeString[0].charAt(0) < '0') || (splittedTimeString[0].charAt(0) > '9')) {
            throw new ParseException("Character is not a number!", 0);
        }

        if (splittedTimeString[0].length() == 2) {
            if ((splittedTimeString[0].charAt(1) < '0') || (splittedTimeString[0].charAt(1) > '9')) {
                throw new ParseException("Character is not a number!", 1);
            }
        }

        if ((splittedTimeString[1].charAt(0) < '0') || (splittedTimeString[1].charAt(0) > '9')) {
            throw new ParseException("Character is not a number!", 1 + indexOfColon);
        }

        if ((splittedTimeString[1].charAt(1) < '0') || (splittedTimeString[1].charAt(1) > '9')) {
            throw new ParseException("Character is not a number!", 2 + indexOfColon);
        }

        int h = 0;

        if (splittedTimeString[0].length() == 2) {
            if (splittedTimeString[0].charAt(0) > '0') {
                h = Integer.valueOf(String.valueOf(splittedTimeString[0].charAt(0))) * 10;
            }
            h = h + Integer.valueOf(String.valueOf(splittedTimeString[0].charAt(1)));
        } else {
            h = h + Integer.valueOf(splittedTimeString[0]);
        }

        int m = 0;

        if (splittedTimeString[1].charAt(0) > '0') {
            m = Integer.valueOf(String.valueOf(splittedTimeString[1].charAt(0))) * 10;
        }
        m = m + Integer.valueOf(String.valueOf(splittedTimeString[1].charAt(1)));

        return new TimeHelper(h, m);
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

    public void copy(final TimeHelper other) {
        if (other == null) {
            valid = false;
            return;
        }

        hours = other.hours;
        minutes = other.minutes;
        valid = true;
    }

    public void add(final TimeHelper toAdd) {
        hours = hours + toAdd.hours;
        minutes = minutes + toAdd.minutes;

        if (minutes > 59) {
            hours = hours + minutes / 60;
            minutes = minutes % 60;
        }
    }

    public void subtract(final TimeHelper toSubtract) {
        hours = hours - toSubtract.hours;
        minutes = minutes - toSubtract.minutes;

        if (minutes < 0) {
            hours = hours + minutes / 60;
            minutes = (minutes % 60) * -1;
        }
    }
}