package com.strategicgains.saga.ai;

public class OllamaChatConfig
extends ChatConfiguration
{
	private static final String DEFAULT_MODEL = "llama3.2";
	private static final String DEFAULT_INFERENCE_URL = "http://localhost:11434/api/chat";
	private static final String CHAT_PROMPT_TEMPLATE = "{"
		+ "  \"model\": \"{{model}}\","
		+ "  \"messages\": ["
		+ "    {"
		+ "      \"role\": \"user\","
		+ "      \"content\": \"{{prompt}}\""
		+ "    }"
		+ "  ],"
		+ "  \"stream\": false"
		+ "}";

	public OllamaChatConfig()
	{
		super(DEFAULT_INFERENCE_URL, DEFAULT_MODEL, CHAT_PROMPT_TEMPLATE);
	}

	public OllamaChatConfig withModel(String model)
    {
        setModel(model);
        return this;
    }

	public OllamaChatConfig withInferenceUrl(String inferenceUrl)
    {
        setInferenceUrl(inferenceUrl);
        return this;
    }

	public OllamaChatConfig withPromptTemplateJson(String promptTemplateJson)
	{
		setPromptTemplateJson(promptTemplateJson);
		return this;
	}
}
