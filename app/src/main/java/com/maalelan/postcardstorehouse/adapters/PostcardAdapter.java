package com.maalelan.postcardstorehouse.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private OnPostcardFavoriteToggleListener postcardClickListener;

    private OnPostcardDeleteListener deleteListener;

    /**
     * Interface for listening to favorite checkbox toggle events.
     */
    public interface OnPostcardFavoriteToggleListener {
        void onFavoriteToggle(Postcard postcard, boolean isFavorite);
    }

    /**
     * Register a listener for favorite toggle events.
     *
     * @param listener The listener to notify when postcard´s favorite checkbox toggle events.
     */
    public void setPostcardFavoriteToggleListener(OnPostcardFavoriteToggleListener listener) {
        this.postcardClickListener = listener;
    }

    /**
     * Interface for listening to postcard delete events.
     */
    public interface OnPostcardDeleteListener {
        void onDelete(Postcard postcard);
    }

    /**
     * Registers a listener for postcard delete events.
     *
     * @param listener The listener to notify when a postcard is deleted
     */
    public void setOnPostcardDeleteListener(OnPostcardDeleteListener listener) {
        this.deleteListener = listener;
    }

    /**
     * Sets the list of postcards to be displayed and refreshes the RecyclerView.
     *
     * @param postcards List of postcard objects to display
     */
    public void setPostcards(List<Postcard> postcards) {
        this.postcards = postcards;
        notifyDataSetChanged(); //Notify RecyclerView to refresh the UI
    }

    /**
     * Sets the map of postcard thumbnail URIs and refreshes the view.
     *
     * @param thumbnails Map of postcard IDs and corresponding thumbnail image URIs
     */
    public void setPostcardThumbnails(Map<Long, String> thumbnails) {
        this.postcardThumbnails = thumbnails;
        notifyDataSetChanged();
    }

    /**
     * Removes a single postcard from the adapter and notifies RecyclerView.
     *
     * @param postcard The postcard to remove
     */
    public void removePostcard(Postcard postcard) {
        int position = postcards.indexOf(postcard);
        if (position != -1) {
            postcards.remove(position);
            notifyItemRemoved(position);
        }
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

        // Set data from the Postcard object to the corresponding views
        holder.textViewCountry.setText("Maa: " + postcard.getCountry());
        holder.textViewTopic.setText("Aihe: " + postcard.getTopic());

        // Convert the sent date to a string format before displaying
        holder.textViewDate.setText("Lähetyspäivä: " + DateUtils.format(postcard.getSentDate()));
        holder.checkBoxFavorite.setChecked(postcard.isFavorite());

        holder.checkBoxFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            postcard.setFavorite(isChecked);
            postcardClickListener.onFavoriteToggle(postcard, isChecked);
        });

        holder.deletePostcard.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(postcard);
            }
        });

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

        Button deletePostcard;

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
            deletePostcard = itemView.findViewById(R.id.button_delete);
        }
    }
}
