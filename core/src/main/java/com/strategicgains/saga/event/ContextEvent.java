package com.strategicgains.saga.event;

public class ContextEvent
{
	private ContextEventType type;
	private String key;
	private Object value;

	protected <T> ContextEvent(ContextEventType type, String key, T value)
	{
		super();
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public ContextEventType getType()
	{
		return type;
	}

	public String getKey()
	{
		return key;
	}

	public Object getValue()
	{
		return value;
	}

	public <T> T getValue(Class<T> type)
	{
		return type.cast(value);
	}
}
