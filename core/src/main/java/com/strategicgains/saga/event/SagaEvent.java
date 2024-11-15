package com.strategicgains.saga.event;

import com.strategicgains.saga.SagaContext;
import com.strategicgains.saga.Saga;

public class SagaEvent
{
	private Type type;
	private Saga saga;
	private SagaContext context;

	public SagaEvent(Type type, Saga saga, SagaContext context)
	{
		super();
		this.saga = saga;
		this.type = type;
		this.context = context;
	}

	public Type getType()
	{
		return type;
	}

	public Saga getSaga()
	{
		return saga;
	}

	public SagaContext getContext()
	{
		return context;
	}

	public enum Type
	{
		SAGA_STARTED,
		SAGA_FAILED,
		SAGA_COMPENSATION_STARTED,
		SAGA_COMPENSATION_FAILED,
		SAGA_COMPENSATED,
		SAGA_COMPLETED,

		STEP_STARTED,
		STEP_COMPLETED,
		STEP_FAILED,
		STEP_COMPENSATION_STARTED,
		STEP_COMPENSATED,
		STEP_COMPENSATION_FAILED
	}
}
