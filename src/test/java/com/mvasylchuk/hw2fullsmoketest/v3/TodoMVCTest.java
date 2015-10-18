package com.mvasylchuk.hw2fullsmoketest.v3;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


/**
 * Created by Max on 26.09.2015.
 */

public class TodoMVCTest {


    @Before
    public void OpenToMVCPage(){
        open("http://todomvc.com/examples/troopjs_require/#/");
        getWebDriver().navigate().refresh();
    }

    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }

    @Test
    public void testTasksE2E(){
        createTasks("1");
        assertItemsLeftCounter(1);

        goToActive();
        toggle("1");
        tasks.filter(visible).shouldBe(empty);

        createTasks("2", "3");
        editTask("2", "2 is edited");
        startEdit("3", "cancel 3").sendKeys(Keys.ESCAPE);
        assertItemsLeftCounter(2);

        deleteTask("2 is edited");
        assertTasksAre("3");

        toggleAll();
        tasks.filter(visible).shouldBe(empty);

        goToCompleted();
        assertTasksAre("1", "3");
        assertItemsLeftCounter(0);

        clearCompleted();
        tasks.shouldBe(empty);
        footer.shouldBe(hidden);

    }

    @Test
    public void SaveWithEmptyName(){
       createTasks("1");
       editTask("1", "");
       tasks.filter(visible).shouldBe(empty);
    }

    @Test
    public void ActivateAll(){
        createTasks("1", "2");
        toggleAll();
        toggleAll();
        goToActive();
        assertTasksAre("1", "2");
        assertItemsLeftCounter(2);
    }

    @Test
    public void SaveByClickOnOtherTask(){
        createTasks("1", "2");
        assertTasksAre("1", "2");
        startEdit("1", "1 is edited");
        tasks.find(exactText("2")).click();
        assertTasksAre("1 is edited", "2");

    }

    private void clearCompleted(){
        $("#clear-completed").click();
        clearButton.shouldBe(hidden);
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

    private void editTask(String oldName, String newName){

        startEdit(oldName, newName).setValue(newName).pressEnter();
    }

    private SelenideElement startEdit(String oldName, String newName){
        tasks.find(exactText(oldName)).find("label").doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newName);
    }

    private void goToAll(){
        $(By.cssSelector("[href='#/']")).click();
    }

    private void goToActive(){
        $(By.cssSelector("[href='#/active']")).click();
    }

    private void goToCompleted(){
        $(By.cssSelector("[href='#/completed']")).click();
    }

    private void assertTasksAre(String... texts){
        tasks.filter(visible).shouldHave(exactTexts(texts));
    }

    private void assertItemsLeftCounter(int counterValue){
        $("#todo-count > strong").shouldHave(exactText(Integer.toString(counterValue)));
    }

    ElementsCollection tasks = $$("#todo-list > li");
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement footer = $("#footer");


}
