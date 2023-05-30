package api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import service.AAAServiceFactory;
import viewmodel.User;

@Path("/AAA")
public class AAA_API {

	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String logIn(User user,@Context HttpServletRequest http) {
		 return AAAServiceFactory.getService().logIn(user,http);
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String logOut(@Context HttpServletRequest http) {
		return AAAServiceFactory.getService().logOut(http);
	}
}
