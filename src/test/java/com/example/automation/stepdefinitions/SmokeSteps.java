package com.example.automation.stepdefinitions;

import com.example.automation.pages.HomePage;
import com.example.automation.utils.Log;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;

public class SmokeSteps {
    private static final Logger LOG = Log.get(SmokeSteps.class);
    private final HomePage homePage = new HomePage();

    @Given("I open the configured application")
    public void iOpenTheConfiguredApplication() {
        homePage.open();
        LOG.info("Opened page with title: {}", homePage.title());
    }

    @Then("I print hello")
    public void iPrintHello() {
        System.out.println("hello");
        LOG.info("hello");
    }
}
