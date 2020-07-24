package sasa.pajic.calendarapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton godina, mesec;
    private Button trenutnaGodina;
    private LinearLayout lGodina, lMesec, lDan;
    private CalendarView cvCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        TextView current_year = findViewById(R.id.current_year);
        Button buttonYear = findViewById(R.id.year);
        current_year.setText("" + year);
        buttonYear.setText("" + year);


        godina = (RadioButton) findViewById(R.id.godina);
        mesec = (RadioButton) findViewById(R.id.mesec);
        trenutnaGodina = (Button) findViewById(R.id.year);

        godina.setOnClickListener(this);
        mesec.setOnClickListener(this);
        trenutnaGodina.setOnClickListener(this);

        lGodina = (LinearLayout) findViewById(R.id.layoutGodina);
        lMesec = (LinearLayout) findViewById(R.id.layoutMesec);
        lDan = (LinearLayout) findViewById(R.id.layoutDan);

        lGodina.setVisibility(View.INVISIBLE);
        lMesec.setVisibility(View.INVISIBLE);
        lDan.setVisibility(View.INVISIBLE);

        cvCalendar = findViewById(R.id.calendar);
        cvCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                //rgButtons.clearCheck();
                String d = String.valueOf(dayOfMonth);
                String m = String.valueOf(month + 1);
                String y = String.valueOf(year);
                if (d.length() < 2)
                    d = "0" + d;
                if (m.length() < 2)
                    m = "0" + m;
                String date = d + "." + m + "." + y;

                Intent intent = new Intent(MainActivity.this, DayActivity.class);
                intent.putExtra("selectedDate", date);
                startActivity(intent);
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.godina:
            case R.id.year:
                lGodina.setVisibility(View.VISIBLE);
                godina.setChecked(true);
                lMesec.setVisibility(View.INVISIBLE);
                mesec.setChecked(false);
                break;
            case R.id.mesec:
                lGodina.setVisibility(View.INVISIBLE);
                godina.setChecked(false);
                lMesec.setVisibility(View.VISIBLE);
                mesec.setChecked(true);
                break;
        }
    }
}
