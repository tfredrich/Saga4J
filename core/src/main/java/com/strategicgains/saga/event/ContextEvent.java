package com.strategicgains.saga.event;

public class ContextEvent
{
	private Type type;
	private String key;
	private Object value;

	protected <T> ContextEvent(Type type, String key, T value)
	{
		super();
		this.type = type;
		this.key = key;
		this.value = value;
	}

	public Type getType()
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

	public enum Type
	{
		CONTEXT_CREATED,
		CONTEXT_UPDATED
	}
}
