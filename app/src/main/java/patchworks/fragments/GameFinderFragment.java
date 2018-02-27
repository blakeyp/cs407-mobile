package patchworks.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.InetAddress;
import java.util.ArrayList;

import patchworks.R;
import patchworks.adapters.GameAdapter;
import patchworks.utils.Game;

public class GameFinderFragment extends Fragment {

    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private ArrayList<Game> gameList;

    public GameFinderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_game_finder, container, false);

//        Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        recyclerView = view.findViewById(R.id.gamesRecyclerView);

        gameList = new ArrayList<>();
        adapter = new GameAdapter(getActivity(), gameList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        try {
            InetAddress netAddress = InetAddress.getByName("127.0.0.1");
            gameList.add(new Game(netAddress, "Game1", "Playing Level", 2, true));
            gameList.add(new Game(netAddress, "Game2", "Building a Level", 3, false));
            gameList.add(new Game(netAddress, "Game3", "GameState", 1, false));
            gameList.add(new Game(netAddress, "Game4", "GameState", 0, true));
            gameList.add(new Game(netAddress, "Game5", "GameState", 0, true));
            gameList.add(new Game(netAddress, "Game6", "GameState", 1, false));
        } catch (java.net.UnknownHostException e) {
            Log.d("fuck ", "something bad happened");
        }

        adapter.notifyDataSetChanged();

        return view;
    }

}
