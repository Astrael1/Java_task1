package saleCentre;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class Demo
{
    private OutputStream lcdOutputStream;
    private OutputStream printerOutputStream;
    private CashRegister cashRegister;

    private void printMessage()
    {
        System.out.print("Hello User!\nWitch device would You like to use?\nType lcd / printer / both");
    }

    private boolean getResponse()
    {
        Scanner scanner = new Scanner(System.in);
        String message = scanner.next();

        switch (message)
        {
            case "lcd": {
                lcdOutputStream = System.out;
                printerOutputStream = new ByteArrayOutputStream();
                return true;
            }
            case "printer": {
                printerOutputStream = System.out;
                lcdOutputStream = new ByteArrayOutputStream();
                return true;
            }
            case "both": {
                printerOutputStream = System.out;
                lcdOutputStream = System.out;
                return true;
            }
            default:
                System.out.println("Wrong input!");
                return false;
        }
    };

    public void serve()
    {
        do {
            printMessage();
        } while (!getResponse());
        cashRegister = new CashRegister(System.in, lcdOutputStream, printerOutputStream);
        try {
            cashRegister.processListOfProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
