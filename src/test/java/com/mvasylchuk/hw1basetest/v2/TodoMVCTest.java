package com.mvasylchuk.hw1basetest.v2;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.*;

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


    @Test
    public void testTasksReview() {

        SelenideElement taskNameField = $("#new-todo");
        taskNameField.val("task1").pressEnter();
        taskNameField.val("task2").pressEnter();
        taskNameField.val("task3").pressEnter();
        taskNameField.val("task4").pressEnter();
        ElementsCollection tasks = $$("#todo-list > li");
        tasks.shouldHave(texts("task1", "task2", "task3", "task4"));
        tasks.find(text("task2")).hover().find(".destroy").click();
        tasks.shouldHave(texts("task1", "task3", "task4"));
        //mark "task4" as complete
        tasks.find(text("task4")).find("input.toggle").click();
        // mark all tasks as complete
        $("input#toggle-all").click();
        // clear all completed tasks
        $("button#clear-completed").click();
        // verify that tasks list is cleared
        $$("#todo-list > li").shouldHave(size(0));

        sleep(5000);


    }


}
