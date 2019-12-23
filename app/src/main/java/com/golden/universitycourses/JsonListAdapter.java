package com.golden.universitycourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonListAdapter extends BaseAdapter {
    private JSONArray jsonArray;
    private Context context;

    public JsonListAdapter(Context context, JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.context = context;
    }

    @Override
    public int getCount() {
        int d = jsonArray.length();
        return jsonArray.length();
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
        try {
            JSONObject curObject = jsonArray.getJSONObject(position);
            View view;
            TextView tvTitle;
            TextView tvDescription;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.list_layout, parent, false);
            } else {
                view = convertView;
            }

            tvTitle = view.findViewById(R.id.tv_item_title);
            tvDescription = view.findViewById(R.id.tv_item_description);
            tvTitle.setText(curObject.getString("name"));

            String description = curObject.getString("description");
            String descriptionSum = description.substring(0, Math.min(150, description.length()));
            tvDescription.setText(descriptionSum);
            return view;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
