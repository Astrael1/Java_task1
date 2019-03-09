package tests;

import dao.ProductDao;
import entity.ProductEntity;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import service.ProductService;
import static org.junit.Assert.assertEquals;

public class mockitoTests
{
    @Mock private ProductDao mockProductDao;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Test
    public void testFindById()
    {
        MockitoAnnotations.initMocks(this);
        ProductService productService = new ProductService(mockProductDao);
        productService.findById("abc");
        Mockito.verify(mockProductDao).findById("abc");
    }

    @Test
    public void responseTest()
    {
        ProductService productService = new ProductService(mockProductDao);
        Mockito.when(mockProductDao.findById("abc")).thenReturn(giveSampleProduct());
        ProductEntity result = productService.findById("abc");
        assertEquals("milk", result.getName());
        assertEquals((Integer)10, result.getPrice());
        Mockito.verify(mockProductDao).findById("abc");
    }

    private ProductEntity giveSampleProduct()
    {
        return new ProductEntity("abc", "milk", 10);
    }
}
