package src.main.java.edu.training.azure;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;

import src.main.java.edu.training.azure.model.CustomerEntity;

public class App {
	public static final String connectionString = "enter connection string either from Access keys or generated SAS keys";
	public static final String tableName = "customers";

	public static void main(String[] args) {
		insert();
		get();
		update();
	}

	public static void insert() {
		CloudStorageAccount cloudStorageAccount;
		CloudTableClient cloudTableClient;
		CloudTable cloudTable;
		UUID uniqueId = UUID.randomUUID();
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudTableClient = cloudStorageAccount.createCloudTableClient();
			cloudTable = cloudTableClient.getTableReference(tableName);
			cloudTable.createIfNotExists();

			CustomerEntity customer = new CustomerEntity("p", "Sai - " + uniqueId);
			customer.setPhoneNumber("1023456789");
			customer.setEmail("azure@gmail.com");
			customer.setAddress("Bangalore - 560100");

			TableOperation tableOperation = TableOperation.insertOrReplace(customer);
			cloudTable.execute(tableOperation);
			System.out.println("Entity saved successfully");
		} catch (StorageException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public static void get() {
		CloudStorageAccount cloudStorageAccount;
		CloudTableClient cloudTableClient;
		CloudTable cloudTable;
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudTableClient = cloudStorageAccount.createCloudTableClient();
			cloudTable = cloudTableClient.getTableReference(tableName);

			String partitionFilter = TableQuery.generateFilterCondition("PartitionKey", QueryComparisons.EQUAL,
					"p");
			TableQuery<CustomerEntity> query = TableQuery.from(CustomerEntity.class).where(partitionFilter);
			for (CustomerEntity entity : cloudTable.execute(query)) {				
				System.out.println(entity.getPartitionKey() + " " + entity.getRowKey() + "\t" + entity.getEmail() + "\t"
						+ entity.getPhoneNumber() + "\t" + entity.getAddress());
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void update() {
		CloudStorageAccount cloudStorageAccount;
		CloudTableClient cloudTableClient;
		CloudTable cloudTable;
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudTableClient = cloudStorageAccount.createCloudTableClient();
			cloudTable = cloudTableClient.getTableReference(tableName);
			
			TableOperation retrieve = TableOperation.retrieve("p", "<enter generated row key here>",CustomerEntity.class);
			
			CustomerEntity customer = cloudTable.execute(retrieve).getResultAsType();
			customer.setPartitionKey(customer.getPartitionKey());
			customer.setRowKey(customer.getRowKey());
			customer.setPhoneNumber("012324425");
			customer.setAddress("Bangalore - 560101");
			
			TableOperation operation = TableOperation.merge(customer);
			cloudTable.execute(operation);
			
			System.out.println("Entity updated successfully");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
