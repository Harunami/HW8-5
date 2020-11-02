package com.example.hw8.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private String mText;

    public HomeViewModel() {
        mText = "This is home fragment.";
    }

    public String getText() {
        return mText;
    }
}