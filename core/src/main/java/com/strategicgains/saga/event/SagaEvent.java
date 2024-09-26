package com.strategicgains.saga.event;

import com.strategicgains.saga.Saga;

public class SagaEvent
{
	private SagaEventType type;
	private Saga saga;

	public SagaEvent(SagaEventType type, Saga saga)
	{
		super();
		this.saga = saga;
		this.type = type;
	}

	public SagaEventType getType()
	{
		return type;
	}

	public Saga getSaga()
	{
		return saga;
	}
}
