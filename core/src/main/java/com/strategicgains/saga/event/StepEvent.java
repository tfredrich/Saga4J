package com.strategicgains.saga.event;

import com.strategicgains.saga.SagaContext;
import com.strategicgains.saga.Saga;
import com.strategicgains.saga.Step;

public class StepEvent
extends SagaEvent
{
	private Step step;

	public StepEvent(Type type, Saga saga, Step step, SagaContext context)
	{
		super(type, saga, context);
		this.step = step;
	}

	public Step getStep()
	{
		return step;
	}
}
