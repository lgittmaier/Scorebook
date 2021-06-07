package htlgkr.scorebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.text.ParseException;
import java.util.List;

public class RoundAdapter extends BaseAdapter {

    public List<Round> rounds;
    LayoutInflater layoutInflater;

    public RoundAdapter(List<Round> rounds, LayoutInflater layoutInflater) {
        this.rounds = rounds;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Round note = notes.get(position);     //current Note

        // due date of note
        try {
            dueDate.setTime(sdf.parse(note.getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View view;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.listview_object, null);

        } else {
            view = convertView;
        }

        // sets the fields in the listview
        titleTextView = view.findViewById(R.id.titleField);
        dateTextView = view.findViewById(R.id.dateField);

        titleTextView.setText(note.getTitle());
        dateTextView.setText(note.getDateTime());

        // if note is due -> set background red
        if (dueDate.getTime().getTime() < now.getTime()) {
            titleTextView.setBackgroundResource(R.color.lightred);
            dateTextView.setBackgroundResource(R.color.lightred);
        } else {
            titleTextView.setBackgroundResource(R.color.lightgrey);
            dateTextView.setBackgroundResource(R.color.lightgrey);
        }

        return view;
    }
}
