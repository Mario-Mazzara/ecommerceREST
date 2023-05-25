package service;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import library.UUID_utils;

import org.apache.commons.dbutils.handlers.BeanHandler;

import viewmodel.ProductType;
import viewmodel.ProductTypeFactory;

public class ProductTypeService implements IProductTypeService{
	private static ProductTypeService instance = null;
	private DataSource dataSource = null;
	
	private String AllProductsTypeQuery = "SELECT Id,Description FROM ProductsType";
	
	private String getTypeQuery =  "SELECT ProductsType.Id AS id, "
										+ "ProductsType.description AS description "
										+ "FROM ProductsType "
										+ "WHERE ProductsType.Id = ? ;"
										;
	
	private String deleteTypeQuery =  "UPDATE ProductsType SET hidden = true "
										+"WHERE ProductsType.id = ? ;"
										;
	
	private String editTypeQuery =  "UPDATE ProductsType "
										+ "SET Description = ? "
										+ "WHERE  Id = ? ;"
										;
	
	private String addTypeQuery =  "INSERT INTO ProductsType (id,description) "
										+ "VALUE (?, ?);"
										;
	
	private String getProductByTypeQuery =  "SELECT Products.Id AS id,"
											+ "FROM Products "
											+ "WHERE Products.TypeId = ? AND Products.hidden = false "
											+ "GROUP BY SalePrice.productId HAVING MAX(SalePrice.Date)"
											+ ";"
											;
	
	
	private ProductTypeService() {
		try {
			dataSource = (DataSource)((Context)(new InitialContext()).lookup("java:comp/env")).lookup("jdbc/ecommerce");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ProductTypeService getInstance() {
		if (instance == null){
			instance = new ProductTypeService();
		}
		return instance;
	}
	@Override
	public List<ProductType> getAllProductTypes() {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<List<ProductType>> resultSetHandler = new BeanListHandler<ProductType>(ProductType.class);	
			return run.query(AllProductsTypeQuery, resultSetHandler);	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public ProductType getTypeByid(byte[] id) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<ProductType> resultSetHandler = new BeanHandler<ProductType>(ProductType.class);	
			return run.query(getTypeQuery, resultSetHandler,id);	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public String addType(ProductType type) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<ProductType> resultSetHandler = new BeanHandler<ProductType>(ProductType.class);	
			 run.execute(addTypeQuery, resultSetHandler, UUID_utils.getRandomUUID(), type.getDescription());	
			 return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Fail";
		}
	}
	@Override
	public ProductType editType(byte[] id, ProductType type) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<ProductType> resultSetHandler = new BeanHandler<ProductType>(ProductType.class);	
			 run.execute(editTypeQuery, resultSetHandler,type.getDescription(),id);	
			 return run.query(getTypeQuery,resultSetHandler,id);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public ProductType deleteType(byte[] id) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<List<ProductType>> resultSetHandlerProduct = new BeanListHandler<ProductType>(ProductType.class);
			if(run.query(getProductByTypeQuery,resultSetHandlerProduct, id).size() == 0){
				throw new Exception();
			}
			ResultSetHandler<ProductType> resultSetHandler = new BeanHandler<ProductType>(ProductType.class);
			ProductType type = run.query(getTypeQuery,resultSetHandler, id);
			run.execute(deleteTypeQuery,id);
			return 	type;
		}catch (Exception e) {
			e.printStackTrace();
			ProductType fail = ProductTypeFactory.buildDefault();
			fail.setDescription("Failed Delete");
			return fail;
		}
	}
}
