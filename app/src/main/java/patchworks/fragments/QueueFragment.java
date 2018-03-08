package patchworks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patchworks.R;
import patchworks.adapters.LevelAdapter;
import patchworks.utils.Level;

public class QueueFragment extends Fragment {

    private RecyclerView recyclerView;
    private LevelAdapter adapter;
    private ArrayList<Level> levelList;

    public QueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        recyclerView = view.findViewById(R.id.levelsRecyclerView);

        levelList = new ArrayList<>();
        adapter = new LevelAdapter(getActivity(), levelList, R.layout.level_card_wide);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        levelList.add(new Level("Nice Level", "some_dude", 3.5f, R.drawable.preview1));
        levelList.add(new Level("Really Nice Level", "some_dude", 4.2f, R.drawable.preview2));
        levelList.add(new Level("Bad Level", "some_dude", 1.1f,R.drawable.preview3));

        adapter.notifyDataSetChanged();

        return view;
    }

}
