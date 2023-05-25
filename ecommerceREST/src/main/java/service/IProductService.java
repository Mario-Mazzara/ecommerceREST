package service;

import java.util.List;

import viewmodel.Product;

public interface IProductService {

	List<Product> getAllProducts();

	Product getProduct(byte[] id);

	Product deleteProduct(byte[] id);

	Product editProduct(byte[] parseHex, Product product);

	String addProduct(Product product);

}