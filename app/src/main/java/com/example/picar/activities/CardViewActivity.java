package com.example.picar.activities;

import android.support.v4.app.Fragment;
import com.example.picar.RecyclerFragment;
import com.example.picar.SingleCardFragment;

public class CardViewActivity extends SingleCardFragment {
    @Override
    protected Fragment createFragment() {
        return new RecyclerFragment().newInstace();
    }
}
