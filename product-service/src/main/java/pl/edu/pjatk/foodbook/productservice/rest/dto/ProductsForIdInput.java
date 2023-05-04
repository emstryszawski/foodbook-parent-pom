package pl.edu.pjatk.foodbook.productservice.rest.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductsForIdInput {
    private String id;
    private List<UUID> productList;
}
