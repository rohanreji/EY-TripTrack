package com.maangalabs.transitnow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class SearchFragment extends Fragment implements onRefreshListener {
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.search_layout, container, false);
         
        return rootView;
    }

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}
 
}