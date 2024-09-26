package com.strategicgains.saga.event;

public class ContextUpdatedEvent
extends ContextEvent
{
	private Object before;

	public <T> ContextUpdatedEvent(String key, T value, T before)
	{
		super(ContextEventType.CONTEXT_UPDATED, key, value);
		this.before = before;
	}

	public Object getBefore()
	{
		return before;
	}

	public <T> T getBefore(Class<T> type)
	{
		return type.cast(before);
	}
}
