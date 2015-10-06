package com.mvasylchuk;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.impl.CollectionElement;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Max on 26.09.2015.
 */

public class TodoMVCTest {

    public boolean ThrowEx(int a){
        if (a==1) {
            throw new RuntimeException();
        }
        return true;
    }

    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code
        System.out.println("@BeforeClass - oneTimeSetUp");
        Configuration.browser = "chrome";
        Configuration.holdBrowserOpen = false;
        System.setProperty("webdriver.chrome.driver", "D:\\Projects\\Java\\seleniumtest\\src\\test\\resources\\chromedriver.exe");

    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        System.out.println("@AfterClass - oneTimeTearDown");
        sleep(5000);

    }

    @Test
    public void testCreateTask(){


        open("http://todomvc.com/examples/troopjs_require/#/");

        $("#new-todo").val("task1").pressEnter();
        $("#new-todo").val("task2").pressEnter();
        $("#new-todo").val("task3").pressEnter();
        $("#new-todo").val("task4").pressEnter();
        $$("#todo-list > li").shouldHave(size(4));
        SelenideElement deleteButton = $("#todo-list > li:nth-child(2) button.destroy");

        $("#todo-list > li:nth-child(2)").hover();
        deleteButton.click();
        $$("#todo-list > li").shouldHave(size(3));
        $("#todo-list > li:nth-child(3) input.toggle").click();
        $("input#toggle-all").click();
        $("button#clear-completed").click();
        $$("#todo-list > li").shouldHave(size(0));



    }

    @Test
    public void testTasksReview(){

        open("http://todomvc.com/examples/troopjs_require/#/");
        SelenideElement taskNameField = $("#new-todo");
        taskNameField.val("task1").pressEnter();
        taskNameField.val("task2").pressEnter();
        taskNameField.val("task3").pressEnter();
        taskNameField.val("task4").pressEnter();
        ElementsCollection tasks = $$("#todo-list > li");
        tasks.shouldHave(texts("task1", "task2", "task3", "task4"));
        tasks.find("");

    }


}
