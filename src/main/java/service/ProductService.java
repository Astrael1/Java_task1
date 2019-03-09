package service;

import dao.ProductDao;
import entity.ProductEntity;

public class ProductService
{
    private ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public ProductEntity findById(String id)
    {
        return productDao.findById(id);
    }
}
