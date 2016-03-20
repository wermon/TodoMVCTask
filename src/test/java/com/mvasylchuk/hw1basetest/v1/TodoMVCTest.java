package com.mvasylchuk.hw1basetest.v1;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

/**
 * Created by Max on 26.09.2015.
 */

public class TodoMVCTest {


    @BeforeClass
    public static void oneTimeSetUp() {
        // one-time initialization code
        System.out.println("@BeforeClass - oneTimeSetUp");
        Configuration.browser = "chrome";
        Configuration.holdBrowserOpen = false;
        System.setProperty("webdriver.chrome.driver", "D:\\Projects\\Java\\seleniumtest\\src\\test\\resources\\chromedriver.exe");

    }

    @Before
    public void SetUp() {
        open("http://todomvc.com/examples/troopjs_require/#/");
        sleep(1000);
    }

    @AfterClass
    public static void oneTimeTearDown() {
        // one-time cleanup code
        System.out.println("@AfterClass - oneTimeTearDown");
        sleep(5000);

    }

    @Test
    public void testCreateTask() {

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


}
