package com.hairhub.BookAnAppointment;

import java.time.LocalTime;
public class TimeSlot {
    private LocalTime start;
    private LocalTime end;

    public TimeSlot(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public boolean overlaps(TimeSlot other) {
        return !(end.isBefore(other.start) || start.isAfter(other.end));
    }
}


