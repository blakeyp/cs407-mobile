package patchworks.adapters;

/**
 * Created by ben on 23/01/18.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import patchworks.utils.Game;
import patchworks.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder> {

    private Context mContext;
    private List<Game> gameList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, state, players;
        public ImageView pin;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            state = (TextView) view.findViewById(R.id.state);
            players = (TextView) view.findViewById(R.id.players);
            pin = (ImageView) view.findViewById(R.id.lock);
        }
    }


    public GameAdapter(Context mContext, List<Game> gameList) {
        this.mContext = mContext;
        this.gameList = gameList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_card, parent, false);
        itemView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Game game = gameList.get(position);
        holder.name.setText(game.getName());
        holder.state.setText(game.getState());
        holder.players.setText(game.getPlayers() + "/4");
        holder.pin.setVisibility(game.getPinProtected() ? View.VISIBLE : View.INVISIBLE);
//
//        holder.overflow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.overflow);
//            }
//        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_album, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }
}