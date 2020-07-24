package sasa.pajic.calendarapp;

public class EventData {

    private String mDate;
    private String mName;
    private String mTime;
    private String mReminder;
    private String mLocation;

    public EventData(String date, String name, String time, String reminder, String location)
    {
        this.mDate = date;
        this.mName = name;
        this.mTime = time;
        this.mReminder = reminder;
        this.mLocation = location;
    }

    public String getDate() {
        return mDate;
    }

    public String getName() {
        return mName;
    }

    public String getTime() {
        return mTime;
    }

    public String getReminder() {
        return mReminder;
    }

    public String getLocation() {
        return mLocation;
    }
}
