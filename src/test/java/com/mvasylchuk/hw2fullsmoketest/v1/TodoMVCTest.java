package com.mvasylchuk.hw2fullsmoketest.v1;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.io.Console;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exactTextCaseSensitive;
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

        toggleAll();
        assertCompletedTasksAre("1", "2", "3");
        assertItemsLeftCounter("0");

        //mark task as active
        toggle("1");
        assertActiveTasksAre("1");
        assertItemsLeftCounter("1");

        //mark all as completed
        toggleAll();
        assertCompletedTasksAre("1", "2", "3");
        assertItemsLeftCounter("0");

        //mark all as Active
        toggleAll();
        assertActiveTasksAre("1", "2", "3");
        assertItemsLeftCounter("3");

        editTaskAndCancel("1", "");
        assertTasksAre("1", "2", "3");

        editTaskAndSave("1", "1 is edited");
        // mark as completed
        toggle("1 is edited");
        assertCompletedTasksAre("1 is edited");

        goToFilter("Active");
        assertSelectedFilterIs("Active");

        editTaskAndSave("2", "");
        assertActiveTasksAre("3");

        editTaskAndCancel("3", "cancel of editing");
        createTasks("4");
        assertActiveTasksAre("3", "4");
        assertItemsLeftCounter("2");

        toggle("4");
        goToFilter("Completed");
        assertCompletedTasksAre("1 is edited", "4");

        clearCompleted();
        completedTasks.shouldBe(empty);

        goToFilter("All");
        deleteTask("3");
        tasks.shouldBe(empty);

        sleep(5000);
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

    private void goToFilter(String filterName){
        if(filterName == "All" || filterName=="Active" || filterName=="Completed"){
            $(By.xpath(String.format("//*[@id='filters']//a[text()='%s']", filterName))).click();
        }
        else{
            System.out.println(String.format("%s is wrong input parameter of method", filterName));
                   }
    }

    private void assertSelectedFilterIs(String filterName){
        $(By.xpath(String.format("//*[@id='filters']//a[text()='%s' and @class='selected']", filterName))).click();
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
