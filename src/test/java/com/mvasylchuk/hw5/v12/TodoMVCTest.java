package com.mvasylchuk.hw5.v12;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static com.mvasylchuk.hw5.v12.TodoMVCTest.TaskStatus.ACTIVE;
import static com.mvasylchuk.hw5.v12.TodoMVCTest.TaskStatus.COMPLETED;


/**
 * Created by Max on 26.09.2015.
 */


public class TodoMVCTest extends BaseTest {

    {
        Configuration.timeout = 10000;
    }

    @Test
    public void testTasksLifeCycle() {
        givenAtAll();
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
        delete("1 is edited");
        assertNoTasks();
        $(byText(""));

    }

    //All Filter
    @Test
    public void testCreateTaskOnAll() {
        givenAtAll();

        createTasks("1");
        assertItemsLeftCounter(1);
        assertTasks("1");
    }

    @Test
    public void testEditTaskOnAll() {
        givenAtAll(ACTIVE, "1");

        editTask("1", "2");
        assertItemsLeftCounter(1);
        assertTasks("2");
    }

    @Test
    public void testEditByClickOutsideOnAll() {
        givenAtAll(ACTIVE, "1", "2");

        assertTasks("1", "2");
        startEdit("1", "1 is edited");
        clickOnTask("2");
        assertTasks("1 is edited", "2");
        assertItemsLeftCounter(2);

    }

    @Test
    public void testCancelEditWithEscOnAll() {
        givenAtAll(ACTIVE, "1");

        startEdit("1", "2").pressEscape();

        assertItemsLeftCounter(1);
        assertTasks("1");
    }

    @Test
    public void testDeleteWithEditToEmptyNameOnAll() {
        givenAtAll(ACTIVE, "1");

        editTask("1", "");
        assertNoVisibleTasks();
    }

    @Test
    public void testDeleteOnAll() {
        givenAtAll(ACTIVE, "1");

        delete("1");
        assertNoTasks();
        footer.shouldBe(hidden);
    }

    @Test
    public void testCompleteOnAll() {
        givenAtAll(ACTIVE, "1");

        toggle("1");
        assertTasks("1");
        assertItemsLeftCounter(0);
        clearButton.shouldBe(visible);
    }

    @Test
    public void testActivateOnAll() {
        givenAtAll(
                aTask("1", COMPLETED),
                aTask("2", ACTIVE)
        );

        toggle("1");
        assertItemsLeftCounter(2);
        assertTasks("1", "2");
    }

    @Test
    public void testActivateAllOnAllAndTransitionToCompleted() {
        givenAtAll(COMPLETED, "1", "2");

        toggleAll();
        assertItemsLeftCounter(2);
        assertTasks("1", "2");
        goToCompleted();
        assertNoVisibleTasks();
        assertItemsLeftCounter(2);
    }

    @Test
    public void testClearCompletedOnAll() {
        givenAtAll(COMPLETED, "1", "2");

        clearCompleted();
        footer.shouldBe(hidden);
        assertNoTasks();
    }

    //Active Filter
    @Test
    public void testCreateTaskOnActive() {
        givenAtActive(ACTIVE, "1");

        createTasks("2");
        assertItemsLeftCounter(2);
        assertVisibleTasks("1", "2");
    }

    @Test
    public void testEditTaskOnActive() {
        givenAtActive(ACTIVE, "1", "2");

        editTask("2", "edited 2");
        assertItemsLeftCounter(2);
        assertVisibleTasks("1", "edited 2");
    }

    @Test
    public void testDeleteOnActive() {
        givenAtActive(ACTIVE, "1", "2");

        delete("2");
        assertItemsLeftCounter(1);
        assertVisibleTasks("1");
    }

    @Test
    public void testCompleteOnActive() {
        givenAtActive(ACTIVE, "1");

        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeftCounter(0);
    }

    @Test
    public void testCompleteAllOnActive() {
        givenAtActive(ACTIVE, "1", "2");

        toggleAll();
        assertNoVisibleTasks();
        assertItemsLeftCounter(0);
    }

