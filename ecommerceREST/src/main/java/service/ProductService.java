package service;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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
										+ "WHERE Products.hidden = false "
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
										+ "WHERE Products.Id = ? AND Products.hidden = false "
										+ "GROUP BY SalePrice.productId HAVING MAX(SalePrice.Date)"
										+ ";"
										;
	
	private String deleteProductQuery =  "UPDATE Products SET hidden = true "
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
	private static Connection conn = null;
	
	private ProductService() {
		try {
			dataSource = (DataSource)((Context)(new InitialContext()).lookup("java:comp/env")).lookup("jdbc/ecommerce");
			conn = dataSource.getConnection();
		} catch (Exception e) {
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
			conn.setAutoCommit(false);
			Product product = run.query(getProductQuery,resultSetHandler, id);
			run.execute(conn, deleteSalePriceQuery,id);
			run.execute(conn,deleteProductQuery,id);
			conn.commit();
			return 	product;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public Product editProduct(byte[] id, Product product) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<Product> resultSetHandler = new BeanHandler<Product>(Product.class);
			conn.setAutoCommit(false);
			run.execute(conn,editProductQuery,product.getDescription(),product.getName(), product.getTypeId(),product.getId());
			run.execute(conn,addSalePriceQuery,UUID_utils.getRandomUUID(),id,product.getPrice());
			conn.commit();
			return run.query(getProductQuery,resultSetHandler, id);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return null;
		}
		
	}

	@Override
	public String addProduct(Product product) {
		try {
			QueryRunner run = new QueryRunner();
			byte[] productId = UUID_utils.getRandomUUID();
			byte[] salePriceId = UUID_utils.getRandomUUID();
			conn.setAutoCommit(false);
			run.execute(conn,addProductQuery,productId ,product.getTypeId(),product.getName(),product.getDescription());
			run.execute(conn,addSalePriceQuery,salePriceId,productId, product.getPrice());
			conn.commit();
			return "Success";
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return "Fail";
		}
	}

}
