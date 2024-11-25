package com.strategicgains.saga.ai;

public class LmStudioChatConfig
extends ChatConfiguration
{
	private static final String DEFAULT_MODEL = "hugging-quants/Llama-3.2-3B-Instruct-Q4_K_M-GGUF/llama-3.2-3b-instruct-q4_k_m.gguf";
	private static final String DEFAULT_INFERENCE_URL = "http://localhost:1234/v1/chat/completions";
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

	public LmStudioChatConfig()
	{
		super(DEFAULT_INFERENCE_URL, DEFAULT_MODEL, CHAT_PROMPT_TEMPLATE);
	}

	public LmStudioChatConfig withModel(String model)
    {
        setModel(model);
        return this;
    }

	public LmStudioChatConfig withInferenceUrl(String inferenceUrl)
    {
        setInferenceUrl(inferenceUrl);
        return this;
    }

	public LmStudioChatConfig withPromptTemplateJson(String promptTemplateJson)
	{
		setPromptTemplateJson(promptTemplateJson);
		return this;
	}
}
