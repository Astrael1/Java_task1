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
import service.ProductService;

public class CashRegister
{
    private Scanner barScanner;
    private Writer lcdDisplay;
    private Writer printer;

    @Mock private ProductDao productDao;
    private ProductService productService;


    private List< Pair<String, Integer> > listOfProducts;


    public CashRegister(InputStream barReaderStream, OutputStream lcdDisplayStream, OutputStream printerStream )
    {
        this.barScanner = new Scanner(barReaderStream);
        this.lcdDisplay = new OutputStreamWriter(lcdDisplayStream);
        this.printer = new OutputStreamWriter(printerStream);
        listOfProducts = new LinkedList<>();

        MockitoAnnotations.initMocks(this);
        productService = new ProductService(productDao);
    }

    public CashRegister()
    {
        this.barScanner = new Scanner(System.in);
        this.lcdDisplay = new OutputStreamWriter(System.out);
        this.printer = new OutputStreamWriter(System.out);
        listOfProducts = new LinkedList<Pair<String, Integer>>();
        productService = new ProductService(productDao);
    }

    private void addProduct(String code)
    {
        ProductEntity result = new ProductEntity(code, "Product with code: " + code, code.hashCode());
        Mockito.when(productDao.findById(code)).thenReturn(result);
        ProductEntity requestedProduct = productService.findById(code);
        listOfProducts.add(new Pair<>(requestedProduct.getName(), requestedProduct.getPrice()));
    }

    private void displayLastProduct() throws IOException {
        Pair<String, Integer> lastProduct = listOfProducts.get(listOfProducts.size() - 1);
        String productName = lastProduct.getKey();
        Integer price = lastProduct.getValue();
        lcdDisplay.write(productName + " " + price + "\n");
        lcdDisplay.flush();
    }

    private void printTicket() throws IOException {
        Integer totalPrice = 0;
        for ( Pair<String, Integer> item: listOfProducts)
        {
            String productName = item.getKey();
            Integer price = item.getValue();
            totalPrice += price;
            printer.write(productName + " " + price + "\n");
            printer.flush();
        }

        printer.write(totalPrice.toString());
        printer.flush();

        lcdDisplay.write(totalPrice.toString());
        lcdDisplay.flush();
    }

    public void readAndProcessCode() throws IOException {
        String code = barScanner.nextLine();
        if(code.equals("") )
        {
            lcdDisplay.write("Invalid bar code\n");
            lcdDisplay.flush();
        }
        else if (code.equals("exit") )
        {
            printTicket();
        }
        else
        {
            addProduct(code);
            displayLastProduct();
        }
    }

    public void processListOfProducts() throws IOException {
        while (barScanner.hasNext())
        {
            readAndProcessCode();
        }
    }

}
