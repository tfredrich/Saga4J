package com.strategicgains.saga.event;

import com.strategicgains.saga.Saga;
import com.strategicgains.saga.SagaStep;

public class StepStartedEvent
extends SagaStepEvent
{
	public StepStartedEvent(Saga saga, SagaStep step)
	{
		super(SagaEventType.STEP_STARTED, saga, step);
	}
}
