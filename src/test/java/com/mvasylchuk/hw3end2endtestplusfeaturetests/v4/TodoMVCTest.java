package com.mvasylchuk.hw3end2endtestplusfeaturetests.v4;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


/**
 * Created by Max on 26.09.2015.
 */

public class TodoMVCTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testTasksLifeCycle(){
        createTasks("1");
        assertItemsLeftCounter(1);

        toggle("1");
        assertTasksAre("1");

        goToActive();
        assertNoVisisbleTasks();

        createTasks("2", "3");
        assertItemsLeftCounter(2);
        assertVisibleTasksAre("2", "3");

        editTask("2", "2 is edited");
        deleteTask("2 is edited");
        startEdit("3", "cancel 3").sendKeys(Keys.ESCAPE);
        assertVisibleTasksAre("3");

        goToCompleted();
        assertVisibleTasksAre("1");

        toggle("1");
        assertNoVisisbleTasks();

        goToActive();
        assertVisibleTasksAre("1", "3");

        toggleAll();
        assertNoVisisbleTasks();
        assertItemsLeftCounter(0);

        goToAll();
        assertTasksAre("1", "3");

        clearCompleted();
        assertNoTasks();
        footer.shouldBe(hidden);

    }

    @Test
    public void testSaveWithEmptyNameOnAll(){
        createTasks("1");
        editTask("1", "");
        assertNoVisisbleTasks();
    }

    @Test
    public void testActivateAll(){
        createTasks("1", "2");
        toggleAll();
        goToActive();
        assertNoVisisbleTasks();
        toggleAll();
        assertVisibleTasksAre("1", "2");
    }

    @Test
    public void testSaveByClickOnOtherTask(){
        createTasks("1", "2");
        assertTasksAre("1", "2");
        startEdit("1", "1 is edited");
        tasks.find(exactText("2")).click();
        assertTasksAre("1 is edited", "2");

    }

    private void clearCompleted(){
        clearButton.click();
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

        startEdit(oldName, newName).pressEnter();
    }

    private SelenideElement startEdit(String oldName, String newName){
        tasks.find(exactText(oldName)).find("label").doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newName);
    }

    private void goToAll(){
        $("[href='#/']").click();
    }

    private void goToActive(){
        $("[href='#/active']").click();
    }

    private void goToCompleted(){
        $("[href='#/completed']").click();
    }

    private void assertTasksAre(String... texts){
        tasks.shouldHave(exactTexts(texts));
    }

    private void assertVisibleTasksAre(String... texts){
        tasks.filter(visible).shouldHave(exactTexts(texts));
    }

    private void assertItemsLeftCounter(int counterValue){
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(counterValue)));
    }

    private void assertNoTasks(){
        tasks.shouldBe(empty);
    }

    private void assertNoVisisbleTasks(){
        tasks.filter(visible).shouldBe(empty);
    }

    ElementsCollection tasks = $$("#todo-list > li");
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement footer = $("#footer");

}
