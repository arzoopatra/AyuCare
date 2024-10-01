package com.alzpal.dashboardactivity.memorygames.game_1;

import com.alzpal.R;

public class Card {

    private final int id;
    private boolean isFlipped;
    private boolean isMatched;
    private boolean isVisible;

    public Card(int id) {
        this.id = id;
        this.isFlipped = false;
        this.isMatched = false;
        this.isVisible = true;
    }

    public int getId() {
        return id;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void flip() {
        isFlipped = !isFlipped;
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getImageResource() {
        switch (id) {
            case 0: return R.drawable.card_img1;
            case 1: return R.drawable.card_img2;
            case 2: return R.drawable.card_img3;
            case 3: return R.drawable.card_img4;
            case 4: return R.drawable.card_img5;
            case 5: return R.drawable.card_img6;
            case 6: return R.drawable.card_img7;
            case 7: return R.drawable.card_img7;
            default: return R.drawable.card_back;
        }
    }
}
