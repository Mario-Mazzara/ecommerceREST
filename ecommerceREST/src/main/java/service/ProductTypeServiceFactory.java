package service;

public class ProductTypeServiceFactory {
	public static IProductTypeService getService() {
		return ProductTypeService.getInstance();
	}
}
