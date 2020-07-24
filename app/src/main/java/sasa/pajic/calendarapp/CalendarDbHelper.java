package sasa.pajic.calendarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "calendarEvents.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "event";

    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_TIME = "Time";
    public static final String COLUMN_REMINDER = "Reminder";
    public static final String COLUMN_LOCATION = "Location";

    private SQLiteDatabase mDb = null;

    public CalendarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_DATE + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_REMINDER + " TEXT, " +
                COLUMN_LOCATION + " TEXT);" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(EventData data) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, data.getDate());
        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_TIME, data.getTime());
        values.put(COLUMN_REMINDER, data.getReminder());
        values.put(COLUMN_LOCATION, data.getLocation());

        db.insert(TABLE_NAME, null, values);
        close();
    }

    /* read entire database */
    public EventData[] readEvents() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null,
                null, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        EventData[] events = new EventData[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            events[i++] = createEvent(cursor);
        }

        close();
        return events;
    }

    /* find events by date and name */
    public EventData[] readEventsDateName(String date, String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_DATE + "=?" + " AND " + COLUMN_NAME + "=?",
                new String[] {date, name}, null, null, null);

        if (cursor.getCount() <= 0) {
            return null;
        }

        EventData[] events = new EventData[cursor.getCount()];
        int i = 0;
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            events[i++] = createEvent(cursor);
        }

        close();
        return events;
    }

    /* find specific event by date and name */
    public EventData readEvent(String date, String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_DATE + "=?" + " AND " + COLUMN_NAME + "=?",
                new String[] {date, name}, null, null, null);
        if (cursor.getCount() <= 0) {
            return null;
        }
        cursor.moveToFirst();
        EventData event = createEvent(cursor);

        close();
        return event;
    }

    public void deleteEvent(String name, String date) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + "=?" + " AND " + COLUMN_DATE + "=?" , new String[] {name, date});
        close();
    }

    private EventData createEvent(Cursor cursor) {
        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
        String reminder = cursor.getString(cursor.getColumnIndex(COLUMN_REMINDER));
        String location = cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION));

        return new EventData(date, name, time, reminder, location);
    }
}
