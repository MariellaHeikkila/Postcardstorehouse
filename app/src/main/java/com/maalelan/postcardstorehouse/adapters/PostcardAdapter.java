package com.maalelan.postcardstorehouse.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maalelan.postcardstorehouse.R;
import com.maalelan.postcardstorehouse.models.Postcard;
import com.maalelan.postcardstorehouse.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecyclerView Adapter for displaying a list of Postcard objects.
 * This adapter binds postcard data to item views for display in a list or grid.
 */
public class PostcardAdapter extends RecyclerView.Adapter<PostcardAdapter.PostcardViewHolder> {

    private List<Postcard> postcards;
    private Map<Long, String> postcardThumbnails = new HashMap<>();

    /**
     * Sets the list of postcards to be displayed and refreshes the RecyclerView.
     * @param postcards List of postcard objects to display
     */
    public void setPostcards(List<Postcard> postcards) {
        this.postcards = postcards;
        notifyDataSetChanged(); //Notify RecyclerView to refresh the UI
    }

    /**
     *
     */
    public void setPostcardThumbnails(Map<Long, String> thumbnails) {
        this.postcardThumbnails = thumbnails;
        notifyDataSetChanged();
    }

    /**
     * Creates a new ViewHolder when there are no existing view holders that can be reused.
     * @param parent The parent ViewGroup into which the new view will be added
     * @param viewType The view type of the new View
     * @return A new postcardViewHolder instance
     */
    @NonNull
    @Override
    public PostcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postcard, parent, false);
        return new PostcardViewHolder(view);
    }

    /**
     * Binds data to the specified ViewHolder.
     * @param holder The ViewHolder which should be updated
     * @param position The position of the item within the adapter´s data set
     */
    @Override
    public void onBindViewHolder(@NonNull PostcardViewHolder holder, int position) {
        Postcard postcard = postcards.get(position);

        Log.d("Adapter", "Topic for id " + postcard.getId() + ": " + postcard.getTopic());

        // Set data from the Postcard object to the corresponding views
        holder.textViewCountry.setText(postcard.getCountry());
        holder.textViewTopic.setText(postcard.getTopic());

        // Convert the sent date to a string format before displaying
        holder.textViewDate.setText(DateUtils.format(postcard.getSentDate()));
        holder.checkBoxFavorite.setChecked(postcard.isFavorite());

        // Load thumbnail if available
        String uri = postcardThumbnails.get(postcard.getId());
        if (uri != null) {
            Glide.with(holder.itemView.getContext())
                    .load(uri)
                    .placeholder(R.drawable.ic_camera_placeholder)
                    .into(holder.imageViewThumbnail);
        } else {
            holder.imageViewThumbnail.setImageResource(R.drawable.ic_camera_placeholder);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter
     * @return The number of postcards
     */
    @Override
    public int getItemCount() {
        return postcards == null ? 0 : postcards.size();
    }

    /**
     * ViewHolder class that holds references to the UI components of each list item.
     */
    public static class PostcardViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCountry, textViewTopic, textViewDate;
        ImageView imageViewThumbnail;

        CheckBox checkBoxFavorite;

        /**
         * Constructor that binds views from the layout.
         * @param itemView The root view of the item layout
         */
        public PostcardViewHolder(View itemView) {
            super(itemView);
            textViewCountry = itemView.findViewById(R.id.text_country);
            textViewTopic = itemView.findViewById(R.id.text_topic);
            textViewDate = itemView.findViewById(R.id.text_sent_date);
            imageViewThumbnail = itemView.findViewById(R.id.image_thumbnail);
            checkBoxFavorite = itemView.findViewById(R.id.checkbox_favorite);
        }
    }
}
