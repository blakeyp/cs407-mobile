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
        adapter = new LevelAdapter(getActivity(), levelList, R.layout.level_card);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        levelList.add(new Level("Lots of Sweets!", "a_user", 5.0f, R.drawable.preview1));
        levelList.add(new Level("Boxes", "patch", 4.2f, R.drawable.preview2));
        levelList.add(new Level("Not Mario 1-1", "miyamoto", 1.1f, R.drawable.preview3));
        levelList.add(new Level("Sweets Anyone?", "a_user", 3.4f, R.drawable.preview1));
        levelList.add(new Level("Can you hear me at the back?", "mike_joy", 4.2f, R.drawable.preview3));

        adapter.notifyDataSetChanged();

        return view;
    }

}
