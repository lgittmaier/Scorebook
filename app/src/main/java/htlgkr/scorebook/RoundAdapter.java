package htlgkr.scorebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RoundAdapter extends BaseAdapter{

    public List<Round> rounds;
    LayoutInflater layoutInflater;
    TextView nameTextView;
    TextView dateTextView;
    TextView overTextView;

    public RoundAdapter(List<Round> rounds, LayoutInflater layoutInflater) {
        this.rounds = rounds;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return rounds.size();
    }

    @Override
    public Object getItem(int position) {
        return rounds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Round round = rounds.get(position);     //current Round


        View view;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.listview_object, null);
        } else {
            view = convertView;
        }

        // sets the fields in the listview
        nameTextView = view.findViewById(R.id.nameField);
        dateTextView = view.findViewById(R.id.dateField);
        overTextView = view.findViewById(R.id.overField);

        nameTextView.setText(round.getName());
        dateTextView.setText(round.getDate());
        overTextView.setText(String.valueOf(round.getOver()));


        return view;
    }


}