    //Completed filter
    @Test
    public void testEditTaskOnCompleted() {
        givenAtCompleted(COMPLETED, "1");

        editTask("1", "2");
        assertItemsLeftCounter(0);
        assertVisibleTasks("2");
    }

    @Test
    public void testDeleteOnCompleted() {
        givenAtCompleted(COMPLETED, "1");

        delete("1");
        assertNoVisibleTasks();
        footer.shouldBe(hidden);
    }

    @Test
    public void testActivateOnCompleted() {
        givenAtCompleted(COMPLETED, "1");

        toggle("1");
        assertNoVisibleTasks();
        assertItemsLeftCounter(1);
        goToAll();
        assertTasks("1");
        assertItemsLeftCounter(1);
    }

    @Test
    public void testClearCompletedOnCompleted() {
        givenAtCompleted(
                aTask("1", COMPLETED),
                aTask("2", COMPLETED),
                aTask("3", ACTIVE)
        );

        clearCompleted();
        assertItemsLeftCounter(1);
        assertNoVisibleTasks();
    }

    @Test
    public void testActivateAllOnCompleted() {
        givenAtCompleted(COMPLETED, "1", "2");

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

    private void delete(String taskText) {
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

    private SelenideElement clickOnTask(String Name) {
        SelenideElement task = tasks.find(exactText(Name)).find("label");
        task.click();
        return task;
    }

    private void goToAll() {
        $(By.linkText("All")).click();
    }

    private void goToActive() {
        $("[href='#/active']").click();
    }

    private void goToCompleted() {
        $("[href='#/completed']").click();
    }

    private void assertTasks(String... texts) {
        tasks.shouldHave(exactTexts(texts));
    }

    private void assertVisibleTasks(String... texts) {
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

    private void given(Filter filter, Task... tasks) {

        ensureOpenedTodoMVCPage(filter);
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

        executeJavaScript("location.reload()");
    }

    private void given(TaskStatus taskStatus, Filter filter, String... texts) {
        Task[] tasks = new Task[texts.length];
        for (int i = 0; i < texts.length; i++) {
            tasks[i] = (aTask(texts[i], taskStatus));

        }
        given(filter, tasks);
    }

    private void givenAtActive(Task... tasks) {
        given(Filter.ACTIVE, tasks);
    }

    private void givenAtActive(TaskStatus taskStatus, String... texts) {
        given(taskStatus, Filter.ACTIVE, texts);
    }

    private void givenAtCompleted(Task... tasks) {
        given(Filter.COMPLETED, tasks);

    }

    private void givenAtCompleted(TaskStatus taskStatus, String... texts) {
        given(taskStatus, Filter.COMPLETED, texts);
    }

    private void givenAtAll(Task... tasks) {
        given(Filter.All, tasks);
    }

    private void givenAtAll(TaskStatus taskStatus, String... texts) {
        given(taskStatus, Filter.All, texts);
    }

    private void ensureOpenedTodoMVCPage(Filter filter) {
        if (!url().equals(filter.getUrl())) {
            open(filter.getUrl());
        }
    }

    public enum TaskStatus {
        ACTIVE,
        COMPLETED;

    }

    public enum Filter {
        All("/#/"),
        ACTIVE("/#/active"),
        COMPLETED("/#/completed");

        private String toDoMVCPageUrl = "https://todomvc4tasj.herokuapp.com";
        private String subUrl;

        Filter(String subUrl) {
            this.subUrl = subUrl;
        }

        public String getUrl() {
            return toDoMVCPageUrl + subUrl;
        }

    }

    static class Task {
        String text;
        TaskStatus taskStatus;

        Task(String text, TaskStatus taskStatus) {

            this.taskStatus = taskStatus;
            this.text = text;
        }

        public String toString() {

            return String.format("{\\\"completed\\\":%1$s, \\\"title\\\":\\\"%2$s\\\"}", taskStatus == TaskStatus.COMPLETED ? "true" : "false", this.text);
        }
    }

    static Task aTask(String text, TaskStatus taskStatus) {
        return new Task(text, taskStatus);
    }


}
