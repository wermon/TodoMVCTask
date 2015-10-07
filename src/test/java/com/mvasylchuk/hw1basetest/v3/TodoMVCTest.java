package com.mvasylchuk.hw1basetest.v3;

import com.codeborne.selenide.*;
import com.codeborne.selenide.collections.ExactTexts;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static java.util.Arrays.asList;

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
    public void SetUp(){
        open("http://todomvc.com/examples/troopjs_require/#/");
        sleep(1000);
    }



    @Test
    public void testTasksBase(){

        //create tasks
        createTasks("1", "2", "3", "4");
        assertTasksAre("1", "2", "3", "4");

        //delete 2d task
        deleteTask("2");
        assertTasksAre("1", "3", "4");

        //mark "4" as complete
        toggle("4");

        // remove completed 4th task
        clearCompleted();
        assertTasksAre("1", "3");

        // mark all tasks as complete
        toggleAll();

        // clear all completed tasks
        clearCompleted();
        // verify that tasks list is cleared
        $$("#todo-list > li").shouldBe(empty);

        sleep(2000);


    }

    private void clearCompleted(){
        $("button#clear-completed").click();
    }

    private void createTask(String taskText){
        taskNameField.val(taskText).pressEnter();
    }

    private void createTasks(String... taskTexts){
        for (String text: taskTexts){
            createTask(text);
        }
    }

    private void deleteTask(String taskText){
        tasks.find(exactText("2")).hover().find(".destroy").click();
    }

    private void toggle(String taskText){
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAll(){
        $("input#toggle-all").click();
    }

    //Assetions
    private void assertTasksAre(String... texts){
        tasks.shouldHave(texts("1", "2", "3", "4"));
    }


    SelenideElement taskNameField = $("#new-todo");
    ElementsCollection tasks = $$("#todo-list > li");


}
