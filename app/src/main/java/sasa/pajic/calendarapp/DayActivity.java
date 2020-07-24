package sasa.pajic.calendarapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DayActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private EditText event;
    private EventAdapter adapter;
    private Button bDay;
    private String date;
    private TextView tvDate;

    private CalendarDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_activity);

        event = findViewById(R.id.add_event);
        adapter = new EventAdapter(this);

        bDay = findViewById(R.id.day_button);

        bDay.setOnClickListener(this);

        ListView list = findViewById(R.id.list_view);
        list.setAdapter(adapter);
        list.setOnItemLongClickListener(this);

        tvDate = findViewById(R.id.tv_date);

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("selectedDate");
        Log.d("datum1", date);
        tvDate.setText(date);

        dbHelper = new CalendarDbHelper(this);
        EventData[] events = dbHelper.readEvents();
        if (events != null) {
            for (EventData event : events) {
                if (event.getDate().matches(date)) {
                    adapter.addEvent(new Event(event.getName()));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, EventActivity.class);
        if (event.getText().toString().matches("")) {
            Toast.makeText(this, "Unesite naziv događaja.", Toast.LENGTH_SHORT).show();
        } else {
            EventData[] events = dbHelper.readEventsDateName(date, event.getText().toString());
            if (events == null) {
                intent.putExtra("elementText", event.getText().toString());
                intent.putExtra("date", date);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Događaj već postoji", Toast.LENGTH_SHORT).show();
            }
        }
        event.getText().clear();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Event event = adapter.eEvents.get(position);
        builder.setTitle(event.eName);
        builder.setMessage(R.string.alert_dialog);
        builder.setPositiveButton(R.string.alert_dialog_uredi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DayActivity.this, EventActivity.class);
                intent.putExtra("elementText", event.eName);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.alert_dialog_obrisi, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteEvent(position);
                dbHelper.deleteEvent(event.eName, date);
            }
        });

        builder.create();
        builder.show();
        return false;
    }
}


