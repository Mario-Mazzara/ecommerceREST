package service;

public class ProductServiceFactory {
	public static IProductService getService() {
		return ProductService.getInstance();
	}
}
