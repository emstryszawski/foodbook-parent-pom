package pl.edu.pjatk.foodbook.productservice.rest.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException() {
        super("Product was not found");
    }
}
