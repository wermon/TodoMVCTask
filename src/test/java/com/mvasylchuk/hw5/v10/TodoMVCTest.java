package com.mvasylchuk.hw5.v10;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.mvasylchuk.hw5.v10.TodoMVCTest.TaskType.ACTIVE;
import static com.mvasylchuk.hw5.v10.TodoMVCTest.TaskType.COMPLETED;


/**
 * Created by Max on 26.09.2015.
 */


public class TodoMVCTest extends BaseTest {

    @Test
    public void testTasksLifeCycle() {
        givenEmptyTodoMVCPage();
        createTasks("1");
        toggle("1");

        goToActive();
        assertNoVisibleTasks();

        goToCompleted();
        editTask("1", "1 is edited");

        toggle("1 is edited");
        assertNoVisibleTasks();

        goToActive();
        toggle("1 is edited");
        assertNoVisibleTasks();

        goToAll();
        deleteTask("1 is edited");
        assertNoTasks();

    }

    //All Filter
    @Test
    public void testCreateTaskOnAll() {
        givenEmptyTodoMVCPage();
        createTasks("1", "2", "3");
        assertItemsLeftCounter(3);
        assertTasksAre("1", "2", "3");
    }

    @Test
    public void testEditTaskOnAll() {
        givenActive("1");
        editTask("1", "2");
        assertItemsLeftCounter(1);
        assertTasksAre("2");
    }

    @Test
    public void testSaveByClickOnOtherTaskOnAll() {
        givenActive("1", "2");
        assertTasksAre("1", "2");
        startEdit("1", "1 is edited");
        tasks.find(exactText("2")).click();
        assertTasksAre("1 is edited", "2");

    }

    @Test
    public void testCancelEditWithEscOnAll() {
        givenActive("1");
        startEdit("1", "2").sendKeys(Keys.ESCAPE);
        assertItemsLeftCounter(1);
        assertTasksAre("1");
    }

    @Test
    public void testSaveWithEmptyNameOnAllFilter() {
        givenActive("1");
        editTask("1", "");
        assertNoVisibleTasks();
    }

    @Test
    public void testDeleteOnAll() {
        givenActive("1");
        deleteTask("1");
        assertNoTasks();
        footer.shouldBe(hidden);
    }

    @Test
    public void testCompleteOnAll() {
        givenActive("1");
        toggle("1");
        assertTasksAre("1");
        assertItemsLeftCounter(0);
        clearButton.shouldBe(visible);
    }

    @Test
    public void testActivateOnAll() {
        given(COMPLETED, "1");
        toggle("1");
        assertItemsLeftCounter(1);
    }

    @Test
    public void testActivateAllOnAll() {
        given(COMPLETED, "1", "2");
        toggleAll();
        goToActive();
        assertTasksAre("1", "2");
    }

    @Test
    public void testClearCompletedOnAll() {
        given(COMPLETED, "1", "2");
        clearCompleted();
        footer.shouldBe(hidden);
        assertNoTasks();
    }

    //Active Filter
    @Test
    public void testCreateTaskOnActive() {
        givenEmptyTodoMVCPage();
        givenActive("1");
        goToActive();
        createTasks("2");
        assertItemsLeftCounter(2);
        assertVisibleTasksAre("1", "2");
    }

    @Test
    public void testEditTaskOnActive() {
        givenActive("1");
        goToActive();
        editTask("1", "2");
        assertItemsLeftCounter(1);
        assertVisibleTasksAre("2");
    }

    @Test
    public void testDeleteOnActive() {
        givenActive("1");
        goToActive();
        deleteTask("1");
        assertNoVisibleTasks();
        footer.shouldBe(hidden);
    }

    @Test
    public void testCompleteOnActive() {
        givenActive("1");
        goToActive();
        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeftCounter(0);
    }

    @Test
    public void testCompleteAllOnActive() {
        givenActive("1", "2");
        goToActive();
        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeftCounter(0);
    }

