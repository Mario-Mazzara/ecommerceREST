package viewmodel;

public class ProductTypeFactory {
	public static ProductType build(byte[] id, String description) {
		return new ProductType(id,description);
	}
	
	public static ProductType buildDefault() {
		return new ProductType(new byte[16],"");
	}
}
