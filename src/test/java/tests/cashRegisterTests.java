package tests;

import org.junit.Test;
import saleCentre.CashRegister;

import java.io.*;

import static org.junit.Assert.*;

public class cashRegisterTests
{
    @Test
    public void GivenEmptyString_ThenCommunicateIsPrinted()
    {
        InputStream testData = new ByteArrayInputStream("\n".getBytes());
        OutputStream resultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister( testData, resultCheck, resultCheck );
        try {
            testRegister.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Invalid bar code", resultCheck.toString());
    }

    @Test
    public void GivenProduct_ThenOutputIsPrinted()
    {
        InputStream testData = new ByteArrayInputStream("abc".getBytes());
        OutputStream resultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister( testData, resultCheck, resultCheck );
        try {
            testRegister.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Product with code: abc 96354", resultCheck.toString());
    }

    @Test
    public void GivenListOfProductsAndExit_ThenTicketIsPrinted()
    {
        InputStream testData = new ByteArrayInputStream("a\nb\nc\nexit".getBytes());
        OutputStream lcdResultCheck = new ByteArrayOutputStream();
        OutputStream printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister(testData, lcdResultCheck, printerResultCheck);

        try {
            testRegister.processListOfProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String expectedResult = "Product with code: a 97\n" +
                "Product with code: b 98\n" +
                "Product with code: c 99\n" +
                "294";

        assertEquals(expectedResult, lcdResultCheck.toString());

        assertEquals(expectedResult, printerResultCheck.toString());
        //System.out.println(lcdResultCheck.toString());
        //System.out.println(printerResultCheck.toString());
    }

    @Test
    public void GivenListOfProducts_WithInvalidBars_ThenTicketIsPrinted()
    {
        InputStream testData = new ByteArrayInputStream("a\n\nc\nexit".getBytes());
        OutputStream lcdResultCheck = new ByteArrayOutputStream();
        OutputStream printerResultCheck = new ByteArrayOutputStream();
        CashRegister testRegister = new CashRegister(testData, lcdResultCheck, printerResultCheck);

        try {
            testRegister.processListOfProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //TODO asercje!

        System.out.println(lcdResultCheck.toString());
        System.out.println(printerResultCheck.toString());
    }
}
