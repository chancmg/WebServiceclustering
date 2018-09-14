package com.example.demo;

public class ServiceData {
	
	//ResponseTime	Availability	Throughput	Successability	Reliability	Compliance	BestPractices	Latency	Documentation	cluster
	//ResponseTime	Availability	Throughput	Successability	Reliability	Compliance	BestPractices	Latency	Documentation	ServiceName	WSDLAddress

private String responseTime;
private String availability;
private String throughput;
private String successability;
private String reliability;
private String compliance;
private String bestPractices;
private String latency;
private String documentation;
private String serviceName;
private String WSDLAddress;
public String getResponseTime() {
	return responseTime;
}
public void setResponseTime(String responseTime) {
	this.responseTime = responseTime;
}
public String getAvailability() {
	return availability;
}
public void setAvailability(String availability) {
	this.availability = availability;
}
public String getThroughput() {
	return throughput;
}
public void setThroughput(String throughput) {
	this.throughput = throughput;
}
public String getSuccessability() {
	return successability;
}
public void setSuccessability(String successability) {
	this.successability = successability;
}
public String getReliability() {
	return reliability;
}
public void setReliability(String reliability) {
	this.reliability = reliability;
}
public String getCompliance() {
	return compliance;
}
public void setCompliance(String compliance) {
	this.compliance = compliance;
}
public String getBestPractices() {
	return bestPractices;
}
public void setBestPractices(String bestPractices) {
	this.bestPractices = bestPractices;
}
public String getLatency() {
	return latency;
}
public void setLatency(String latency) {
	this.latency = latency;
}
public String getDocumentation() {
	return documentation;
}
public void setDocumentation(String documentation) {
	this.documentation = documentation;
}
public String getServiceName() {
	return serviceName;
}
public void setServiceName(String serviceName) {
	this.serviceName = serviceName;
}
public String getWSDLAddress() {
	return WSDLAddress;
}
public void setWSDLAddress(String wSDLAddress) {
	WSDLAddress = wSDLAddress;
}

}
