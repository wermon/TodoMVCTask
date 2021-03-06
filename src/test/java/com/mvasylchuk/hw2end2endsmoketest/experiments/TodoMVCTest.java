package com.mvasylchuk.hw2end2endsmoketest.experiments;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
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

public class TodoMVCTest {


    @Before
    public void OpenToMVCPage() {
        open("http://todomvc.com/examples/troopjs_require/#/");
        getWebDriver().navigate().refresh();
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
    }

    @Test
    public void testTasksE2E() {
        createTasks("1");
        goToActive();
        createTasks("2");
        toggle("2");
        assertVisibleTasksAre("1", "2");

        sleep(10000);
    }


    private void clearCompleted() {
        $("#clear-completed").click();
        clearButton.shouldBe(hidden);
    }

    private void createTasks(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").val(text).pressEnter();
        }
    }

    private void deleteTask(String taskText) {
        tasks.find(exactText(taskText)).hover().find(".destroy").click();
    }

    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void editTask(String oldName, String newName) {

        startEdit(oldName, newName).setValue(newName).pressEnter();
    }

    private SelenideElement startEdit(String oldName, String newName) {
        tasks.find(exactText(oldName)).find("label").doubleClick();
        return tasks.find(cssClass("editing")).find(".edit").setValue(newName);
    }

    private void goToAll() {
        $("[href='#/']").click();
    }

    private void goToActive() {
        $("[href='#/active']").click();
    }

    private void goToCompleted() {
        $("[href='#/completed']").click();
    }

    private void assertTasksAre(String... texts) {
        tasks.shouldHave(exactTexts(texts));

    }

    private void assertVisibleTasksAre(String... texts) {
        ElementsCollection visibleTasks = tasks.filter(visible);
        sleep(1000);
        visibleTasks.shouldHave(exactTexts(texts));
    }

    private void assertItemsLeftCounter(int counterValue) {
        $("#todo-count > strong").shouldHave(exactText(Integer.toString(counterValue)));
    }

    private void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    private void assertNoVisisbleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    ElementsCollection tasks = $$("#todo-list > li");
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement footer = $("#footer");


}
