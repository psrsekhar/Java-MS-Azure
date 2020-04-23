package src.main.java.edu.training.azure.model;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class CustomerEntity extends TableServiceEntity {
	public CustomerEntity(String userName, String name) {
		this.partitionKey = userName;
		this.rowKey = name;
	}

	public CustomerEntity() {
	}

	String email;
	String phoneNumber;
	String address;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
