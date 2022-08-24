package com.jasonjat;

import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;

public class GUI implements ActionListener {

    public static void initialize() throws InterruptedException {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JButton button = new JButton("Hello");
        button.setBounds(10, 110, 300, 25);
        button.addActionListener(new GUI());
        panel.add(button);

        JButton button2 = new JButton("Bye");
        button2.setBounds(10, 140, 300, 25);
        button2.addActionListener(new GUI());
        panel.add(button2);

        frame.setVisible(true);


        Scanner scanner = new Scanner(System.in);
        System.out.println("Input username: ");
        String username = scanner.nextLine();
        System.out.println("Input password: ");
        String password = scanner.nextLine();

        ChromeOptions options = new ChromeOptions();
//
//        // Fixing 255 Error crashes
////        options.addArguments("--user-data-dir=C:\\Users\\jason\\AppData\\Local\\Google\\Chrome\\User Data");
////        options.addArguments("--profile-directory=Profile 5");
//
//        // Other
        options.addArguments("disable-infobars");

        options.setCapability(CapabilityType.PAGE_LOAD_STRATEGY, PageLoadStrategy.NORMAL);

        WebDriver driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        int id = 679672365;

        Cards cards = new Cards(id, driver);
        cards.getTerm().forEach(System.out::println);
        cards.getDefinition().forEach(System.out::println);

        driver.get("https://sso.philasd.org/cas/login");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//*[@id=\"username\"]")).sendKeys(username);
        driver.findElement(By.xpath("//*[@id=\"password\"]")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"fm1\"]/section[3]/input[4]")).click();
        Thread.sleep(5000);
        driver.get("https://mail.google.com/a/philasd.org/");
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@id=\"view_container\"]/div/div/div[2]/div/div[2]/div/div[1]/div/div/button")).click();
        Thread.sleep(2000);

        driver.get("https://www.quizlet.com/login/");
        Thread.sleep(1000);
        driver.findElements(By.className("UIButton")).get(0).click();
        Thread.sleep(1000);

        driver.get("https://www.quizlet.com/" + id + "/match");
        Thread.sleep(3000);
        driver.findElements(By.tagName("button")).get(6).click();

        Actions actions = new Actions(driver);

        // begin the game
        List<WebElement> matchCards = driver.findElements(By.className("MatchModeQuestionScatterTile"));
        while (!matchCards.isEmpty()) {
            Thread.sleep(500);
            WebElement card1 = matchCards.get(0);


            String matching = null;
            for (int i = 0; i<cards.term.size(); i++) {
                if (cards.term.get(i).equals(card1.getText())) matching=cards.definition.get(i);
            }

            if (matching==null) {
                for (int i = 0; i < cards.definition.size(); i++) {
                    if (cards.definition.get(i).equals(card1.getText())) matching = cards.term.get(i);
                }
            }

            System.out.println(card1.getText() + " matching with " + matching);

            // find matching
            WebElement match = null;
            for (WebElement c : matchCards) {
                if (c.getText().equals(matching)) {
                    actions.dragAndDrop(card1, c).perform();
                    match = c;
                }
            }
            System.out.println(matching);
            matchCards.remove(card1);
            matchCards.remove(match);

            double a = Math.random();

            double b = a > 0 ? 0 : a;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getActionCommand().equals("Bye")) {
            System.out.println("Do something that has to do with bye");
        } else {
            System.out.println("You are not bye!");
        }
    }
}
