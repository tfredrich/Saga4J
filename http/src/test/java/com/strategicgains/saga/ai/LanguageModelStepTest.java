package com.strategicgains.saga.ai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.strategicgains.saga.SagaContext;

class LanguageModelStepTest
{
	private static final String SAMPLE_PROMPT = "Hello, I'm Todd. Please introduce yourself.";
	private static final String PIRATE_PROMPT_TEMPLATE = "{"
		+ "  \"model\": \"{{model}}\","
		+ "  \"messages\": ["
		+ "    {"
		+ "      \"role\": \"system\","
		+ "      \"content\": \"Always answer as a pirate.\""
		+ "    },"
		+ "    {"
		+ "      \"role\": \"user\","
		+ "      \"content\": \"{{prompt}}\""
		+ "    }"
		+ "  ],"
		+ "  \"stream\": false"
		+ "}";


	@Test
	void testOllama()
	throws Exception
	{
		LanguageModelStep step = new OllamaChatStep(new OllamaChatConfig().withPromptTemplateJson(PIRATE_PROMPT_TEMPLATE));
		SagaContext context = new SagaContext();
		context.setValue("prompt", SAMPLE_PROMPT);
		step.execute(context);
		assertNotNull(context.getValue(OllamaChatStep.RESPONSE));
		System.out.println(context.getValue(OllamaChatStep.RESPONSE));
	}

	@Test
	void testLmStudio()
	throws Exception
	{
		LanguageModelStep step = new LmStudioChatStep(new LmStudioChatConfig()
			.withPromptTemplateJson(PIRATE_PROMPT_TEMPLATE)
			.withModel("mlx-community/Llama-3.2-3B-Instruct-4bit")
		);
		SagaContext context = new SagaContext();
		context.setValue("prompt", SAMPLE_PROMPT);
		step.execute(context);
		assertNotNull(context.getValue(LmStudioChatStep.RESPONSE));
		System.out.println(context.getValue(LmStudioChatStep.RESPONSE));
	}
}
