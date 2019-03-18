import java.util.ArrayList;
import java.util.List;

public class TimeTable {
    private List<List<TimeSlot>> availableTimeSlots;
    private List<Course> courses;
    public TimeTable(List<List<TimeSlot>> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }

    public TimeTable(List<TimeSlot> availableDayTimeSlots, int workingDays) {
        this.availableTimeSlots = new ArrayList<>();
        for (int day = 0; day < workingDays; day++) {
            this.availableTimeSlots.add(new ArrayList<>(availableDayTimeSlots));
        }
    }
    // finds the appropriate time table
    public List<List<TimeSlot>> create TimeTable(){ // doing

    }
    private List<List<TimeSlot>> go(int currentTimeSlot, List<Lessons> availableLessons){ // doing recursion

    }
    public List<TimeSlot> getDayTimeslots(int day) { // TODO : days start from 0 or from 1? :D
        return this.availableTimeSlots.get(day);
    }

    public List<TimeSlot> setDayTimeslots(int day, List<TimeSlot> timeSlots) {
        return this.availableTimeSlots.set(day, timeSlots);
    }
}
