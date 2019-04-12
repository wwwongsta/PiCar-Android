package com.example.picar.activities;

import android.support.v4.app.Fragment;

import com.example.picar.card_view.RecyclerFragment;
import com.example.picar.card_view.SIngleCardFragment;

public class CardViewActivity extends SIngleCardFragment {
    @Override
    protected Fragment createFragment() {
        return new RecyclerFragment().newInstace();
    }
}
