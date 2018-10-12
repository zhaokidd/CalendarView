package com.haibin.calendarview;

public class MensesCalendar {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_PAILUAN_DURATION = 1;
    public final static int STATE_JINGQI = 2;
    public final static int STATE_PAILUAN_DATE = 3;

    public static Calendar setUpMensesCalendar(Calendar calendar) {
        if (calendar.getDay() % 4 == 0) {
            calendar.setMensesState(STATE_JINGQI);
        } else if (calendar.getDay() % 3 == 0) {
            calendar.setMensesState(STATE_PAILUAN_DATE);
        } else if (calendar.getDay() % 2 == 0) {
            calendar.setMensesState(STATE_PAILUAN_DURATION);
        } else {
            calendar.setMensesState(STATE_NORMAL);
        }
        return calendar;
    }
}
