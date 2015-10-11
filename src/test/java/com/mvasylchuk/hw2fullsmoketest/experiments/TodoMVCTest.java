package com.mvasylchuk.hw2fullsmoketest.experiments;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.*;


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
    public void testTasksE2E(){
        createTasks("1", "2", "3");
        assertTasksAre("1", "2", "3");
        assertItemsLeftCounter("3");

        editTaskAndCancel("1", "1 is edited");
        sleep(3000);
//        deleteTask("2");
//        assertTasksAre("1", "3", "4");
//
//        toggle("4");
//        clearCompleted();
//        assertTasksAre("1", "3");
//
//        toggleAll();
//        clearCompleted();
//        tasks.shouldBe(empty);
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

    private void editTaskAndSave(String oldText, String newText){

        $(By.xpath(String.format("//*[@id='todo-list']//li//label[text()='%s']", oldText))).doubleClick();
        SelenideElement input = $(By.xpath(String.format("//*[@id='todo-list']//li//label[text()='%s']/../../input[@class='edit']", oldText)));
        input.clear();
        input.sendKeys(newText + Keys.ENTER);
    }

    private void editTaskAndCancel(String oldText, String newText){

        $(By.xpath(String.format("//*[@id='todo-list']//li//label[text()='%s']", oldText))).doubleClick();
        SelenideElement input = $(By.xpath(String.format("//*[@id='todo-list']//li//label[text()='%s']/../../input[@class='edit']", oldText)));
        input.clear();
        input.sendKeys(newText + Keys.ESCAPE);
    }

    private void assertTasksAre(String... texts){
        tasks.shouldHave(exactTexts(texts));
    }

    private void assertCompletedTasksAre(String... texts){
        completedTasks.shouldHave(texts(texts));
    }

    private void assertActiveTasksAre(String... texts) {
        activeTasks.shouldHave(texts(texts));
    }

    private void assertItemsLeftCounter(String text){
        $("#todo-count > strong").shouldHave(exactText(text));
    }

    ElementsCollection tasks = $$("#todo-list > li");
    ElementsCollection completedTasks = tasks.filter(cssClass("completed"));
    ElementsCollection activeTasks = tasks.filter(cssClass("active"));



}
