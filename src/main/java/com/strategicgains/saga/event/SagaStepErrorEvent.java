package com.strategicgains.saga.event;

import com.strategicgains.saga.Saga;
import com.strategicgains.saga.SagaStep;

public class SagaStepErrorEvent
extends SagaStepEvent
{
	private Exception e;

	public SagaStepErrorEvent(SagaEventType type, Saga saga, SagaStep step, Exception exception)
	{
		super(type, saga, step);
		this.e = exception;
	}

	public Exception getException()
	{
		return e;
	}
}
