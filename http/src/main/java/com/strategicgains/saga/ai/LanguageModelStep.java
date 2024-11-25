package com.strategicgains.saga.ai;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.strategicgains.saga.SagaContext;
import com.strategicgains.saga.http.AbstractHttpStep;

public class LanguageModelStep
extends AbstractHttpStep
{
	protected static final String LLM_RESPONSE = "llmResponse";
	private static final String PROMPT_REGEX = "{{prompt}}";
	private static final String MODEL_REGEX = "{{model}}";
	private ChatConfiguration config;

	protected LanguageModelStep(ChatConfiguration config)
	{
		super(config.getInferenceUrl());
		this.config = config;
	}

	protected ChatConfiguration getConfig()
	{
		return config;
	}

	@Override
	public void execute(SagaContext context) throws Exception
	{
		String prompt = context.getValue("prompt", String.class);
		assert prompt != null;
		String body = config.getPromptTemplateJson()
			.replace(MODEL_REGEX, config.getModel())
			.replace(PROMPT_REGEX, prompt);
		HttpResponse<JsonNode> response = post(config.getInferenceUrl(), body).asJson();

		if (isSuccessful(response))
		{
			context.setValue(LLM_RESPONSE, response.getBody().getObject());
		}
		else
		{
			throw new Exception("Failed to get response from language model: " + response.getBody().toString());
		}
	}
}
