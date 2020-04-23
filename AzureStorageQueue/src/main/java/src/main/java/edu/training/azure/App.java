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
			customer.setPhoneNumber(1023456789);
			customer.setEmail("azure@gmail.com");
			customer.setAddress("Bangalore - 560100");

			ObjectMapper objectMapper = new ObjectMapper();
			String customerData = objectMapper.writeValueAsString(customer);

			cloudQueueMessage = new CloudQueueMessage(customerData);
			cloudQueue.addMessage(cloudQueueMessage);
			
			System.out.println("Message added into queue.....");
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
				System.out.println("Queue message : " + cloudQueueMessage.getMessageContentAsString() + " is processed");
			    if (cloudQueueMessage != null)
			    {
			    	System.out.println("Queue message : " + cloudQueueMessage.getMessageContentAsString() + " is deleted");
			    	cloudQueue.deleteMessage(cloudQueueMessage);
			    }
			}
		} catch (Exception e) {
			System.out.println("Exception : " + e.getMessage());
		}
	}
}
