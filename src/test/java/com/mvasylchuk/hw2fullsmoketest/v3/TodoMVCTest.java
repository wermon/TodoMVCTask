package com.mvasylchuk.hw2fullsmoketest.v3;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.collections.ExactTexts;
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
        assertItemsLeftCounter(1);
        toggle("1");
        createTasks("2", "3");
        edit("2", "2 is edited");
        editTaskAndCancel("3", "cancel 3");
        assertItemsLeftCounter(2);
        deleteTask("2 is edited");

        toggleAll();
        tasks.filter(visible).shouldBe(empty);

        goToFilter("Completed");
        assertTasksAre("1", "3");
        assertItemsLeftCounter(0);
        clearCompleted();
        tasks.shouldBe(empty);
        clearButton.shouldBe(hidden);
        footer.shouldBe(hidden);

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

    private void edit(String oldName, String newName){

        startEdit(oldName, newName).setValue(newName).pressEnter();
    }

    private SelenideElement startEdit(String oldName, String newName){
        tasks.find(exactText(oldName)).find("label").doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newName);
    }

    private void editTaskAndCancel(String oldName, String newName){

        SelenideElement input = startEdit(oldName, newName);
        input.clear();
        input.sendKeys(newName + Keys.ESCAPE);
    }

    private void goToFilter(String filterName){
        if(filterName == "All" || filterName=="Active" || filterName=="Completed"){
            $(By.xpath(String.format("//*[@id='filters']//a[text()='%s']", filterName)));
        }
        else{
            System.out.println(String.format("%s is wrong input parameter of method", filterName));
                   }
    }

    private void assertTasksAre(String... texts){
        tasks.filter(visible).shouldHave(exactTexts(texts));
    }

    private void assertItemsLeftCounter(int text){
        itemsLeftCounter.shouldHave(exactText(Integer.toString(text)));
    }

    ElementsCollection tasks = $$("#todo-list > li");
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement itemsLeftCounter = $("#todo-count > strong");
    SelenideElement footer = $("#footer");


}
