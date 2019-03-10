package tests;

import org.junit.*;
import saleCentre.CashRegister;

import java.io.*;

import static org.junit.Assert.*;

public class cashRegisterTests
{
    private static InputStream testData;
    private static OutputStream lcdResultCheck;
    private static OutputStream printerResultCheck;

    /*@After
    public void cleanUp()
    {
        try {
            testData.close();
            lcdResultCheck.close();
            printerResultCheck.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    

    @Test
    public void GivenEmptyString_ThenCommunicateIsPrinted()
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
        assertEquals("Invalid bar code\n", lcdResultCheck.toString());
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
        assertEquals("Product with code: abc 96354\n", lcdResultCheck.toString());
    }

    @Test
    public void GivenListOfProductsAndExit_ThenTicketIsPrinted()
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

        assertEquals(expectedResult, lcdResultCheck.toString());

        assertEquals(expectedResult, printerResultCheck.toString());
        //System.out.println(lcdResultCheck.toString());
        //System.out.println(printerResultCheck.toString());
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

        assertEquals( lcdExpected, lcdResultCheck.toString());
        assertEquals( printerExpected, printerResultCheck.toString());
    }
}
