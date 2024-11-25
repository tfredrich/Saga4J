package com.strategicgains.saga.ai;

import org.json.JSONObject;

import com.strategicgains.saga.SagaContext;

public class LmStudioChatStep
extends LanguageModelStep
{
	protected static final String RESPONSE = "lmStudioResponse";

	public LmStudioChatStep()
	{
		this(new LmStudioChatConfig());
	}

	public LmStudioChatStep(LmStudioChatConfig config)
	{
		super(config);
	}

	public LmStudioChatStep withModel(String model)
	{
		withModel(model);
		return this;
	}

	@Override
	public void execute(SagaContext context)
	throws Exception
	{
		super.execute(context);
		JSONObject response = context.getValue(LanguageModelStep.LLM_RESPONSE, JSONObject.class);
		context.setValue(RESPONSE, response.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content"));
	}
}
