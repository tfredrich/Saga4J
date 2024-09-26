package com.strategicgains.saga.event;

import com.strategicgains.saga.Saga;

public class SagaErrorEvent
extends SagaEvent
{
	private Exception e;

	public SagaErrorEvent(SagaEventType type, Saga saga, Exception exception)
	{
		super(type, saga);
		this.e = exception;
	}

	public Exception getException()
	{
		return e;
	}
}
