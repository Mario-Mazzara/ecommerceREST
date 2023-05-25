package viewmodel;

public class Product {
	private String name, description;
	private byte[]id;
	private String type;
	private byte[] typeId;
	private double price;
	
	public Product() {}
	public Product(String name, String description, byte[] id, String type, byte[] typeId, double price) {
		this.name = name;
		this.description = description;
		this.id = id;
		this.type = type;
		this.typeId = typeId;
		this.price = price;
	}
	
	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}	
	
	public byte[] getTypeId() {
		return typeId;
	}
	public void setTypeId(byte[] typeId) {
		this.typeId = typeId;
	}
}
