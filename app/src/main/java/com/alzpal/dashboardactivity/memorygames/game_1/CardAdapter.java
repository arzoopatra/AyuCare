package com.alzpal.dashboardactivity.memorygames.game_1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.alzpal.R;

import java.util.List;

public class CardAdapter extends BaseAdapter {

    private final Context context;
    private final List<Card> cards;
    private final CardClickListener cardClickListener;

    public CardAdapter(Context context, List<Card> cards, CardClickListener cardClickListener) {
        this.context = context;
        this.cards = cards;
        this.cardClickListener = cardClickListener;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);
        }

        final Card card = cards.get(position);
        ImageButton cardButton = convertView.findViewById(R.id.card_button);

        // Set visibility and image based on card state
        if (card.isVisible()) {
            cardButton.setVisibility(View.VISIBLE);
            if (card.isFlipped()) {
                cardButton.setImageResource(card.getImageResource());
            } else {
                cardButton.setImageResource(R.drawable.card_back); // Image for the back of the card
            }
        } else {
            cardButton.setVisibility(View.INVISIBLE); // Keep space but hide the card
        }

        cardButton.setOnClickListener(v -> cardClickListener.onCardClicked(card));

        return convertView;
    }

    public interface CardClickListener {
        void onCardClicked(Card card);
    }
}
