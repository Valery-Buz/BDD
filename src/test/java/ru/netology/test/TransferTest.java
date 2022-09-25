package ru.netology.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferTest {
    @Test
    void shouldTransferZeroMoneyBetweenOwnCards() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getFirstCardBalance();
        val balanceSecondCardBefore = dashboardPage.getSecondCardBalance();
        val moneyTransferPage = dashboardPage.secondCard();
        int amount = 0;
        moneyTransferPage.transferMoney(amount, DataHelper.FirstCard());
        val balanceFirstCardAfter = dashboardPage.getFirstCardBalance();
        val balanceSecondCardAfter = dashboardPage.getSecondCardBalance();
        assertEquals((balanceFirstCardBefore - amount), balanceFirstCardAfter);
        assertEquals((balanceSecondCardBefore + amount), balanceSecondCardAfter);
    }

    @Test
    void shouldTransferMoneyBetweenFirstCardToSecond() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getFirstCardBalance();
        val balanceSecondCardBefore = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.secondCard();
        int amount = 10000;
        transferPage.transferMoney(amount, DataHelper.FirstCard());
        val balanceFirstCardAfter = dashboardPage.getFirstCardBalance();
        val balanceSecondCardAfter = dashboardPage.getSecondCardBalance();
        assertEquals((balanceFirstCardBefore - amount), balanceFirstCardAfter);
        assertEquals((balanceSecondCardBefore + amount), balanceSecondCardAfter);
    }

    @Test
    void shouldTransferMoneyBetweenSecondCardToFirst() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getFirstCardBalance();
        val balanceSecondCardBefore = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.firstCard();
        int amount = 1000;
        transferPage.transferMoney(amount, DataHelper.SecondCard());
        val balanceFirstCardAfter = dashboardPage.getFirstCardBalance();
        val balanceSecondCardAfter = dashboardPage.getSecondCardBalance();
        assertEquals((balanceFirstCardBefore + amount), balanceFirstCardAfter);
        assertEquals((balanceSecondCardBefore - amount), balanceSecondCardAfter);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsUnderLimit() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.verify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getFirstCardBalance();
        val balanceSecondCardBefore = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.secondCard();
        int amount = 250000;
        transferPage.transferMoney(amount, DataHelper.FirstCard());
        transferPage.errorMessage();
        val balanceFirstCardAfter = dashboardPage.getFirstCardBalance();
        val balanceSecondCardAfter = dashboardPage.getSecondCardBalance();
        assertEquals(balanceFirstCardBefore, balanceFirstCardAfter);
        assertEquals(balanceSecondCardBefore, balanceSecondCardAfter);
    }
}
