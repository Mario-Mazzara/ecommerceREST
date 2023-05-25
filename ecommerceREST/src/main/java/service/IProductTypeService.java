package service;

import java.util.List;

import viewmodel.ProductType;

public interface IProductTypeService {
	
	List<ProductType> getAllProductTypes();

	ProductType getTypeByid(byte[] id);

	String addType(ProductType type);

	ProductType editType(byte[] id, ProductType type);

	ProductType deleteType(byte[] id);
	
}
