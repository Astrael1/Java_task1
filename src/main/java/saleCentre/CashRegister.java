package saleCentre;

import dao.ProductDao;
import entity.ProductEntity;
import javafx.util.Pair;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import service.ProductService;

public class CashRegister
{
    private Scanner barScanner;
    private Writer lcdDisplay;
    private Writer printer;

    @Mock private ProductDao productDao;
    private ProductService productService;


    private List< Pair<String, Integer> > listOfItems;


    public CashRegister(InputStream barReaderStream, OutputStream lcdDisplayStream, OutputStream printerStream )
    {
        this.barScanner = new Scanner(barReaderStream);
        this.lcdDisplay = new OutputStreamWriter(lcdDisplayStream);
        this.printer = new OutputStreamWriter(printerStream);
        listOfItems = new LinkedList<Pair<String, Integer>>();

        MockitoAnnotations.initMocks(this);
        productService = new ProductService(productDao);
    }

    public CashRegister()
    {
        this.barScanner = new Scanner(System.in);
        this.lcdDisplay = new OutputStreamWriter(System.out);
        this.printer = new OutputStreamWriter(System.out);
        listOfItems = new LinkedList<Pair<String, Integer>>();
        productService = new ProductService(productDao);
    }

    private void addItem(String code)
    {
        ProductEntity result = new ProductEntity(code, "Product with code: " + code, code.hashCode());
        Mockito.when(productDao.findById(code)).thenReturn(result);
        ProductEntity requestedProduct = productService.findById(code);
        listOfItems.add(new Pair<>(requestedProduct.getName(), requestedProduct.getPrice()));
    }

    private void displayLastItem() throws IOException {
        Pair<String, Integer> lastProduct = listOfItems.get(listOfItems.size() - 1);
        String productName = lastProduct.getKey();
        Integer price = lastProduct.getValue();
        lcdDisplay.write(productName + " " + price);
        lcdDisplay.flush();
    }

    private void printTicketAndFinish() throws IOException {
        Integer totalPrice = 0;
        for ( Pair<String, Integer> item: listOfItems )
        {
            String productName = item.getKey();
            Integer price = item.getValue();
            totalPrice += price;
            printer.write(productName + " " + price);
            printer.flush();
        }

        lcdDisplay.write(totalPrice);
        lcdDisplay.flush();
    }

    public void readAndProcessCode() throws IOException {
        String code = barScanner.nextLine();
        if(code.equals("") )
        {
            lcdDisplay.write("Product not found");
            lcdDisplay.flush();
        }
        else if (code.equals("exit") )
        {
            printTicketAndFinish();
        }
        else
        {
            addItem(code);
            displayLastItem();
        }
    }

}
