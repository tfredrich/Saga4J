package com.strategicgains.saga.ai;

public class ChatConfiguration
{
	private String inferenceUrl;

	public ChatConfiguration(String inferenceUrl)
	{
		this.inferenceUrl = inferenceUrl;
	}

	public String getInferenceUrl()
	{
		return inferenceUrl;
	}

	protected void setInferenceUrl(String inferenceUrl)
	{
		this.inferenceUrl = inferenceUrl;
	}
}
