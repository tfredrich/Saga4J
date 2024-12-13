package com.strategicgains.saga.ai;

import com.strategicgains.saga.SagaContext;
import com.strategicgains.saga.http.AbstractHttpStep;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.json.JSONObject;

public abstract class LanguageModelStep
extends AbstractHttpStep
{
	protected static final String CONVERSATION = "conversation";
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
		Conversation conversation = context.getValue(CONVERSATION, Conversation.class);
		assert conversation != null;
		HttpResponse<JsonNode> response = post(config.getInferenceUrl(), conversation).asJson();

		if (isSuccessful(response))
		{
			updateConversation(context, response.getBody().getObject());
		}
		else
		{
			throw new Exception("Failed to get response from language model: " + response.getBody().toString());
		}
	}

	protected abstract void updateConversation(SagaContext context, JSONObject response);
}
