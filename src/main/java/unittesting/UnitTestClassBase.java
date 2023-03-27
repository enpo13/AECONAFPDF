package unittesting;

import com.hp.lft.report.Status;
import com.hp.lft.unittesting.TestNgUnitTestBase;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.Iterator;

public class UnitTestClassBase extends TestNgUnitTestBase {

    @BeforeSuite(alwaysRun = true)
    public void baseBeforeSuite() throws Exception {
        suiteSetup();
    }

    @BeforeClass(alwaysRun = true)
    public void baseBeforeClass(ITestContext ctx) throws Exception {
        String[] context = {ctx.getSuite().getName(), ctx.getName(), this.getClass().getName()};
        className.set(this.getClass().getName());
        classSetup(context);
    }

    @BeforeMethod(alwaysRun = true)
    public void baseBeforeMethod(Object[] arguments, ITestContext ctx, Method method) throws Exception {
        String methodName = super.getMethodName(method, arguments);
        String methodName2 = null;
        if (methodName.contains("[")) {
            methodName2 = methodName.replaceAll("\\[(.*?)\\]", "");
        } else {
            methodName2 = methodName;
        }
        String[] context = {ctx.getSuite().getName(), ctx.getName(), this.getClass().getName(), methodName2};
        testName.set(methodName2);
        testSetup(context);
    }

    @AfterMethod(alwaysRun = true)
    public void baseAfterMethod(ITestResult result) throws Exception {

        //testTearDown();
        setStatus(result.getStatus() == ITestResult.FAILURE ? Status.Failed : Status.Passed);

    }

    @AfterClass(alwaysRun = true)
    public void baseAfterClass(ITestContext ctx) throws Exception {
        classTearDown();
    }

    @AfterSuite(alwaysRun = true)
    public void baseAfterSuite() throws Exception {
        suiteTearDown();
        getReporter().generateReport();
    }

    @Override
    protected String getTestName() {
        return testName.get();
    }

    @Override
    protected String getClassName() {
        return className.get();
    }

    @DataProvider(name = "ALM")
    public Iterator<String[]> data() throws Exception {
        return getAlmData().iterator();
    }

    protected ThreadLocal<String> className = new ThreadLocal<String>();
    protected ThreadLocal<String> testName = new ThreadLocal<String>();
}
