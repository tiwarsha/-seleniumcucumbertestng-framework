package com.example.automation.pages;

import com.example.automation.config.ConfigManager;

public class HomePage extends BasePage {
    public void open() {
        driver().get(ConfigManager.baseUrl());
    }

    public String title() {
        return driver().getTitle();
    }
}
