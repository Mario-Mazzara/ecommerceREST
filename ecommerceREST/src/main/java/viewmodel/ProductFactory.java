package viewmodel;

public class ProductFactory {
	public static Product build(String name, String description,byte[] id, String type, byte[] typeId, double price) {
		return new Product(name,description,id,type,typeId,price);
	}
	
	public static Product buildDefault() {
		return new Product("","",new byte[16],"",new byte[16],0d);
	}
}
