package com.strategicgains.saga.ai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.strategicgains.saga.SagaContext;

import kong.unirest.core.Unirest;
import kong.unirest.modules.gson.GsonObjectMapper;

class LanguageModelStepTest
{
	private static final String SYSTEM_PROMPT = "Always answer as a pirate.";
	private static final String USER_PROMPT = "Hello, I'm Jivin' Jake. Please introduce yourself.";
	private static final String LM_STUDIO_MODEL = "mlx-community/Llama-3.2-3B-Instruct-4bit";
	private static final String OLLAMA_MODEL = "llama3.2";

	@BeforeAll
	static void beforeAll()
	{
		Unirest.config().setObjectMapper(new GsonObjectMapper());
	}

	@Test
	void testOllama()
	throws Exception
	{
		LanguageModelStep step = new OllamaChatStep();
		SagaContext context = new SagaContext();
		context.setValue(LanguageModelStep.CONVERSATION, new Conversation().setModel(OLLAMA_MODEL).withSystem(SYSTEM_PROMPT).withUser(USER_PROMPT));
		step.execute(context);
		Conversation conversation = context.getValue(LanguageModelStep.CONVERSATION, Conversation.class);
		assertNotNull(conversation);
		System.out.println("Ollama response: " + conversation.getLastMessage().getContent());
	}

	@Test
	void testLmStudio()
	throws Exception
	{
		LanguageModelStep step = new LmStudioChatStep();
		SagaContext context = new SagaContext();
		context.setValue(LanguageModelStep.CONVERSATION, new Conversation().setModel(LM_STUDIO_MODEL).withSystem(SYSTEM_PROMPT).withUser(USER_PROMPT));
		step.execute(context);
		Conversation conversation = context.getValue(LanguageModelStep.CONVERSATION, Conversation.class);
		assertNotNull(conversation);
		System.out.println("LM Studio response: " + conversation.getLastMessage().getContent());
	}
}
