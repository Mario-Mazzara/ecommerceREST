package api;


import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.MediaType;
import library.UUID_utils;
import service.ProductServiceFactory;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import viewmodel.Product;


@Path("/products")
public class ProductsAPI{
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Product> getAllProducts(){
		return ProductServiceFactory.getService().getAllProducts();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Product getProductById(@PathParam("id") String  id){
		return ProductServiceFactory.getService().getProduct(UUID_utils.parseUUID(id));
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addProduct(Product product) {
		return ProductServiceFactory.getService().addProduct(product);
	}
	
	@PUT//ibrido tra get e post (id nell'url e informazioni nel body)
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Product editProduct(@PathParam("id") String id,Product product) {
		return ProductServiceFactory.getService().editProduct(UUID_utils.parseUUID(id), product);
	}
	
	@DELETE//ibrido tra get e post 
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Product deleteProduct(@PathParam("id") String id) {
		return ProductServiceFactory.getService().deleteProduct(UUID_utils.parseUUID(id));
	}
	
}