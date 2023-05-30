package service;

public class AAAServiceFactory {
	public static IAAAService getService() {
		return AAAService.getInstance();
	}
}
