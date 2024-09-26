package com.strategicgains.saga.event;

import com.strategicgains.saga.Saga;
import com.strategicgains.saga.SagaStep;

public class SagaStepEvent
extends SagaEvent
{
	private SagaStep step;

	public SagaStepEvent(SagaEventType type, Saga saga, SagaStep step)
	{
		super(type, saga);
		this.step = step;
	}

	public SagaStep getStep()
	{
		return step;
	}
}
