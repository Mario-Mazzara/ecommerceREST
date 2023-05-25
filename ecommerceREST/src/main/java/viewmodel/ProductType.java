package viewmodel;

public class ProductType {
	private byte[] id;
	private String description;
	public ProductType() {}
	public byte[] getId() {
		return id;
	}
	
	public ProductType(byte[] id, String description) {
		this.id = id;
		this.description = description;
	}
	public void setId(byte[] id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
