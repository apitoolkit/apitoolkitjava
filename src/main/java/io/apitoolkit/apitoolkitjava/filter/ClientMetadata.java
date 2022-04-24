package io.apitoolkit.apitoolkitjava.filter;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;

// TODO: figure this out later
public class ClientMetadata {
  @JsonAlias("project_id")
  public String project_id;
  @JsonAlias("pubsub_project_id")
  private String pubsubProjectId;
  public String getProject_id() {
    return project_id;
  }
  public void setProject_id(String project_id) {
    this.project_id = project_id;
  }
  public String getPubsubProjectId() {
    return pubsubProjectId;
  }
  public void setPubsubProjectId(String pubsubProjectId) {
    this.pubsubProjectId = pubsubProjectId;
  }
  public String getTopicID() {
    return topicID;
  }
  public void setTopicID(String topicID) {
    this.topicID = topicID;
  }
  public Map<String, String> getPubsubPushServiceAccount() {
    return pubsubPushServiceAccount;
  }
  public void setPubsubPushServiceAccount(Map<String, String> pubsubPushServiceAccount) {
    this.pubsubPushServiceAccount = pubsubPushServiceAccount;
  }
  @JsonAlias("topic_id")
  private String topicID;
  @JsonAlias("pubsub_push_service_account")
  private Map<String, String> pubsubPushServiceAccount ;
}
