package com.mvasylchuk.hw3end2endtestplusfeaturetests.v3;

import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.junit.After;
import ru.yandex.qatools.allure.annotations.Attachment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Max on 19.10.2015.
 */
public class BaseTest {
    @After
    public void PostScreenshot() throws IOException {
        screenshot();
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.takeScreenShotAsFile();
        return Files.toByteArray(screenshot);
    }

}
