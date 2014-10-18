package uqtr.covoituragemobile;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import model.Ad;
import model.AdsListAdapter;

/**
 * Created by gabriellamer on 2014-10-15.
 */
public class SearchAdsFragment extends Fragment {
    private ArrayAdapter<Ad> arrayAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_search_ads, container, false);

        arrayAdapter = new AdsListAdapter(this.getActivity(), R.layout.list_ad, Search.listAds);

        listView = (ListView) rootView.findViewById(R.id.search_ads_lv);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent addIntent = new Intent(rootView.getContext(), Ads.class);
                addIntent.putExtra("adId", (int)arg3);
                startActivity(addIntent);
            }
        });

        return rootView;
    }

    public void refreshList() {
        arrayAdapter = new AdsListAdapter(this.getActivity(), R.layout.list_ad, Search.listAds);
        listView.setAdapter(arrayAdapter);
    }
}