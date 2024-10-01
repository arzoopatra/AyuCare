package com.alzpal.dashboardactivity.memorygames.avatars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alzpal.R;
import java.util.List;

public class AvatarAdapter extends BaseAdapter {

    private final Context context;
    private final List<Avatar> avatars;
    private final LayoutInflater inflater;
    private int selectedPosition = -1;

    public AvatarAdapter(Context context, List<Avatar> avatars) {
        this.context = context;
        this.avatars = avatars;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return avatars.size();
    }

    @Override
    public Object getItem(int position) {
        return avatars.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.items_avatar, parent, false);
        }

        ImageView avatarImageView = convertView.findViewById(R.id.avatar_image_view);
        Avatar avatar = avatars.get(position);
        avatarImageView.setImageResource(avatar.getImageResourceId());


        if (position == selectedPosition) {
            convertView.setBackgroundResource(R.drawable.avatar_selected_background);
        } else {
            convertView.setBackgroundResource(R.drawable.avatar_default_background);
        }


        convertView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });

        return convertView;
    }
    public int getSelectedPosition() {
        return selectedPosition;
    }


    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }
}