    //Completed filter
    @Test
    public void testEditTaskOnCompleted() {
        given(COMPLETED, "1");
        goToCompleted();
        editTask("1", "2");
        assertItemsLeftCounter(0);
        assertVisibleTasksAre("2");
    }

    @Test
    public void testDeleteOnCompleted() {
        given(COMPLETED, "1");
        goToCompleted();
        deleteTask("1");
        assertNoVisibleTasks();
        footer.shouldBe(hidden);
    }

    @Test
    public void testActivateOnCompleted() {
        given(COMPLETED, "1");
        goToCompleted();
        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeftCounter(1);
    }

    @Test
    public void testClearCompletedOnCompleted() {
        given(
                aTask("1", COMPLETED),
                aTask("2", COMPLETED),
                aTask("3", ACTIVE)
        );
        goToCompleted();
        clearCompleted();
        assertItemsLeftCounter(1);
        assertNoVisibleTasks();
    }

    @Test
    public void testActivateAllOnCompleted() {
        given(COMPLETED, "1", "2");
        goToCompleted();
        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeftCounter(2);
    }

    SelenideElement footer = $("#footer");
    SelenideElement clearButton = $("#clear-completed");
    ElementsCollection tasks = $$("#todo-list > li");

    private void clearCompleted() {
        clearButton.click();
        clearButton.shouldBe(hidden);
    }

    private void createTasks(String... taskTexts) {
        for (String text : taskTexts) {
            $("#new-todo").shouldBe(enabled).val(text).pressEnter();
        }
    }

    private void deleteTask(String taskText) {
        tasks.find(exactText(taskText)).hover().find(".destroy").shouldBe(enabled).click();
    }

    private void toggle(String taskText) {
        tasks.find(exactText(taskText)).find(".toggle").click();
    }

    private void toggleAll() {
        $("#toggle-all").click();
    }

    private void editTask(String oldName, String newName) {

        startEdit(oldName, newName).pressEnter();
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
        tasks.filter(visible).shouldHave(exactTexts(texts));
    }

    private void assertItemsLeftCounter(int counterValue) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(counterValue)));
    }

    private void assertNoTasks() {
        tasks.shouldBe(empty);
    }

    private void assertNoVisibleTasks() {
        tasks.filter(visible).shouldBe(empty);
    }

    private void given(Task... tasks) {

        ensureOpenedTodoMVCPage();
        StringBuilder jsStringBuilder = new StringBuilder("localStorage.setItem(\"todos-troopjs\", \"[");

        for (int i = 0; i < tasks.length; i++) {
            String taskData = tasks[i].toString();

            jsStringBuilder.append(taskData);
            if (i < tasks.length - 1) {
                jsStringBuilder.append(",");
            }
        }
        jsStringBuilder.append("]\")");

        executeJavaScript(jsStringBuilder.toString());
        getWebDriver().navigate().refresh();
    }

    private void givenActive(String... texts) {
        given(TaskType.ACTIVE, texts);
    }

    private void given(TaskType taskType, String... texts) {
        Task[] tasks = new Task[texts.length];
        for (int i = 0; i < texts.length; i++) {
            tasks[i] = (aTask(texts[i], taskType));

        }
        given(tasks);
    }

    String toDoMVCPageUrl = "https://todomvc4tasj.herokuapp.com";

    private void givenEmptyTodoMVCPage() {
        given();
    }

    private void ensureOpenedTodoMVCPage() {

        if (!getWebDriver().getCurrentUrl().equals(toDoMVCPageUrl)) {
            open(toDoMVCPageUrl);
        }

    }

    public enum TaskType {
        ACTIVE,
        COMPLETED;

    }

    static class Task {
        String text;
        TaskType status;

        Task(String text, TaskType taskType) {

            this.status = taskType;
            this.text = text;
        }

        public String toString() {

            return String.format("{\\\"completed\\\":%1$s, \\\"title\\\":\\\"%2$s\\\"}", status == TaskType.COMPLETED ? "true" : "false", this.text);
        }
    }

    static Task aTask(String text, TaskType taskType) {
        return new Task(text, taskType);
    }


}
