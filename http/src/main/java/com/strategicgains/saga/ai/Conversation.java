package com.strategicgains.saga.ai;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entire context of a conversation with an LLM model.
 */
public class Conversation {
	private String model;
	private List<Message> messages = new ArrayList<>();
	private boolean stream;

	public Conversation()
	{
		super();
	}

	public Conversation(String model)
	{
		this();
		setModel(model);
	}

	public Conversation setModel(String model)
	{
		this.model = model;
		return this;
	}

	public String getModel()
	{
		return model;
	}

	public Conversation shouldStream(boolean value)
	{
		this.stream = value;
		return this;
	}

	public boolean isStream()
	{
		return stream;
	}

	public Conversation withSystem(String prompt)
	{
		messages.add(new SystemMessage(prompt));
		return this;
	}

	public Conversation withUser(String prompt)
	{
		messages.add(new UserMessage(prompt));
		return this;
	}

	public Conversation withAssistant(String prompt)
	{
		messages.add(new AssistantMessage(prompt));
		return this;
	}

	public Message getLastMessage()
	{
//		JDK 21: return messages.getLast();
        return (messages != null && !messages.isEmpty() ? messages.get(messages.size() - 1) : null);
	}

	public class Message
	{
		private String role;
		private String content;

		public Message(String role, String content)
		{
			super();
			this.content = content;
			this.role = role;
		}

		public String getRole()
		{
			return role;
		}

		public String getContent()
		{
			return content;
		}
	}

	public class AssistantMessage
	extends Message
	{
		public static final String ROLE = "assistant";

		public AssistantMessage(String content)
		{
			super(ROLE, content);
		}
	}

	public class SystemMessage
	extends Message
	{
		public static final String ROLE = "system";

		public SystemMessage(String content)
		{
			super(ROLE, content);
		}
	}

	public class UserMessage
	extends Message
	{
		public static final String ROLE = "user";

		public UserMessage(String content)
		{
			super(ROLE, content);
		}
	}
}
