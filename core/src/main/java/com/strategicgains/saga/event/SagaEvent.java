package com.strategicgains.saga.event;

import com.strategicgains.saga.ExecutionContext;
import com.strategicgains.saga.Saga;

public class SagaEvent
{
	private SagaEventType type;
	private Saga saga;
	private ExecutionContext context;

	public SagaEvent(SagaEventType type, Saga saga, ExecutionContext context)
	{
		super();
		this.saga = saga;
		this.type = type;
		this.context = context;
	}

	public SagaEventType getType()
	{
		return type;
	}

	public Saga getSaga()
	{
		return saga;
	}

	public ExecutionContext getContext()
	{
		return context;
	}
}
