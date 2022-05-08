package io.apitoolkit.apitoolkitjava.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Component
public class PubSub {

  private ClientMetadata metadata = new ClientMetadata();
  private static String projectId = "";
  private static String topicId = "";
  private static String APITOOLKIT_API_KEY = "";

  public PubSub(@Value("${apitoolkit.apiKey}") String apiKey) {
    APITOOLKIT_API_KEY = apiKey;

    this.getClientMetadata();

    projectId = metadata.getProject_id();
    topicId = metadata.getTopic_id();
    System.out.println("data: " + metadata.getPubsub_push_service_account());
  }

  public String getProjectID() {
    return this.projectId;
  }

  public String getApiToolKitKey() {
    return this.APITOOLKIT_API_KEY;
  }

  public void getClientMetadata() {
    // TODO: this route should be configurable (add to properties)
    String apitoolkitProductRoute = "https://app.apitoolkit.io/api/client_metadata";
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + APITOOLKIT_API_KEY);

    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<Object> response = restTemplate.exchange(apitoolkitProductRoute, HttpMethod.GET, requestEntity,
        Object.class);

    ModelMapper mapper = new ModelMapper();

    metadata = mapper.map(response.getBody(), metadata.getClass());
  }

  public void publishWithErrorHandler(String message) throws IOException, InterruptedException {
    System.out.println("projectid: " + metadata.getPubsub_project_id() + " topicID: " + topicId);
    TopicName topicName = TopicName.of(metadata.getPubsub_project_id(), topicId);
    Publisher publisher = null;

    try {
      // TODO: refactor initialization of publisher
      // Create a publisher instance with default settings bound to the topic

      var sa = metadata.getPubsub_push_service_account();
      ObjectMapper ob = new ObjectMapper();
      var json = ob.writeValueAsString(sa);
      InputStream stream = new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8")));

      publisher = Publisher.newBuilder(topicName)
          .setCredentialsProvider(FixedCredentialsProvider.create(ServiceAccountCredentials.fromStream(stream)))
          .build();

      // List<String> messages = Arrays.asList(message);

      // for (final String message : messages) {
      ByteString data = ByteString.copyFromUtf8(message);
      PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

      // Once published, returns a server-assigned message id (unique within the
      // topic)
      ApiFuture<String> future = publisher.publish(pubsubMessage);

      // Add an asynchronous callback to handle success / failure
      ApiFutures.addCallback(
          future,
          new ApiFutureCallback<String>() {

            @Override
            public void onFailure(Throwable throwable) {
              if (throwable instanceof ApiException) {
                ApiException apiException = ((ApiException) throwable);
                // details on the API exception
                System.out.println(apiException.getStatusCode().getCode());
                System.out.println(apiException.isRetryable());
              }
              System.out.println("Error publishing message : " + message);
            }

            @Override
            public void onSuccess(String messageId) {
              // Once published, returns server-assigned message ids (unique within the topic)
              System.out.println("Published message ID: " + messageId);
            }
          },
          MoreExecutors.directExecutor());
      // }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("completed sending message");
      if (publisher != null) {
        // When finished with the publisher, shutdown to free up resources.
        publisher.shutdown();
        publisher.awaitTermination(1, TimeUnit.MINUTES);
      }
    }
  }
}