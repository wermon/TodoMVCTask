package com.mvasylchuk.hw2fullsmoketest.v3;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


/**
 * Created by Max on 26.09.2015.
 */

public class TodoMVCTest {


    @Before
    public void OpenToMVCPage(){
        open("http://todomvc.com/examples/troopjs_require/#/");
    }

    @Test
    public void testTasksE2E(){
        createTasks("1");

        goToFilter("Active");
        assertItemsLeftCounter("1");
        toggle("1");
        createTasks("2", "3");
        editTaskAndSave("2", "2 is edited");
        editTaskAndCancel("3", "cancel 3");
        deleteTask("2 is edited");
        assertItemsLeftCounter("1");
        toggleAll();
        tasks.shouldBe(empty);
        
        goToFilter("Completed");
        assertCompletedTasksAre("1", "3");
        clearCompleted();
        tasks.shouldBe(empty);
        clearButton.shouldBe(hidden);

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

        doubleClickOnTask(oldText);
        tasks.find(cssClass("editing")).find(".edit").setValue(newText).pressEnter();

    }

    private void doubleClickOnTask(String text){
        tasks.find(exactText(text)).find("label").doubleClick();
    }

    private void editTaskAndCancel(String oldText, String newText){

        doubleClickOnTask(oldText);
        SelenideElement input = tasks.find(cssClass("editing")).find(".edit");
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
        itemsLeftCounter.shouldHave(exactText(text));
    }

    ElementsCollection tasks = $$("#todo-list > li");
    ElementsCollection completedTasks = tasks.filter(cssClass("completed"));
    ElementsCollection activeTasks = tasks.filter(cssClass("active"));
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement itemsLeftCounter = $("#todo-count > strong");


}
