package service;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import jakarta.json.Json;
import jakarta.servlet.http.HttpServletRequest;
import viewmodel.Product;
import viewmodel.User;

public class AAAService implements IAAAService{
	private static AAAService instance = null;
	private DataSource dataSource = null;
	
	private String getAccountPrivilegeQuery =  "SELECT Products.Id AS id,"
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
	private String authenticationQuery =  "SELECT Email "
										+ "FROM authentications "
										+ "WHERE Email = ? AND Password = ?;"
										;
	
	private AAAService() {
		try {
			dataSource = (DataSource)((Context)(new InitialContext()).lookup("java:comp/env")).lookup("jdbc/ecommerce");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	public static AAAService getInstance() {
		if (instance == null){
			instance = new AAAService();
		}
		return instance;
	}
	
	@Override
	public boolean authenticate(String email, String password) {
		try {
			QueryRunner run = new QueryRunner(dataSource);
			ResultSetHandler<Product> resultSetHandler = new BeanHandler<Product>(Product.class);
			run.execute(authenticationQuery, resultSetHandler,email,password);
			return true; //	
		} catch (Exception e) {
			e.printStackTrace();
			return false;//
		}
	}
	
	@Override
	public String getAccountPrivilege(String email, String password) {
		return null;
	}

	@Override
	public String logIn(User user, HttpServletRequest http) {
		if( authenticate(user.getEmail(),user.getPassword()) ) {
			http.getSession().setAttribute("email", user.getEmail());
			http.getSession().setAttribute("password", user.getPassword());
			return Json.createObjectBuilder().add("response", "success").toString();
		}else {
			return Json.createObjectBuilder().add("response", "fail").build().toString();
		}
	}

	@Override
	public String logOut(HttpServletRequest http) {
		try {
		http.getSession().invalidate();
		return Json.createObjectBuilder().add("response", "success").toString();
		}catch(Exception e){
			return Json.createObjectBuilder().add("response", "fail").toString();
		}
	}
	
}
