package com.convention.notification.app;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsRowAdapter extends ArrayAdapter<Item> {

	private Activity activity;
	private List<Item> items;
	private Item objBean;
	private int row;
	private List<Integer> disable;
	View view ;
	int disableView;

	public NewsRowAdapter(Activity act, int resource, List<Item> arrayList, List<Integer> disableList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;
		this.disable=disableList;

	}
	
	public int getCount() {
	    return items.size();        
	}
	public Item getItem(int position) {
	    return items.get(position);
	}
	public long getItemId(int position) {
	    return position;
	}


	    @Override
	    public int getItemViewType(int position) {

       	for(int k =0;k < disable.size();k++){
       		if(position==disable.get(k)){
       			
       			disableView=disable.get(k);
       			System.out.println("disagle view at" +disableView);
       				
       		} 
   
       	}
          

	return position;
}
  
			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View view = convertView;
				ViewHolder holder;

				if (view == null) {
					LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(row, null);

					getItemViewType(position);
				
					if(position==disableView){ 
						view.setBackgroundColor(Color.YELLOW);
						
						}else{
							view.setBackgroundColor(Color.WHITE);
						
						}
	
					
					//ViewHolder is a custom class that gets TextViews by name: tvName, tvCity, tvBDate, tvGender, tvAge;
					holder = new ViewHolder();
					
					/* setTag Sets the tag associated with this view. A tag can be used to
					 *  mark a view in its hierarchy and does not have to be unique 
					 *  within the hierarchy. Tags can also be used to store data within
					 *   a view without resorting to another data structure.

		*/
					view.setTag(holder);
				} else {
					
					getItemViewType(position);
		
					if(position==disableView){ 
						view.setBackgroundColor(Color.YELLOW);
					
						
						}else{
							view.setBackgroundColor(Color.WHITE);
							
						}
					
					//the Object stored in this view as a tag
					holder = (ViewHolder) view.getTag();
				}

				if ((items == null) || ((position + 1) > items.size()))
					return view;

				objBean = items.get(position);


		holder.tv_event_name = (TextView) view.findViewById(R.id.tv_event_name);
		holder.tv_event_date = (TextView) view.findViewById(R.id.tv_event_date);
		holder.tv_event_start = (TextView) view.findViewById(R.id.tv_event_start);
		holder.tv_event_end = (TextView) view.findViewById(R.id.tv_event_end);
		holder.tv_event_location = (TextView) view.findViewById(R.id.tv_event_location);


		if (holder.tv_event_name != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.tv_event_name.setText(Html.fromHtml(objBean.getName()));
			
		}
		if (holder.tv_event_date != null && null != objBean.getDate()
				&& objBean.getDate().trim().length() > 0) {
			holder.tv_event_date.setText(Html.fromHtml(objBean.getDate()));
		}
		if (holder.tv_event_start != null && null != objBean.getStartTime()
				&& objBean.getStartTime().trim().length() > 0) {
			holder.tv_event_start.setText(Html.fromHtml(objBean.getStartTime()));
		}
		if (holder.tv_event_end != null && null != objBean.getEndTime()
				&& objBean.getEndTime().trim().length() > 0) {
			holder.tv_event_end.setText(Html.fromHtml(objBean.getEndTime()));
		}
		if (holder.tv_event_location != null && null != objBean.getLocation ()
				&& objBean.getLocation ().trim().length() > 0) {
			holder.tv_event_location.setText(Html.fromHtml(objBean.getLocation ()));
			
		}
	
		
		return view;
	}

	public class ViewHolder {
		public TextView 
		tv_event_name,
		tv_event_date,
		tv_event_start,
		tv_event_end,
		tv_event_location
		/*tv_event_delete_flag*/;
		
		
	}
	
	 
}