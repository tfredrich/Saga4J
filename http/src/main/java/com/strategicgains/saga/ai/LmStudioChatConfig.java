package com.strategicgains.saga.ai;

public class LmStudioChatConfig
extends ChatConfiguration
{
	private static final String DEFAULT_INFERENCE_URL = "http://localhost:1234/v1/chat/completions";

	public LmStudioChatConfig()
	{
		super(DEFAULT_INFERENCE_URL);
	}

	public LmStudioChatConfig withInferenceUrl(String inferenceUrl)
    {
        setInferenceUrl(inferenceUrl);
        return this;
    }
}
