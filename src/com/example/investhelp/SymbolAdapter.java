package com.example.investhelp;

import java.util.ArrayList;

import android.app.Notification.Style;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

public class SymbolAdapter extends ArrayAdapter<Style> implements Filterable {
	
	private ArrayList<Style> mData;

	public SymbolAdapter(Context context, int textViewResourceId){
		super(context, textViewResourceId);
		mData = new ArrayList<Style>();
	}
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Style getItem(int index) {
		return mData.get(index);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Create the cell (View) and populate it with an element from filter
		return null;
	}

	@Override
	public Filter getFilter() {
		Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                	constraint.toString().split(" ",2)[0];
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }

}
