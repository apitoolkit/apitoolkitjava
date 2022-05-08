package io.apitoolkit.apitoolkitjava.filter;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;

// TODO: figure this out later
public class ClientMetadata {
  @JsonAlias("project_id")
  public String project_id;

  @JsonAlias("pubsub_project_id")
  private String pubsub_project_id;

  @JsonAlias("topic_id")
  private String topic_id;

  @JsonAlias("pubsub_push_service_account")
  private Map<String, String> pubsub_push_service_account;


  public String getProject_id() {
    return project_id;
  }

  public void setProject_id(String project_id) {
    this.project_id = project_id;
  }

  public String getPubsub_project_id() {
    return pubsub_project_id;
  }

  public void setPubsub_project_id(String pubsubProjectId) {
    this.pubsub_project_id = pubsubProjectId;
  }

  public String getTopic_id() {
    return topic_id;
  }

  public void setTopic_id(String topicID) {
    this.topic_id = topicID;
  }

  public Map<String, String> getPubsub_push_service_account() {
    return pubsub_push_service_account;
  }

  public void setPubsub_push_service_account(Map<String, String> pubsubPushServiceAccount) {
    this.pubsub_push_service_account = pubsubPushServiceAccount;
  }
}
