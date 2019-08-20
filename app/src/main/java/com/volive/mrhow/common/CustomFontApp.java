package com.volive.mrhow.common;

import android.app.Application;

public class CustomFontApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/poppins-regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/poppins-regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/poppins-regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/poppins-regular.ttf");
    }
}
