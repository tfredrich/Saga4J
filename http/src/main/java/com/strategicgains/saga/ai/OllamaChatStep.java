package com.strategicgains.saga.ai;

import org.json.JSONObject;

import com.strategicgains.saga.SagaContext;

public class OllamaChatStep
extends LanguageModelStep
{
	protected static final String RESPONSE = "ollamaResponse";

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
	public void execute(SagaContext context)
	throws Exception
	{
		super.execute(context);
		JSONObject response = context.getValue(LanguageModelStep.LLM_RESPONSE, JSONObject.class);
		context.setValue(RESPONSE, response.getJSONObject("message").getString("content"));
	}
}
