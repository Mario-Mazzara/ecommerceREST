package service;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import library.UUID_utils;

import viewmodel.Product;

public class ProductService implements IProductService {
	private String getAllProductsQuery =  "SELECT Products.Id AS id,"
										+ "	   	   Products.Name AS name,"
										+ "        Products.Description AS description,"
										+ "        Products.TypeId as typeId,"
										+ "        ProductsType.Description AS type,"
										+ "        SalePrice.Price AS price "
										+ "FROM Products"
										+ "	 INNER JOIN ProductsType ON ProductsType.id = Products.typeId "
										+ "  INNER JOIN SalePrice ON SalePrice.productId = Products.Id "
										+ "GROUP BY SalePrice.productId HAVING MAX(SalePrice.Date)"
										+ ";"
										;
	
	private String getProductQuery =  "SELECT Products.Id AS id,"
										+ "	   	   Products.Name AS name,"
										+ "        Products.Description AS description,"
										+ "        Products.TypeId as typeId,"
										+ "        ProductsType.Description AS type,"
										+ "        SalePrice.Price AS price "
										+ "FROM Products "
										+ "	 INNER JOIN ProductsType ON ProductsType.id = Products.typeId "
										+ "  INNER JOIN SalePrice ON SalePrice.productId = Products.Id "
										+ "WHERE Products.Id = ? "
										+ "GROUP BY SalePrice.productId HAVING MAX(SalePrice.Date)"
										+ ";"
										;
	
	private String deleteProductQuery =  "DELETE FROM Products "
										+"WHERE  Products.id= ? ;"
										;
	private String deleteSalePriceQuery ="DELETE FROM SalePrice "
										+"WHERE  SalePrice.ProductId= ? ;"
										;
	
	private String editProductQuery =  "UPDATE Products "
										+ "SET Description = ?, Name= ?, TypeId = ? "
										+ "WHERE  Id = ? ;"
										;
	private String addSalePriceQuery =  "INSERT INTO SalePrice (Id,productid,price) "
										+ "VALUE (?, ?, ?);"
										;
	private String addProductQuery =  "INSERT INTO Products (id, TypeId, Name,description) "
										+ "VALUE (?, ?, ?, ?);"
										;

	
	private static ProductService instance = null;
	private DataSource dataSource = null;
	
	private ProductService() {
		try {
			dataSource = (DataSource)((Context)(new InitialContext()).lookup("java:comp/env")).lookup("jdbc/ecommerce");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ProductService getInstance() {
		if (instance == null){
			instance = new ProductService();
		}
		return instance;
	}
	
	@Override
	public List<Product> getAllProducts() {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<List<Product>> resultSetHandler = new BeanListHandler<Product>(Product.class);	
			return run.query(getAllProductsQuery, resultSetHandler);	
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public Product getProduct(byte[] id) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<Product> resultSetHandler = new BeanHandler<Product>(Product.class);
			Product product = run.query(getProductQuery, resultSetHandler,id);
			System.out.println(id.toString()+product);
			return product;	
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public Product deleteProduct(byte[] id) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<Product> resultSetHandler = new BeanHandler<Product>(Product.class);
			Product product = run.query(getProductQuery,resultSetHandler, id);
			run.execute(deleteSalePriceQuery,id);
			run.execute(deleteProductQuery,id);
			return 	product;
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}

	@Override
	public Product editProduct(byte[] id, Product product) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<Product> resultSetHandler = new BeanHandler<Product>(Product.class);
			run.execute(editProductQuery,product.getDescription(),product.getName(), product.getTypeId(),product.getId());
			run.execute(addSalePriceQuery,UUID_utils.getRandomUUID(),id,product.getPrice());
			return run.query(getProductQuery,resultSetHandler, id);
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
		
	}

	@Override
	public String addProduct(Product product) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			byte[] productId = UUID_utils.getRandomUUID();
			byte[] salePriceId = UUID_utils.getRandomUUID();
			run.execute(addProductQuery,productId ,product.getTypeId(),product.getName(),product.getDescription());
			run.execute(addSalePriceQuery,salePriceId,productId, product.getPrice());
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			
			return "Fail";
		}
	}

}
