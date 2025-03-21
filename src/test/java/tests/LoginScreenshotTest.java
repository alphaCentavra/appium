package tests;

import base.BaseTest;
import com.github.romankh3.image.comparison.model.ImageComparisonResult;
import com.github.romankh3.image.comparison.model.ImageComparisonState;
import config.ConfigReader;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

/*
Описание дефектов находится в файле test -> resources -> docs -> AppDefects.xls
Список тест кейсов находится в файле test -> resources -> docs -> TestCases.doc
*/
public class LoginScreenshotTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger();
    private String actualScreensDir = ConfigReader.testDataConfig.folderWithActualScreenshots();

    private String passwordNotShownExpectedValue = ConfigReader.testDataConfig.passwordNotShownExpectedValue();

    private String passwordNotShownActualValue = ConfigReader.testDataConfig.passwordNotShownActualValue();

    private String imageComparisonResultNotShownValue = ConfigReader.testDataConfig.imageComparisonResultNotShownValue();

    private String passwordIsShownExpectedValue = ConfigReader.testDataConfig.passwordIsShownExpectedValue();

    private String passwordIsShownActualValue = ConfigReader.testDataConfig.passwordIsShownActualValue();

    private String imageComparisonResultIsShownValue = ConfigReader.testDataConfig.imageComparisonResultIsShownValue();

    @Test
    @AllureId("3")
    @Description("Проверяем, что пароль может быть как скрыт, так и отображен")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("Дефект №4 - Иконка показа/скрытия пароля имеет неверную анимацию")
    public void testPasswordCouldBeHiddenAndShown() {
        // тестовые данные
        String passwordFieldText = ConfigReader.testDataConfig.validPassword();
        String trueValue = ConfigReader.testDataConfig.yes();
        String falseValue = ConfigReader.testDataConfig.no();

        logger.info("ШАГ 1 - вводим текст в поле для ввода пароля");
        loginPage
                .waitPageTitleDisplayed()
                .enterPassword(passwordFieldText)
                .clickLoginButton()
                .waitPageTitleDisplayed();
        logger.info("ШАГ 2 - проверяем на скриншоте, что пароль не показывается и что иконка находится в статусе checked");
        ImageComparisonResult imageComparisonResultNotShown = loginPage
                .savePasswordFieldScreenshot(actualScreensDir, passwordNotShownActualValue)
                .compareScreenshots(passwordNotShownActualValue, passwordNotShownExpectedValue, imageComparisonResultNotShownValue);
        assertEquals(ImageComparisonState.MATCH, imageComparisonResultNotShown.getImageComparisonState());
        assertEquals(loginPage.getPasswordFieldAttribute(), trueValue, "Поле 'Пароль' не предназначено для ввода пароля");
        assertEquals(loginPage.getShowPasswordIconCheckedState(), trueValue, "По умолчанию иконка должна отображаться в перечеркнутом виде"); // в этой строчке воспроизводится дефект №4
        logger.info("ШАГ 3 - кликаем один раз на иконку показа/скрытия пароля");
        loginPage
                .clickShowPasswordIcon(1)
                .clickLoginButton()
                .waitPageTitleDisplayed();
        logger.info("ШАГ 4 - проверяем на скриншоте, что пароль показывается и что иконка не находится в статусе checked");
        ImageComparisonResult imageComparisonResultIsShown = loginPage
                .savePasswordFieldScreenshot(actualScreensDir, passwordIsShownActualValue)
                .compareScreenshots(passwordIsShownActualValue, passwordIsShownExpectedValue, imageComparisonResultIsShownValue);
        assertEquals(ImageComparisonState.MATCH, imageComparisonResultIsShown.getImageComparisonState());
        assertEquals(loginPage.getShowPasswordIconCheckedState(), falseValue, "При одиночном клике иконка должна менять свое состояние на противоположное"); // в этой строчке воспроизводится дефект №4
        logger.info("ШАГ 5 - кликаем два раза на иконку показа/скрытия пароля");
        loginPage
                .clickShowPasswordIcon(2)
                .clickLoginButton()
                .waitPageTitleDisplayed();
        logger.info("ШАГ 6 - проверяем на скриншоте, что пароль по-прежнему показывается и что иконка не в статусе checked");
        imageComparisonResultIsShown = loginPage
                .savePasswordFieldScreenshot(actualScreensDir, passwordIsShownActualValue)
                .compareScreenshots(passwordIsShownActualValue, passwordIsShownExpectedValue, imageComparisonResultIsShownValue);
        assertEquals(ImageComparisonState.MATCH, imageComparisonResultIsShown.getImageComparisonState());
        assertEquals(loginPage.getShowPasswordIconCheckedState(), falseValue, "При двойном клике иконка не должна менять свое состояние"); // в этой строчке воспроизводится дефект №4
    }
}
