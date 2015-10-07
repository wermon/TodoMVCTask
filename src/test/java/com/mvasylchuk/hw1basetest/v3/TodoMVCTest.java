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
    public static void SetUp() {

        Configuration.browser = "chrome";
        System.setProperty("webdriver.chrome.driver", "D:\\Projects\\Java\\seleniumtest\\src\\test\\resources\\chromedriver.exe");

    }

    @Before
    public void OpenToMVCPage(){
        open("http://todomvc.com/examples/troopjs_require/#/");
        sleep(1000);
    }



    @Test
    public void testTasksBase(){
        createTasks("1", "2", "3", "4");
        assertTasksAre("1", "2", "3", "4");

        deleteTask("2");
        assertTasksAre("1", "3", "4");

        toggle("4");

        clearCompleted();
        assertTasksAre("1", "3");

        toggleAll();

        clearCompleted();
        tasks.shouldBe(empty);
    }

    private void clearCompleted(){
        $("#clear-completed").click();
    }

    private void createTasks(String... taskTexts){
        for (String text: taskTexts){
            $("#new-todo").val(text).pressEnter();
        }
    }

    private void deleteTask(String taskText){
        tasks.find(exactText(taskText)).hover().find(".destroy").click();
    }

    private void toggle(String taskText){
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAll(){
        $("#toggle-all").click();
    }

    private void assertTasksAre(String... texts){
        tasks.shouldHave(texts(texts));
    }

    ElementsCollection tasks = $$("#todo-list > li");


}
