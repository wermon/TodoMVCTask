package com.mvasylchuk.hw5.v6;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.mvasylchuk.hw5.v6.TodoMVCTest.TaskFactory.aTask;
import static com.mvasylchuk.hw5.v6.TodoMVCTest.TaskType.*;


/**
 * Created by Max on 26.09.2015.
 */


public class TodoMVCTest extends BaseTest {

    @Test
    public void testTasksLifeCycle() {
        givenEmptyTodoMVCPage();
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

    //All Filter
    @Test
    public void testCreateTaskOnAll() {
        givenEmptyTodoMVCPage();
        createTasks("1");
        assertItemsLeftCounter(1);
        assertTasksAre("1");
    }

    @Test
    public void testEditTaskOnAll() {
        given("1");
        editTask("1", "2");
        assertItemsLeftCounter(1);
        assertTasksAre("2");
    }

    @Test
    public void testSaveByClickOnOtherTaskOnAll() {
        given(
                aTask("1", ACTIVE),
                aTask("2", ACTIVE)
        );
        assertTasksAre("1", "2");
        startEdit("1", "1 is edited");
        tasks.find(exactText("2")).click();
        assertTasksAre("1 is edited", "2");

    }

    @Test
    public void testCancelEditWithEscOnAll() {
        given("1");
        startEdit("1", "2").sendKeys(Keys.ESCAPE);
        assertItemsLeftCounter(1);
        assertTasksAre("1");
    }

    @Test
    public void testSaveWithEmptyNameOnAllFilter() {
        given(
                "1"
        );
        editTask("1", "");
        assertNoVisibleTasks();
    }

    @Test
    public void testDeleteOnAll() {
        given("1");
        deleteTask("1");
        assertNoTasks();
        assertItemsLeftCounter(0);
    }

    @Test
    public void testCompleteOnAll() {
        given("1");
        toggle("1");
        assertTasksAre("1");
        assertItemsLeftCounter(0);
    }

    @Test
    public void testCompleteAllOnAll() {
        given("1", "2");
        toggleAll();
        assertTasksAre("1", "2");
        assertItemsLeftCounter(0);
    }

    @Test
    public void testActivateOnAll() {
        given(COMPLETED, "1");
        toggle("1");
        assertItemsLeftCounter(1);
    }

    @Test
    public void testActivateAllOnAll() {
        given(
                aTask("1", COMPLETED),
                aTask("2", COMPLETED)
        );

        toggleAll();
        goToActive();
        assertTasksAre("1", "2");
    }

    @Test
    public void testClearCompletedOnAll() {
        given(
                aTask("1", COMPLETED),
                aTask("2", COMPLETED)
        );
        clearCompleted();
        footer.shouldBe(hidden);
        assertNoTasks();
    }

    //Active Filter
    @Test
    public void testCreateTaskOnActive() {
        givenEmptyTodoMVCPage();
        goToActive();
        createTasks("1");
        assertItemsLeftCounter(1);
        assertVisibleTasksAre("1");
    }

    @Test
    public void testEditTaskOnActive() {
        given("1");
        goToActive();
        editTask("1", "2");
        assertItemsLeftCounter(1);
        assertVisibleTasksAre("2");
    }

    @Test
    public void testCancelEditWithEscOnActive() {
        given("1");
        goToActive();
        startEdit("1", "2").sendKeys(Keys.ESCAPE);
        assertItemsLeftCounter(1);
        assertVisibleTasksAre("1");
    }

    @Test
    public void testDeleteOnActive() {
        given("1");
        goToActive();
        deleteTask("1");
        assertNoVisibleTasks();
        footer.shouldBe(hidden);
    }

    @Test
    public void testCompleteOnActive() {
        given("1");
        goToActive();
        toggle("1");
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
    public void testCancelEditWithEscOnCompleted() {
        given(COMPLETED, "1");
        goToCompleted();
        startEdit("1", "2").sendKeys(Keys.ESCAPE);
        assertItemsLeftCounter(0);
        assertVisibleTasksAre("1");
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
        givenEmptyTodoMVCPage();
        StringBuilder jsStringBuilder = new StringBuilder("localStorage.setItem(\"todos-troopjs\", \"[");
        int i = 0;
        for (Task task : tasks) {
            String taskData = task.toString();
            if (i < tasks.length - 1) {
                jsStringBuilder.append(taskData).append(", ");
            } else if (i == tasks.length - 1) {
                jsStringBuilder.append(taskData);
            }
            i++;
        }
        jsStringBuilder.append("]\")");

        executeJavaScript(jsStringBuilder.toString());
        getWebDriver().navigate().refresh();
    }

    private void given(String... texts) {
        Task[] tasks = new Task[texts.length];
        int i = 0;
        for (String text : texts) {
            tasks[i] = (aTask(text, TaskType.ACTIVE));
            i++;
        }
        given(tasks);
    }

    private void given(TaskType taskType, String... texts) {
        Task[] tasks = new Task[texts.length];
        int i = 0;
        for (String text : texts) {
            tasks[i] = (aTask(text, taskType));
            i++;
        }
        given(tasks);
    }

    private void givenEmptyTodoMVCPage() {
        smartOpenTodoMVCPage();
        if (tasks.size() > 0) {
            executeJavaScript("localStorage.clear()");
            getWebDriver().navigate().refresh();
        }
    }

    private void smartOpenTodoMVCPage() {
        if ($("#new-todo").is(not(visible))) {
            open("http://todomvc.com/examples/troopjs_require/#/");
            getWebDriver().navigate().refresh();
        }
    }


    ElementsCollection tasks = $$("#todo-list > li");
    SelenideElement clearButton = $("#clear-completed");
    SelenideElement footer = $("#footer");

    public enum TaskType {
        ACTIVE("false"),
        COMPLETED("true");

        private String completedStatus;

        TaskType(String completedStatus) {
            this.completedStatus = completedStatus;
        }
    }


    static class Task {
        String text;
        String status;

        Task(String text, TaskType taskType) {

            this.status = taskType.completedStatus;
            this.text = text;
        }

        public String toString() {

            return String.format("{\\\"completed\\\":%1$s, \\\"title\\\":\\\"%2$s\\\"}", status, this.text);
        }
    }

    static class TaskFactory {
        static Task aTask(String text, TaskType taskType) {
            return new Task(text, taskType);
        }
    }

}
