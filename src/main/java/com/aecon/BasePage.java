package com.aecon.logic;


import com.hp.lft.report.ReportAdditionalInfo;
import com.hp.lft.report.ReportContextInfo;
import com.hp.lft.report.ReportException;
import com.hp.lft.report.Reporter;
import com.hp.lft.verifications.Verify;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;

public abstract class BasePage {

    protected WebDriver driver;
    protected WebDriverWait driverWait;
    //protected static SUBCON_Profile_DAO subconProfileDao = new SUBCON_Profile_DAO();

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        driverWait = DriverUtils.initWaits(driver, 5, 60, 1);
    }

    @FindBy(xpath = "//*[@id=\"header-logo\"]")
    WebElement headerLogo;
    @FindBy(className = "message-top")
    WebElement importantMessage;

    public BasePage() {

    }

    public BasePage validateAeconMessages(String page) throws IOException {
        Verify.areEqual(headerLogo.getAttribute("alt"), "Aecon",
                "Verifying that logo is displayed on the " + page + " Page", "some description", DriverUtils.takeScreenshot(driver));
        Verify.contains("IMPORTANT: Aecon uses ISNetworld as our primary system for managing subcontractor health & safety/insurance prequalification requirements. If your company is already an active subscriber to ISN, please be advised you will no longer be required to maintain a prequalification account via this portal. For further details about ISNetworld, please contact the ISN Customer Service Team at (800) 976-1303 or visit their website at www.isn.com. For questions regarding the subcontractor prequalification process, please contact subconprequal@aecon.com"
                , importantMessage.getText(), "Verifying that IMPORTANT message is displayed on the " + page + " Page", "some description", DriverUtils.takeScreenshot(driver));
        return this;
    }

    public BasePage proceedToURL(String url) throws ReportException {
        Reporter.reportEvent("Proceed to " + url, "");
        driver.get(url);
        return this;
    }

    @FindBy(xpath = "//*[@id=\"destinationSelect\"]//print-preview-settings-section/div/select")
    WebElement selectDropdown;
    @FindBy(xpath = "//*[@id=\"survey-footer\"]/div/a[2]")
    WebElement printButton;

    public void print() {

    }

    public abstract BasePage ensurePageLoaded() throws InterruptedException, IOException;

    private void waitForJSToLoad() {
        driverWait.until((Function<? super WebDriver, Boolean>) WebDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                .equalsIgnoreCase("complete"));
    }

    protected void clickItemFromListByText(WebElement list, String text, String tagToBeclicked) {
        List<WebElement> elementList = list.findElements(By.tagName("li"));
        for (WebElement option : elementList) {
            if (option.getText().contains(text)) {
                option.findElement(By.tagName(tagToBeclicked)).click();
            }
        }
    }

    protected WebElement scrollToElement(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element);
        action.perform();
        return element;
    }

    protected WebElement waitForTextToBe(WebElement element, String text) {
        driverWait.until((Function<? super WebDriver, Boolean>) ExpectedConditions.textToBePresentInElement(element, text));
        return element;
    }

    protected void jsClick(WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    protected WebElement scrollTo(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        return element;
    }

    protected WebElement waitForVisible(WebElement element) {
        waitForJSToLoad();
        driverWait.until(ExpectedConditions.visibilityOf(element));
        return element;
    }

    protected void waitForVisible(String xpath) {
        waitForJSToLoad();
        driverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpath))));

    }

    protected void pressEnterButton() throws AWTException {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_ENTER);
        r.keyRelease(KeyEvent.VK_ENTER);
    }

    protected void sendKeys(String keys) throws AWTException, InterruptedException {
        Robot robot = new Robot();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(keys);
        clipboard.setContents(stringSelection, null);
        for (int i = 0; i < 5; i++) {
            Thread.sleep(1000);
        }
        robot.keyPress(KeyEvent.VK_CONTROL);
        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_V);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(200);
        robot.keyPress(KeyEvent.VK_V);
        Thread.sleep(200);
    }

    protected void waitInSeconds(int seconds) {
        driverWait.withTimeout(Duration.ofSeconds(seconds));
    }

    protected WebElement hoverOverAndClick(WebElement element) {
        waitForJSToLoad();
        waitToBeClickable(element);
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.doubleClick(element);
        return element;
    }

    protected WebElement waitToBeClickable(WebElement element){
        waitForJSToLoad();
        driverWait.until(ExpectedConditions.elementToBeClickable(element));
        return element;
    }

    protected WebElement waitToBeDisabled(WebElement element){
        waitForJSToLoad();
        driverWait.until(ExpectedConditions.invisibilityOf(element));
        return element;
    }

    public boolean clickWithRetry(String xpath) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 5) {
            try {
                driver.findElement(By.xpath(xpath)).click();
                result = true;
                break;
            } catch(StaleElementReferenceException e) {
            }
            attempts++;
        }
        return result;
    }
    protected String getXpath(WebElement element){
        int n = element.findElements(By.xpath("./ancestor::*")).size();
        String path = "";
        WebElement current = element;
        for(int i = n; i > 0; i--){
            String tag = current.getTagName();
            int lvl = current.findElements(By.xpath("./preceding-sibling::" + tag)).size() + 1;
            path = String.format("/%s[%d]%s", tag, lvl, path);
            current = current.findElement(By.xpath("./parent::*"));
        }
        return "/" + current.getTagName() + path;
    }
    public String getTextwithRetry(String xpath) {
        boolean result = false;
        String text = null;
        int attempts = 0;
        while(attempts < 5) {
            try {

                text = driver.findElement(By.xpath(xpath)).getText();
                break;
            } catch(StaleElementReferenceException e) {
            }
            attempts++;
        }
        return text;
    }

    public boolean sendKeysWithRetry(String xpath, String keys ) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 5) {
            try {
                driver.findElement(By.xpath(xpath)).sendKeys(keys);
                result = true;
                break;
            } catch (StaleElementReferenceException | NoSuchElementException e) {
            }
            attempts++;
        }
        return result;
    }

    public void startStep(String[] parameters, String methodName) throws ReportException, IOException {

        ReportAdditionalInfo reportAdditionalInfo = new ReportAdditionalInfo();
        TreeMap<String, Object> reportAddInfo = new TreeMap<>();
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++)
                reportAddInfo.put("parameter#" + i + ": ", parameters[i]);
            reportAdditionalInfo.setData(reportAddInfo);
        }
        reportAdditionalInfo.setImage(DriverUtils.takeScreenshot(driver));
        ReportContextInfo reportContextInfo = new ReportContextInfo();
        reportContextInfo.setData(reportAddInfo);

        Reporter.startReportingContext(methodName, reportContextInfo);
        Reporter.reportEvent("Started " + methodName, reportAdditionalInfo);
    }

    public void endStep() throws ReportException, IOException {
        if(driver!=null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        ReportAdditionalInfo reportAdditionalInfo = new ReportAdditionalInfo();
        reportAdditionalInfo.setImage(DriverUtils.takeScreenshot(driver));
        Reporter.reportEvent("Completed ", reportAdditionalInfo);
        Reporter.endReportingContext();
    }

    public void verifySaveSuccessfully(WebElement element, String s, String parameters[]) throws IOException, ReportException {
        waitForVisible(element);
        Verify.contains("Successfully", element.getText(), "Verifying that " + s + " was saved successfully", "", DriverUtils.takeScreenshot(driver));
        ReportAdditionalInfo reportAdditionalInfo = new ReportAdditionalInfo();
        TreeMap<String, Object> reportAddInfo = new TreeMap<>();
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++)
                reportAddInfo.put("parameter#" + i + ": ", parameters[i]);
            reportAdditionalInfo.setData(reportAddInfo);
        }
        reportAdditionalInfo.setImage(DriverUtils.takeScreenshot(driver));
        Reporter.reportEvent("Finished " + s, reportAdditionalInfo);
    }


}
