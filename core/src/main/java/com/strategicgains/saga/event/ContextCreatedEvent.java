package com.strategicgains.saga.event;

public class ContextCreatedEvent
extends ContextEvent
{
	public <T> ContextCreatedEvent(String key, T value)
	{
		super(Type.CONTEXT_CREATED, key, value);
	}
}
