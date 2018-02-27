package patchworks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import patchworks.R;
import patchworks.adapters.GameAdapter;
import patchworks.adapters.LevelAdapter;
import patchworks.utils.Game;
import patchworks.utils.Level;

/**
 * Created by u1421499 on 29/01/18.
 */

public class FeaturedLevelsFragment extends Fragment {

    private RecyclerView recyclerView;
    private LevelAdapter adapter;
    private ArrayList<Level> levelList;

    public FeaturedLevelsFragment() {
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
        adapter = new LevelAdapter(getActivity(), levelList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        levelList.add(new Level("Nice Level", "some_dude", 3.5f));
        levelList.add(new Level("Really Nice Level", "some_dude", 4.2f));
        levelList.add(new Level("Bad Level", "some_dude", 1.1f));

        adapter.notifyDataSetChanged();

        return view;
    }

}
