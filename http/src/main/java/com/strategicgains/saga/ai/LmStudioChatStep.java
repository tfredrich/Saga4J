package com.strategicgains.saga.ai;

import com.strategicgains.saga.SagaContext;

import kong.unirest.core.json.JSONObject;

public class LmStudioChatStep
extends LanguageModelStep
{
	public LmStudioChatStep()
	{
		this(new LmStudioChatConfig());
	}

	public LmStudioChatStep(LmStudioChatConfig config)
	{
		super(config);
	}

	@Override
	protected void updateConversation(SagaContext context, JSONObject response)
	{
		Conversation conversation = context.getValue(LanguageModelStep.CONVERSATION, Conversation.class);
        conversation.withAssistant(response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
	}
}
