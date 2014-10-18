package model;

import java.util.List;

import uqtr.covoituragemobile.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdsListAdapter extends ArrayAdapter<Ad> {
	public AdsListAdapter(Context context, int resource, List<Ad> objects) {
		super(context, resource, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.list_ad, null);
		}
		
		if((position % 2) == 0) {
			v.setBackgroundColor(Color.argb(255, 70, 164, 86));
		} else {
			v.setBackgroundColor(Color.argb(255, 85, 203, 106));
		}
		
		Ad i = getItem(position);
		
		if (i != null) {
			TextView title = (TextView) v.findViewById(R.id.tvAdTitle);
			TextView description = (TextView) v.findViewById(R.id.tvAdDescription);
			
			if (title != null) {
				title.setText(i.getTitle());
			}
		
			if (description != null) {
				description.setText(i.getDescription());
			}
		}
		
		return v;
	}
}
