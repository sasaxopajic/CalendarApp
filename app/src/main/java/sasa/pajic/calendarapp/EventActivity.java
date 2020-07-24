package sasa.pajic.calendarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvEvent, tvEventDate;
    private EditText etLocation;
    public Button bOK, bSaveEvent;
    private ImageView ivIcon;
    private TextView tvTemp;
    private TimePicker tp;
    private CheckBox cbEvent;

    private HttpHelper httpHelper;
    private CalendarDbHelper dbHelper;

    private Bundle bundle;
    private String location, date, text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        tvEvent = findViewById(R.id.tv_event_name);
        tvEventDate = findViewById(R.id.tv_event_date);
        etLocation = findViewById(R.id.et_location);
        bOK = findViewById(R.id.b_OK);
        ivIcon = findViewById(R.id.iv_weather_icon);
        tvTemp = findViewById(R.id.tv_weather_temp);
        bSaveEvent = findViewById(R.id.b_save_event);
        tp = findViewById(R.id.tp_event);
        cbEvent = findViewById(R.id.cb_event);

        bOK.setOnClickListener(this);
        bSaveEvent.setOnClickListener(this);

        bundle = getIntent().getExtras();
        text = bundle.getString("elementText");
        date = bundle.getString("date");
        tvEvent.setText(text);
        tvEventDate.setText(date);

        /* get existing event location */
        dbHelper = new sasa.pajic.calendarapp.CalendarDbHelper(this);
        EventData event = dbHelper.readEvent(date, text);
        if (event != null) {
            etLocation.setText(event.getLocation());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_OK:
                getWeather();
                break;

            case R.id.b_save_event:
                if (location == null) {
                    Toast.makeText(this, "Sva polja morju biti popunjena.", Toast.LENGTH_SHORT).show();
                } else {
                    String hour = tp.getCurrentHour().toString();
                    String minute = tp.getCurrentMinute().toString();
                    if (hour.length() < 2)
                        hour = "0" + hour;
                    if (minute.length() < 2)
                        minute = "0" + minute;

                    String cbValue;
                    if (cbEvent.isChecked())
                        cbValue = "yes";
                    else
                        cbValue = "no";

                    EventData data = new EventData(date, text,
                            hour + ":" + minute, cbValue, location);
                    dbHelper.insert(data);

                    Intent intent = new Intent(this, DayActivity.class);
                    intent.putExtra("selectedDate", date);
                    startActivity(intent);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    /* get weather info from OpenWeatherMap */
    public void getWeather() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
                String API_KEY = "&APPID=5415baaa60c0df413c507063db746866&units=metric";
                String GET_WEATHER;
                location = etLocation.getText().toString();
                GET_WEATHER = BASE_URL + location + API_KEY;

                try {
                    httpHelper = new HttpHelper();
                    JSONObject jsonobject = httpHelper.getJSONObjectFromURL(GET_WEATHER);
                    final JSONObject mainobject = jsonobject.getJSONObject("main");
                    final JSONArray weatherarray = jsonobject.getJSONArray("weather");
                    final JSONObject jsonweather = weatherarray.getJSONObject(0);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int temp = mainobject.getInt("temp");
                                tvTemp.setText(String.valueOf(temp) + "° C");
                                String iconCode = jsonweather.getString("icon");
                                String iconUrl = "http://openweathermap.org/img/w/" + iconCode + ".png";
                                Picasso.with(EventActivity.this).load(iconUrl).into(ivIcon);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (location.matches("")) {
                                Toast.makeText(EventActivity.this, "Unesite lokaciju."
                                        + location, Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("EVENT ACTIVITY", "JSON parsing --> unable to get weather for" + location);
                                Toast.makeText(EventActivity.this, "Vremenska prognoza nije dostupna za lokaciju: "
                                        + location, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
