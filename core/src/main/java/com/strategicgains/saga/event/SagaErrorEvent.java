package com.strategicgains.saga.event;

import com.strategicgains.saga.SagaContext;
import com.strategicgains.saga.Saga;

public class SagaErrorEvent
extends SagaEvent
{
	private Exception e;

	public SagaErrorEvent(Type type, Saga saga, SagaContext context, Exception exception)
	{
		super(type, saga, context);
		this.e = exception;
	}

	public Exception getException()
	{
		return e;
	}
}
