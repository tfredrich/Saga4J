package com.strategicgains.saga;

import static com.strategicgains.saga.event.SagaEventType.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.strategicgains.saga.event.SagaErrorEvent;
import com.strategicgains.saga.event.SagaEvent;
import com.strategicgains.saga.event.SagaEventType;
import com.strategicgains.saga.event.SagaStepErrorEvent;
import com.strategicgains.saga.event.SagaStepEvent;

/**
 * A Saga is a sequence of steps that must be executed in order. If any step fails, the Saga will attempt to rollback
 * all previous steps using compensation.
 */
public class Saga
	implements Observable<SagaEvent>
{
	private List<SagaStep> steps = new ArrayList<>();
	private List<Observer<SagaEvent>> observers;

	public Saga addStep(SagaStep step)
	{
		steps.add(step);
		return this;
	}

	public void execute(SagaContext context)
	{
		List<SagaStep> executedSteps = new ArrayList<>();

		notify(SAGA_STARTED, this);

		for (SagaStep step : steps) try
		{
			executedSteps.add(step);

			notify(STEP_STARTED, this, step);
			step.execute(context);
			notify(STEP_COMPLETED, this, step);
		}
		catch (Exception e)
		{
			notify(STEP_FAILED, this, step, e);
			compensate(context, executedSteps);
			notify(SAGA_COMPENSATED, this);
		}
	}

	public void compensate(SagaContext context)
	{
		compensate(context, steps);
	}

	protected void compensate(SagaContext context, List<SagaStep> executedSteps)
	{
		notify(SAGA_COMPENSATION_STARTED, this);

		List<SagaStep> reversed = new ArrayList<>(executedSteps);
		Collections.reverse(reversed);

		for (SagaStep step : reversed) try
		{
			notify(STEP_COMPENSATION_STARTED, this, step);
			step.compensate(context);
			notify(STEP_COMPENSATED, this, step);
		}
		catch (Exception e)
		{
			notify(STEP_COMPENSATION_FAILED, this, step, e);
			notify(SAGA_COMPENSATION_FAILED, this, e);
		}
	}

	@Override
	public void addObserver(Observer<SagaEvent> observer)
	{
		if (observers == null)
		{
			observers = new ArrayList<>();
		}

		observers.add(observer);
	}

	private boolean hasObservers()
	{
		return observers != null;
	}

	private void notify(SagaEvent event)
	{
		observers.forEach(observer -> observer.onEvent(event));
	}

	private void notify(SagaEventType type, Saga saga, SagaStep step, Exception e)
	{
		if (hasObservers()) notify(new SagaStepErrorEvent(type, saga, step, e));		
	}

	private void notify(SagaEventType type, Saga saga, SagaStep step)
	{
		if (hasObservers()) notify(new SagaStepEvent(type, saga, step));
	}

	private void notify(SagaEventType type, Saga saga)
	{
		if (hasObservers()) notify(new SagaEvent(type, saga));
	}

	private void notify(SagaEventType type, Saga saga, Exception e)
	{
		if (hasObservers()) notify(new SagaErrorEvent(type, saga, e));
	}
}
