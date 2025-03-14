package tests;

import base.BaseTest;
import config.ConfigReader;
import io.qameta.allure.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/*
Описание дефектов находится в файле test -> resources -> docs -> AppDefects.xls
Список тест кейсов находится в файле test -> resources -> docs -> TestCases.doc
*/
public class LoginTest extends BaseTest {

    private static final Logger logger = LogManager.getLogger();

    @Test
    @AllureId("1")
    @Description("Проверяем, что основные элементы страницы отображены на странице и имеют необходимые характеристики")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("Дефект №3 - Кнопка входа имеет неверное название")
    public void testUserMainLoginPageElements() {
        // тестовые данные
        String expectedLoginPageTitle = "Вход в Alfa-Test";
        String loginFieldText = "Логин";
        String loginButtonName = "Войти";
        String passwordFieldText = "Пароль";
        String trueValue = ConfigReader.testDataConfig.yes();
        String falseValue = ConfigReader.testDataConfig.no();
        String actualLoginPageTitle = "";

        logger.info("ШАГ 1 - проверяем видимость и недоступность заголовка, а также его отображаемый текст");
        loginPage.waitPageTitleDisplayed();
        assertEquals(loginPage.isPageTitleFocusable(), falseValue, "Заголовку страницы можно установить фокус");
        actualLoginPageTitle = loginPage.getPageTitle();
        assertEquals(actualLoginPageTitle, expectedLoginPageTitle,
                "Заголовок '" + actualLoginPageTitle + "' на странице логина не равен " + expectedLoginPageTitle);

        logger.info("ШАГ 2 - проверяем видимость и доступность поля 'Логин', то что это текстовое поле, которое имеет лейбл 'Логин'");
        assertTrue(loginPage.isLoginFieldDisplayed(), "Поле 'Логин' не отображается на странице авторизации");
        assertEquals(loginPage.isLoginFieldFocusable(), trueValue, "В поле 'Логин' страницы авторизации нельзя установить фокус");
        assertEquals(loginPage.isLoginFieldClickable(), trueValue, "Поле 'Логин' страницы авторизации не является кликабельным");
        assertEquals(loginPage.getLoginFieldText(), loginFieldText, "Текст поля 'Логин' страницы авторизации не равен '" + loginFieldText + "'");

        logger.info("ШАГ 3 - проверяем видимость и доступность поля 'Пароль', то что это текстовое поле, которое имеет лейбл 'Пароль'");
        assertTrue(loginPage.isPasswordFieldDisplayed(), "Поле 'Пароль' не отображается на странице авторизации");
        assertEquals(loginPage.isPasswordFieldFocusable(), trueValue, "В поле 'Пароль' страницы авторизации нельзя установить фокус");
        assertEquals(loginPage.isPasswordFieldClickable(), trueValue, "Поле 'Пароль' страницы авторизации не является кликабельным");
        assertEquals(loginPage.getPasswordFieldText(), passwordFieldText, "Текст поля 'Пароль' страницы авторизации не равен '" + passwordFieldText + "'");

        logger.info("ШАГ 4 - проверяем видимость и доступность кнопки 'Войти' и её название");
        assertTrue(loginPage.isLoginButtonDisplayed(), "Кнопка 'Войти' не отображается на странице авторизации");
        assertEquals(loginPage.isLoginButtonFocusable(), trueValue, "На кнопку 'Войти' страницы авторизации нельзя установить фокус");
        assertEquals(loginPage.isLoginButtonClickable(), trueValue, "Кнопка 'Войти' страницы авторизации не кликабельна");
        assertEquals(loginPage.getLoginButtonText(), loginButtonName, "Название кнопки логина не равно '" + loginButtonName + "'");
    }

    @Test //тест дописан до попытки логина с невалидным логином и паролем
    @AllureId("2")
    @Description("Проверяем, что в приложения возможно залогиниться с валидными данными и невозможно с невалидными")
    @Severity(SeverityLevel.CRITICAL)
    @Issue("Дефект №1 - Не отображается валидационное сообщение поля 'Логин'")
    @Issue("Дефект №2 - Не отображается валидационное сообщение поля 'Пароль'")
    public void testUserLogin() {
        // тестовые данные
        String invalidValueValidationMessage = ConfigReader.testDataConfig.invalidValueValidationMessage();
        String validLoginValue = ConfigReader.testDataConfig.validLogin();
        String validPasswordValue = ConfigReader.testDataConfig.validPassword();
        String invalidLoginValue = ConfigReader.testDataConfig.invalidLogin();
        String invalidPasswordValue = ConfigReader.testDataConfig.invalidPassword();
        String expectedHomePageContent = "Вход в Alfa-Test выполнен";
        String actualPageContent = "";

        logger.info("ШАГ 1 - проверяем, что пользователь не может залогиниться с невалидными логином и паролем");
        loginPage
                .enterUsername(invalidLoginValue)
                .enterPassword(invalidPasswordValue)
                .clickLoginButton();
        // после того, как будут исправлены дефект №1 и дефект №2, здесь будут добавлены проверки на то, что были введены некорректные данные
        // usernameValidationMessage = loginPage.getUsernameValidationMessage();
        // assertEquals(usernameValidationMessage, invalidValueValidationMessage);
        // String passwordValidationMessage = loginPage.getPasswordValidationMessage();
        // assertFalse(passwordValidationMessage.isEmpty(), "Не осуществляется проверка на то, чтобы длина пароля не превышала максимальную");
        assertTrue(loginPage.isPageTitleDisplayed(), "Не отображается заголовок страницы логина");
        logger.info("ШАГ 2 - проверяем, что пользователь может залогиниться с валидным логином и паролем");
        loginPage
                .enterUsername(validLoginValue)
                .enterPassword(validPasswordValue)
                .clickLoginButton()
                .waitPageTitleNotVisible();
        // тут можно сделать перехват трафика и убедиться, что вызывается API метод authorize
        homePage.waitPageContentVisible();
        actualPageContent = homePage.getPageContent();
        assertEquals(homePage.getPageContent(), expectedHomePageContent,
                "Текст '" + actualPageContent + "' на домашней странице не равен " + expectedHomePageContent);
    }
}
