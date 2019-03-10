package tests;

import org.junit.*;
import saleCentre.CashRegister;

import java.io.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class cashRegisterTests
{
    private static InputStream testData;
    private static OutputStream lcdResultCheck;
    private static OutputStream printerResultCheck;

    @After
    public void cleanUp()
    {
        try {
            testData.close();
            lcdResultCheck.close();
            printerResultCheck.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void GivenEmptyString_ThenInvalidBarMessageIsPrinted()
    {
        testData = new ByteArrayInputStream("\n".getBytes());
        lcdResultCheck = new ByteArrayOutputStream();
        printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister( testData, lcdResultCheck, printerResultCheck );
        try {
            testRegister.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expectedResult = "Invalid bar code\n";
        assertThat(lcdResultCheck.toString(), is(equalTo(expectedResult)));
    }

    @Test
    public void GivenProduct_ThenOutputIsPrinted()
    {
        testData = new ByteArrayInputStream("abc".getBytes());
        lcdResultCheck = new ByteArrayOutputStream();
        printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister( testData, lcdResultCheck, printerResultCheck );
        try {
            testRegister.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expectedResult = "Product with code: abc 96354\n";
        assertThat(lcdResultCheck.toString(), is(equalTo(expectedResult)));
    }

    @Test
    public void GivenListOfProducts_AndExit_ThenTicketIsPrinted()
    {
        testData = new ByteArrayInputStream("a\nb\nc\nexit".getBytes());
        lcdResultCheck = new ByteArrayOutputStream();
        printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister(testData, lcdResultCheck, printerResultCheck);

        try {
            testRegister.processListOfProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedResult = "Product with code: a 97\n" +
                "Product with code: b 98\n" +
                "Product with code: c 99\n" +
                "294\n";

        assertThat(lcdResultCheck.toString(), is(equalTo(expectedResult)));
        assertThat(printerResultCheck.toString(), is(equalTo(expectedResult)));
    }

    @Test
    public void GivenListOfProducts_WithInvalidBars_ThenTicketIsPrinted()
    {
        testData = new ByteArrayInputStream("a\n\nc\nexit".getBytes());
        lcdResultCheck = new ByteArrayOutputStream();
        printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister(testData, lcdResultCheck, printerResultCheck);

        try {
            testRegister.processListOfProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String lcdExpected = "Product with code: a 97\n" +
                "Invalid bar code\n" +
                "Product with code: c 99\n" +
                "196\n";
        String printerExpected = "Product with code: a 97\n" +
                "Product with code: c 99\n" +
                "196\n";

        assertThat( lcdResultCheck.toString(), is(equalTo(lcdExpected)));
        assertThat( printerResultCheck.toString(), is(equalTo(printerExpected)));
    }

    @Test
    public void GivenNullProduct_ThenNotFoundMessageIsDisplayed()
    {
        testData = new ByteArrayInputStream("null\n".getBytes());
        lcdResultCheck = new ByteArrayOutputStream();
        printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister( testData, lcdResultCheck, printerResultCheck );
        try {
            testRegister.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedResult = "Product not found\n";
        assertThat(lcdResultCheck.toString(), is(equalTo(expectedResult)));
    }
}
