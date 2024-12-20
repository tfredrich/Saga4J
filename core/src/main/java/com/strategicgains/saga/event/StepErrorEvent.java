package com.strategicgains.saga.event;

import com.strategicgains.saga.SagaContext;
import com.strategicgains.saga.Saga;
import com.strategicgains.saga.Step;

public class StepErrorEvent
extends StepEvent
{
	private Exception e;

	public StepErrorEvent(Type type, Saga saga, Step step, SagaContext context, Exception exception)
	{
		super(type, saga, step, context);
		this.e = exception;
	}

	public Exception getException()
	{
		return e;
	}
}
