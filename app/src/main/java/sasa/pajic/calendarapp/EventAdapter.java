package sasa.pajic.calendarapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {

    private Context eContext;
    public ArrayList<Event> eEvents;

    public EventAdapter(Context context){
        eContext = context;
        eEvents = new ArrayList<>();
    }

    public void addEvent(Event event){
        eEvents.add(event);
        notifyDataSetChanged();
    }

    public void deleteEvent(int position) {
        eEvents.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return eEvents.size();
    }

    @Override
    public Object getItem(int position) {
        Object ev = null;
        try {
            ev = eEvents.get(position);
        } catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return ev;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) eContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.event, null);
            ViewHolder holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.event_name);
            holder.image = (ImageView) view.findViewById(R.id.bell);
            holder.checkBox = (CheckBox) view.findViewById(R.id.check_box);
            view.setTag(holder);
        }

        Event event = (Event) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(event.eName);
        holder.image.setImageDrawable(event.eImage);
        holder.checkBox.setChecked(event.checked);

        return view;
    }

    /*public void remove(Event event) {

        eEvents.remove(event);
        notifyDataSetChanged();
    }*/

    private class ViewHolder{
        public TextView name = null;
        public ImageView image = null;
        public CheckBox checkBox = null;
    }
}
