package com.golden.universitycourses;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnJsonListItemSelected} interface
 * to handle interaction events.
 * Use the {@link JsonListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JsonListFragment extends Fragment {
    private String title;
    private JSONArray jsonArray;
    private int listId;

    private OnJsonListItemSelected mListener;

    public JsonListFragment() {
        // Required empty public constructor
    }

    public static JsonListFragment newInstance(String title, JSONArray jsonArray, int listId) {
        JsonListFragment fragment = new JsonListFragment();
        fragment.title = title;
        fragment.jsonArray = jsonArray;
        fragment.listId = listId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_json_list, container, false);
        TextView tvTitle = view.findViewById(R.id.tv_list_title);
        ListView listView = view.findViewById(R.id.list_view);

        tvTitle.setText(title);
        JsonListAdapter adapter = new JsonListAdapter(getContext(), jsonArray);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemSelected(position, listId);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnJsonListItemSelected) {
            mListener = (OnJsonListItemSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnJsonListItemSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnJsonListItemSelected {
        void onItemSelected(int index, int listId);
    }
}
