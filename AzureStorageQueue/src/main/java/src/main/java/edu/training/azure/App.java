package src.main.java.edu.training.azure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.queue.CloudQueue;
import com.microsoft.azure.storage.queue.CloudQueueClient;
import com.microsoft.azure.storage.queue.CloudQueueMessage;

import src.main.java.edu.training.azure.model.Customer;

public class App {
	public static final String connectionString = "enter connection string either from Access keys or generated SAS keys";

	public static void main(String[] args) {
		addMessage();
		viewMessages();
	}

	public static void addMessage() {
		CloudStorageAccount cloudStorageAccount;
		CloudQueueClient cloudQueueClient;
		CloudQueue cloudQueue;
		CloudQueueMessage cloudQueueMessage;
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudQueueClient = cloudStorageAccount.createCloudQueueClient();
			cloudQueue = cloudQueueClient.getQueueReference("customers");
			cloudQueue.createIfNotExists();

			Customer customer = new Customer();
			customer.setName("Test");
			customer.setPhoneNumber(Long.parseLong("9100520873"));
			customer.setEmail("poluru.sairam@gmail.com");
			customer.setAddress("Bangalore - 560100");

			ObjectMapper objectMapper = new ObjectMapper();
			String customerData = objectMapper.writeValueAsString(customer);

			cloudQueueMessage = new CloudQueueMessage(customerData);
			cloudQueue.addMessage(cloudQueueMessage);
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}

	public static void viewMessages() {
		CloudStorageAccount cloudStorageAccount;
		CloudQueueClient cloudQueueClient;
		CloudQueue cloudQueue;
		CloudQueueMessage cloudQueueMessage;
		try {
			cloudStorageAccount = CloudStorageAccount.parse(connectionString);
			cloudQueueClient = cloudStorageAccount.createCloudQueueClient();
			cloudQueue = cloudQueueClient.getQueueReference("customers");
			
			cloudQueue.downloadAttributes();
			long cachedMessageCount = cloudQueue.getApproximateMessageCount();
			System.out.println("Queue message count : " + cachedMessageCount);
			
			for(int i=0; i<cachedMessageCount; i++)
			{
				cloudQueueMessage = cloudQueue.retrieveMessage();
				System.out.println("Queue message : " + cloudQueueMessage.getMessageContentAsString());
			    if (cloudQueueMessage != null)
			    {			        
			    	cloudQueue.deleteMessage(cloudQueueMessage);
			    }
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
}
