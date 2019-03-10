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
        listOfProducts = new LinkedList<>();
        productService = new ProductService(productDao);
    }


    private boolean addProduct(String code)
    {
        ProductEntity requestedProduct = getProductFromDatabase(code);
        if(requestedProduct != null)
        {
            listOfProducts.add(new Pair<>(requestedProduct.getName(), requestedProduct.getPrice()));
            return true;
        }
        return false;
    }

    private ProductEntity getProductFromDatabase(String code)
    {
        if(!code.equals("null"))
        {
            ProductEntity result = new ProductEntity(code, "Product with code: " + code, code.hashCode());
            Mockito.when(productDao.findById(code)).thenReturn(result);
            return productService.findById(code);
        }
        return null;
    }

    private void displayLastProduct() throws IOException {
        Pair<String, Integer> lastProduct = listOfProducts.get(listOfProducts.size() - 1);
        String productName = lastProduct.getKey();
        Integer price = lastProduct.getValue();
        lcdDisplay.write(productName + " " + price + "\n");
        lcdDisplay.flush();
    }

    private void displayOnLCD(String message) throws IOException {
        lcdDisplay.write(message);
        lcdDisplay.flush();
    }

    private void printOnPrinter(String message) throws IOException {
        printer.write(message);
        printer.flush();
    }

    private void printTicket() throws IOException {
        Integer totalPrice = 0;
        for ( Pair<String, Integer> product: listOfProducts)
        {
            String productName = product.getKey();
            Integer price = product.getValue();
            totalPrice += price;
            printOnPrinter(productName + " " + price + "\n");
        }

        listOfProducts.clear();

        printOnPrinter(totalPrice.toString() + "\n");
        displayOnLCD(totalPrice.toString() + "\n");
    }

    public void readAndProcessCode() throws IOException {
        String code = barScanner.nextLine();
        if(code.equals("") )
        {
            displayOnLCD("Invalid bar code\n");
        }
        else if (code.equals("exit") )
        {
            printTicket();
        }
        else if (addProduct(code))
        {
            displayLastProduct();
        }
        else
        {
            displayOnLCD("Product not found\n");
        }
    }

    public void processListOfProducts() throws IOException {
        while (barScanner.hasNext())
        {
            readAndProcessCode();
        }
    }

}
