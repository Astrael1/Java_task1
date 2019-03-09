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
        CashRegister register = new CashRegister( testData, resultCheck, resultCheck );
        try {
            register.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Product not found", resultCheck.toString());
    }

    @Test
    public void GivenProduct_ThenOutputIsPrinted()
    {
        InputStream testData = new ByteArrayInputStream("abc".getBytes());
        OutputStream resultCheck = new ByteArrayOutputStream();
        CashRegister register = new CashRegister( testData, resultCheck, resultCheck );
        try {
            register.readAndProcessCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Product with code: abc 96354", resultCheck.toString());
    }
}
