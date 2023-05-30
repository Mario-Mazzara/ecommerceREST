package service;

import jakarta.servlet.http.HttpServletRequest;
import viewmodel.User;

public interface IAAAService {
	String logIn(User user, HttpServletRequest http);
	String logOut(HttpServletRequest http);
	boolean authenticate(String email, String password);
	String getAccountPrivilege(String email, String password);
}
