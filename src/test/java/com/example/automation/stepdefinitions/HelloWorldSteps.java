package com.example.automation.stepdefinitions;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.example.automation.pages.HelloWorldPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HelloWorldSteps {
    private final HelloWorldPage helloWorldPage = new HelloWorldPage();

    @Given("I open the hello world application")
    public void iOpenTheHelloWorldApplication() {
        helloWorldPage.open();
    }

    @Then("the page title contains {string}")
    public void thePageTitleContains(String expected) {
        assertTrue(helloWorldPage.heading().contains(expected),
                "Expected heading to contain '" + expected + "' but was '" + helloWorldPage.heading() + "'");
    }

    @And("the greeting reads {string}")
    public void theGreetingReads(String expected) {
        assertEquals(helloWorldPage.greeting(expected), expected);
    }

    @When("I refresh the greeting")
    public void iRefreshTheGreeting() {
        helloWorldPage.refreshGreeting();
    }
}
