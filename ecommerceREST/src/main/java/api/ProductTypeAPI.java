package api;


import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import library.UUID_utils;
import service.ProductTypeServiceFactory;


import viewmodel.ProductType;

@Path("/productstype")
public class ProductTypeAPI {
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProductType> getAllProductTypes(){
		return ProductTypeServiceFactory.getService().getAllProductTypes();
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductType getTypeById(@PathParam("id") String  id){
		return ProductTypeServiceFactory.getService().getTypeByid(UUID_utils.parseUUID(id));
	}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addType(ProductType type) {
		return ProductTypeServiceFactory.getService().addType(type);
	}
	
	@PUT//ibrido tra get e post (id nell'url e informazioni nel body)
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ProductType editType(@PathParam("id") String id,ProductType type) {
		return ProductTypeServiceFactory.getService().editType(UUID_utils.parseUUID(id), type);
	}
	
	@DELETE//ibrido tra get e post 
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ProductType deleteType(@PathParam("id") String id) {
		return ProductTypeServiceFactory.getService().deleteType(UUID_utils.parseUUID(id));
	}
	
}
