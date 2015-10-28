package com.mvasylchuk.hw5.v2;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;


/**
 * Created by Max on 26.09.2015.
 */

public class TodoMVCTest extends AtTodoMVCPageWithClearedDataAfterEachTest {

    @Test
    public void testTasksE2E(){
        createTasks("1");
        assertItemsLeftCounter(1);

        toggle("1");
        assertTasksAre("1");

        goToActive();
        assertNoVisibleTasks();

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
        assertNoVisibleTasks();

        goToActive();
        assertVisibleTasksAre("1", "3");

        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeftCounter(0);

        goToAll();
        assertTasksAre("1", "3");

        clearCompleted();
        assertNoTasks();
        footer.shouldBe(hidden);

    }

    @Test
    public void SaveWithEmptyName(){
        createTasks("1");
        editTask("1", "");
        assertNoVisibleTasks();
    }

    @Test
    public void ActivateAll(){
        createTasks("1", "2");
        toggleAll();
        toggleAll();
        goToActive();
        assertVisibleTasksAre("1", "2");
    }

    @Test
    public void SaveByClickOnOtherTask(){
        given(TaskType.ACTIVE, "1", "2");
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

    private void assertNoVisibleTasks(){
        tasks.filter(visible).shouldBe(empty);
    }

    private void given(TaskType taskType, String...texts){
        StringBuilder jsStringBuilder = new StringBuilder("localStorage.setItem(\"todos-troopjs\", \"[");

        String status = "";
        if (taskType == TaskType.ACTIVE){
           status = "false";
        }
        else if (taskType == TaskType.COMPLETED){
            status = "true";
        }

        int i= 0;
        for (String text : texts) {
            String js = String.format("{\\\"completed\\\":%1$s, \\\"title\\\":\\\"%2$s\\\"}, ", status, text);

            if (i < texts.length - 1){
                jsStringBuilder.append(js);
            }
            else if (i == texts.length - 1){
                js = String.format("{\\\"completed\\\":%1$s, \\\"title\\\":\\\"%2$s\\\"}", status, text);
                jsStringBuilder.append(js);
            }
            i++;
        }

        jsStringBuilder.append("]\")");
        executeJavaScript(jsStringBuilder.toString());
        getWebDriver().navigate().refresh();
    }

    ElementsCollection tasks = $$("#todo-list > li");
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement footer = $("#footer");

    public enum TaskType{
        ACTIVE,
        COMPLETED
    }
}
