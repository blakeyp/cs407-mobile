package patchworks.adapters;

/**
 * Created by ben on 23/01/18.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import patchworks.R;
import patchworks.activities.LevelDetailActivity;
import patchworks.utils.Level;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.MyViewHolder> {

    private Context mContext;
    private List<Level> levelList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name, author;
        public RatingBar stars;

        public MyViewHolder(View view, final Context context) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            author = (TextView) view.findViewById(R.id.author);
            stars = (RatingBar) view.findViewById(R.id.rating);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    // item clicked
                    Intent intent = new Intent(context, LevelDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }


    public LevelAdapter(Context mContext, List<Level> levelList) {
        this.mContext = mContext;
        this.levelList = levelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_card, parent, false);
        itemView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                )
        );

        return new MyViewHolder(itemView, mContext);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Level level = levelList.get(position);
        holder.name.setText(level.getName());
        holder.author.setText(level.getAuthor());
        holder.stars.setRating(level.getStars());
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
        return levelList.size();
    }
}