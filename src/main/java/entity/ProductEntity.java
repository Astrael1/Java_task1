package entity;

public class ProductEntity
{
    private String id;
    private String name;
    private Integer price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ProductEntity(String id, String name, Integer price)
    {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
