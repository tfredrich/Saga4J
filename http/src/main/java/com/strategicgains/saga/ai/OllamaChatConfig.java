package com.strategicgains.saga.ai;

public class OllamaChatConfig
extends ChatConfiguration
{
	private static final String DEFAULT_INFERENCE_URL = "http://localhost:11434/api/chat";

	public OllamaChatConfig()
	{
		super(DEFAULT_INFERENCE_URL);
	}

	public OllamaChatConfig withInferenceUrl(String inferenceUrl)
    {
        setInferenceUrl(inferenceUrl);
        return this;
    }
}
