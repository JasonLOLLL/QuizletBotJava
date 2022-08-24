package com.jasonjat;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Cards {
    private final int quizletId;
    private HashMap<String, String> dictionary = new HashMap<>();
    public ArrayList<String> term = new ArrayList<>();
    public ArrayList<String> definition = new ArrayList<>();
    private WebDriver driver;

    public Cards(int id, WebDriver driver) {
        quizletId = id;
        this.driver = driver;
        activate();
    }

    private void activate() {

        driver.get("https://www.quizlet.com/" + quizletId);
        driver.findElements(By.className("SetPageTerms-term")).forEach(x -> {
           x.findElements(By.tagName("span")).forEach(s -> {
               WebElement parent = (WebElement) ((JavascriptExecutor) driver).executeScript(
                       "return arguments[0].parentNode;", s);
               if (parent.getAttribute("class").equals("SetPageTerm-wordText")) { // this is a term
                   term.add(s.getText());
               } else if (parent.getAttribute("class").equals("SetPageTerm-definitionText")) { // this is a definition
                  definition.add(s.getText());
               }
           });
        });
    }

    public HashMap<String, String> getDictionary() {
        return dictionary;
    }


    public ArrayList<String> getTerm() {
        return term;
    }

    public ArrayList<String> getDefinition() {
        return definition;
    }
}
