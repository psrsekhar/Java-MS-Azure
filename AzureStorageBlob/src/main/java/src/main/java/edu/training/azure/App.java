package src.main.java.edu.training.azure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.OperationContext;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import src.main.java.edu.training.azure.model.Customer;

public class App {
	public static final String connectionString = "enter connection string either from Access keys or generated SAS keys";

	public static void main(String[] args)
			throws InvalidKeyException, URISyntaxException, StorageException, JsonProcessingException {
		uploadBlob();
		viewBlob();
		downloadBlob();
	}

	public static void uploadBlob() {
		CloudStorageAccount cloudStorageAccount;
		CloudBlobClient cloudBlobClient;
		CloudBlobContainer container;
		CloudBlockBlob blob;
		File file;
		Writer output;
		UUID uniqueId = UUID.randomUUID();
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
			container = cloudBlobClient.getContainerReference("test");

			System.out.println("Creating container: " + container.getName());
			container.createIfNotExists(BlobContainerPublicAccessType.CONTAINER, new BlobRequestOptions(),
					new OperationContext());
			System.out.println("Container : " + container.getName() + " created");

			Customer customer = new Customer();
			customer.setName("Test");
			customer.setPhoneNumber(1023456789);
			customer.setEmail("azure@gmail.com");
			customer.setAddress("Bangalore - 560100");

			ObjectMapper objectMapper = new ObjectMapper();
			String customerData = objectMapper.writeValueAsString(customer);

			file = File.createTempFile("customer-" + uniqueId, ".json");
			System.out.println("Creating a sample file at: " + file.toString());
			output = new BufferedWriter(new FileWriter(file));
			output.write(customerData);
			output.close();
			System.out.println("Customer data generated");

			System.out.println("Customer data uploading....");
			blob = container.getBlockBlobReference(file.getName());
			blob.uploadFromFile(file.getAbsolutePath());
			System.out.println("Customer data upload successfull");
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		} finally {
		}
	}

	public static void viewBlob() {
		CloudStorageAccount cloudStorageAccount;
		CloudBlobClient cloudBlobClient;
		CloudBlobContainer container;
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
			container = cloudBlobClient.getContainerReference("test");
			System.out.println("List of blobs in container");
			for (ListBlobItem blobItem : container.listBlobs()) {
				System.out.println("URI of blob is: " + blobItem.getUri());
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		} finally {
		}
	}

	public static void downloadBlob() {
		CloudStorageAccount cloudStorageAccount;
		CloudBlobClient cloudBlobClient;
		CloudBlobContainer container;
		CloudBlockBlob blob;
		File downloadedFile;
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudBlobClient = cloudStorageAccount.createCloudBlobClient();
			container = cloudBlobClient.getContainerReference("test");
			for (ListBlobItem blobItem : container.listBlobs()) {				
				String blobName = ((CloudBlob) blobItem).getName();
				System.out.println("Blob name: " + blobName);
				downloadedFile = new File(blobName);
				blob = container.getBlockBlobReference(blobName);
				blob.downloadToFile(downloadedFile.getAbsolutePath());
				System.out.println("Blob with name: " + blobName + " downloaded successfully");
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		} finally {
		}
	}
}
