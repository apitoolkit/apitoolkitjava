package io.apitoolkit.apitoolkitjava.filter;

import java.util.List;
import java.util.Map;

public class ApitoolkitPayload {
  /// nano seconds
  private long Duration;
  // domain name
  private String Host;
  private String Method;
  // localhost/product/:product_id/ {product_id, 1234-1234}
  private Map<String, String> PathParams;
  // from apitoolkit using the api key
  private String ProjectID; 
  private Integer ProtoMajor = 1;
  private Integer ProtoMinor = 1;
  // locahost/product?product_id=1234-1234
  private Map<String, List<String>> QueryParams; 
  // complete url with param values
  private String RawURL;
  // referer from the request
  private String Referer;
  // request body
  private String RequestBody;
  private Map<String, List<String>> RequestHeaders;
  private String ResponseBody;
  private Map<String, List<String>> ResponseHeaders;
  private String SdkType = "JavaSpringBoot"; // TODO: replace with created
  private Integer StatusCode;
  private String Timestamp;
  private String URLPath;
  
  public ApitoolkitPayload(Integer duration, String host, String method, Map<String, String> pathParams,
      String projectID, Integer protoMajor, Integer protoMinor, Map<String, List<String>> queryParams, String rawURL,
      String referer, String requestBody, Map<String, List<String>> requestHeaders, String responseBody,
      Map<String, List<String>> responseHeaders, String sdkType, Integer statusCode, String timestamp, String uRLPath) {
    Duration = duration;
    Host = host;
    Method = method;
    PathParams = pathParams;
    ProjectID = projectID;
    ProtoMajor = protoMajor;
    ProtoMinor = protoMinor;
    QueryParams = queryParams;
    RawURL = rawURL;
    Referer = referer;
    RequestBody = requestBody;
    RequestHeaders = requestHeaders;
    ResponseBody = responseBody;
    ResponseHeaders = responseHeaders;
    SdkType = sdkType;
    StatusCode = statusCode;
    Timestamp = timestamp;
    URLPath = uRLPath;
  }

  @Override
  public String toString() {
    return "ApitoolkitPayload [Duration=" + Duration + ", Host=" + Host + ", Method=" + Method + ", PathParams="
        + PathParams + ", ProjectID=" + ProjectID + ", ProtoMajor=" + ProtoMajor + ", ProtoMinor=" + ProtoMinor
        + ", QueryParams=" + QueryParams + ", RawURL=" + RawURL + ", Referer=" + Referer + ", RequestBody="
        + RequestBody + ", RequestHeaders=" + RequestHeaders + ", ResponseBody=" + ResponseBody + ", ResponseHeaders="
        + ResponseHeaders + ", SdkType=" + SdkType + ", StatusCode=" + StatusCode + ", Timestamp=" + Timestamp
        + ", URLPath=" + URLPath + "]";
  }

  public ApitoolkitPayload() {
  }

  public long getDuration() {
    return Duration;
  }

  public void setDuration(long duration) {
    Duration = duration;
  }

  public String getHost() {
    return Host;
  }

  public void setHost(String host) {
    Host = host;
  }

  public String getMethod() {
    return Method;
  }

  public void setMethod(String method) {
    Method = method;
  }

  public Map<String, String> getPathParams() {
    return PathParams;
  }

  public void setPathParams(Map<String, String> pathParams) {
    PathParams = pathParams;
  }

  public String getProjectID() {
    return ProjectID;
  }

  public void setProjectID(String projectID) {
    ProjectID = projectID;
  }

  public Integer getProtoMajor() {
    return ProtoMajor;
  }

  public void setProtoMajor(Integer protoMajor) {
    ProtoMajor = protoMajor;
  }

  public Integer getProtoMinor() {
    return ProtoMinor;
  }

  public void setProtoMinor(Integer protoMinor) {
    ProtoMinor = protoMinor;
  }

  public Map<String, List<String>> getQueryParams() {
    return QueryParams;
  }

  public void setQueryParams(Map<String, List<String>> queryParams) {
    QueryParams = queryParams;
  }

  public String getRawURL() {
    return RawURL;
  }

  public void setRawURL(String rawURL) {
    RawURL = rawURL;
  }

  public String getReferer() {
    return Referer;
  }

  public void setReferer(String referer) {
    Referer = referer;
  }

  public String getRequestBody() {
    return RequestBody;
  }

  public void setRequestBody(String requestBody) {
    RequestBody = requestBody;
  }

  public Map<String, List<String>> getRequestHeaders() {
    return RequestHeaders;
  }

  public void setRequestHeaders(Map<String, List<String>> requestHeaders) {
    RequestHeaders = requestHeaders;
  }

  public String getResponseBody() {
    return ResponseBody;
  }

  public void setResponseBody(String responseBody) {
    ResponseBody = responseBody;
  }

  public Map<String, List<String>> getResponseHeaders() {
    return ResponseHeaders;
  }

  public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
    ResponseHeaders = responseHeaders;
  }

  public String getSdkType() {
    return SdkType;
  }

  public void setSdkType(String sdkType) {
    SdkType = sdkType;
  }

  public Integer getStatusCode() {
    return StatusCode;
  }

  public void setStatusCode(Integer statusCode) {
    StatusCode = statusCode;
  }

  public String getTimestamp() {
    return Timestamp;
  }

  public void setTimestamp(String timestamp) {
    Timestamp = timestamp;
  }

  public String getURLPath() {
    return URLPath;
  }

  public void setURLPath(String uRLPath) {
    URLPath = uRLPath;
  }

  
}
