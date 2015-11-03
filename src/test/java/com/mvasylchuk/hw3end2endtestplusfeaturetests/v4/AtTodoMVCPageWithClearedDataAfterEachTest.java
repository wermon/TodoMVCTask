package com.mvasylchuk.hw3end2endtestplusfeaturetests.v4;

import org.junit.After;
import org.junit.Before;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Created by Max on 19.10.2015.
 */
public class  AtTodoMVCPageWithClearedDataAfterEachTest extends BaseTest {

    @Before
    public void openToMVCPage(){
        open("http://todomvc.com/examples/troopjs_require/#/");
        getWebDriver().navigate().refresh();
    }

    @After
    public void clearData(){
        executeJavaScript("localStorage.clear()");
    }
}
