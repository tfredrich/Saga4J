package com.strategicgains.saga.ai;

import com.strategicgains.saga.SagaContext;

import kong.unirest.core.json.JSONObject;

public class OllamaChatStep
extends LanguageModelStep
{
	public OllamaChatStep()
	{
		super(new OllamaChatConfig());
	}

	public OllamaChatStep(OllamaChatConfig config)
	{
		super(config);
	}

	public OllamaChatStep withModel(String model)
	{
		withModel(model);
		return this;
	}

	@Override
	protected void updateConversation(SagaContext context, JSONObject response)
	{
		Conversation conversation = context.getValue(LanguageModelStep.CONVERSATION, Conversation.class);
		conversation.withAssistant(response.getJSONObject("message").getString("content"));
	}
}
