package com.strategicgains.saga.ai;

public class ChatConfiguration
{
	private String inferenceUrl;
	private String model;
	private String promptTemplateJson;

	public ChatConfiguration(String inferenceUrl, String model, String promptTemplateJson)
	{
		this.inferenceUrl = inferenceUrl;
		this.model = model;
		this.promptTemplateJson = promptTemplateJson;
	}

	public String getInferenceUrl()
	{
		return inferenceUrl;
	}

	protected void setInferenceUrl(String inferenceUrl)
	{
		this.inferenceUrl = inferenceUrl;
	}

	public String getModel()
	{
		return model;
	}

	protected void setModel(String model)
    {
        this.model = model;
    }

	public String getPromptTemplateJson()
	{
		return promptTemplateJson;
	}

	protected void setPromptTemplateJson(String promptTemplateJson)
	{
		this.promptTemplateJson = promptTemplateJson;
	}
}
