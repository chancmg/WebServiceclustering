package com.example.demo;

import java.util.List;

public class ResponseDTO {
	private String responseCode;
	private String responseMessage;
	private List<ResultVO> result;
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public List<ResultVO> getResult() {
		return result;
	}
	public void setResult(List<ResultVO> result) {
		this.result = result;
	}

}
