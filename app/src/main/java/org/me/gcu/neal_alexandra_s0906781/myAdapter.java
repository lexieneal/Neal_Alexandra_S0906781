//Student Name: Alexandra Neal
//Student ID: S0906781

package org.me.gcu.neal_alexandra_s0906781;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class myAdapter extends ArrayAdapter<TrafficItem> implements Filterable {
    public ArrayList<TrafficItem> origTrafficItems;
    public ArrayList<TrafficItem> trafficItems;
    private Filter filter;
    private final Context context;
    private Boolean inc = false;

    private static LayoutInflater inflater = null;

    public myAdapter (Context context, ArrayList<TrafficItem> _items) {
        super(context, 0, _items);
        this.context = context;
        this.origTrafficItems = _items;
        this.trafficItems = _items;
    }

    @Override
    public TrafficItem getItem(int position) {
        return trafficItems.get(position);
    }

    @Override
    public int getCount() {
        return trafficItems.size();
    }

    @Override
    public long getItemId(int position) {
        return trafficItems.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TrafficItem it = getItem(position);

            ItemHolder holder = new ItemHolder();

            if (convertView == null) {
                Log.d("MyTag", "No Filter");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_list_view, parent, false);
                TextView tvName = (TextView) convertView.findViewById(R.id.itemLocation);
                TextView tvPeriod = (TextView) convertView.findViewById(R.id.itemDates);
                LinearLayout LlClick= (LinearLayout) convertView.findViewById(R.id.trafficItems);

                holder.clickable = LlClick  ;
                holder.itemTitleView = tvName;
                holder.itemPeriodView = tvPeriod;

                tvName.setText(it.getTitle());
                tvPeriod.setText(it.getPeriod());

                inc = it.getInc();
                if (inc == false){
                TextView tvDuration= (TextView) convertView.findViewById(R.id.tvDuration);
                tvDuration.setText(Long.toString(it.getDuration())+" Hrs");
                    tvDuration.setBackgroundColor(Color.parseColor(it.getColour()));
                holder.itemDurationView = tvDuration;
                }
                convertView.setTag(holder);
            }else{
                holder = (ItemHolder) convertView.getTag();
            }

            final TrafficItem t = trafficItems.get(position);

           TextView tv =  holder.itemTitleView;
                   tv.setText(t.getTitle());
            if (inc == false) {
                holder.itemPeriodView.setText(t.getPeriod());
                holder.itemDurationView.setText(Long.toString(t.getDuration()) + " Hrs");
                holder.itemDurationView.setBackgroundColor(Color.parseColor(it.getColour()));
            }else {
                holder.itemPeriodView.setText("");
            }
            holder.clickable.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
//                    Log.e("MyTag","clicked! ");
                    Intent i = new Intent(context, ItemActivity.class);

                    i.putExtra("title", t.getTitle());
                    i.putExtra("desc", t.getDesc());
                    i.putExtra("desc0", t.getDesc0());
                    i.putExtra("inc", t.getInc());
                    i.putExtra("pub", t.getPubDate());
                    i.putExtra("geo", t.getGeoRss());

                    if(inc == false) {
                        i.putExtra("dur", Long.toString(t.getDuration()) + " hrs");
                        i.putExtra("period", t.getPeriod2());
                        i.putExtra("col", t.getColour());
                    }
                    context.startActivity(i);
                }
            });
            return convertView;
    }


    public void resetData() {
        trafficItems = origTrafficItems;
    }

    private static class ItemHolder {
        public TextView itemTitleView;
        public TextView itemPeriodView;
        public TextView itemDurationView;
        public LinearLayout clickable;
    }

    @Override
    public Filter getFilter() {
        //Using Search Bar to filter Results
        Log.d("MyTag","Start Filtering Process");
        if (filter == null)
            filter = new myFilter();
        return filter;
    }

private class myFilter extends Filter {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        Log.d("myAdapter","Filtering Traffic Items");
        FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            Log.d("MyTag","Nothing Entered - All Unfiltered Items Returned");
            ArrayList<TrafficItem> ar = new ArrayList<TrafficItem>(origTrafficItems);
            results.values = ar;
            results.count = ar.size();
        }
        else {
            Log.d("MyTag","User Searched For: "+constraint);
            ArrayList<TrafficItem> nItemList = new ArrayList<TrafficItem>();
            for (TrafficItem p : origTrafficItems) {
                if ((p.getTitle().toUpperCase().contains(constraint.toString().toUpperCase())) | (p.getPeriod().toUpperCase().contains(constraint.toString().toUpperCase()))) {
                        Log.d("MyTag","Results: "+p.toString());
                        nItemList.add(p);
                }
            }
        Log.d("MyTag","Returned Results: " +nItemList.size());
            results.values = nItemList;
            results.count = nItemList.size();
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint,
                                  FilterResults results) {
        if (results.count == 0)
            notifyDataSetInvalidated();
        else {
            trafficItems = (ArrayList<TrafficItem>) results.values;
            notifyDataSetChanged();
        }
    }
}

}




