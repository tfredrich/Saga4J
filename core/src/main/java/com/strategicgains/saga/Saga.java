package com.strategicgains.saga;

import static com.strategicgains.saga.event.SagaEventType.SAGA_COMPENSATED;
import static com.strategicgains.saga.event.SagaEventType.SAGA_COMPENSATION_FAILED;
import static com.strategicgains.saga.event.SagaEventType.SAGA_COMPENSATION_STARTED;
import static com.strategicgains.saga.event.SagaEventType.SAGA_STARTED;
import static com.strategicgains.saga.event.SagaEventType.STEP_COMPENSATED;
import static com.strategicgains.saga.event.SagaEventType.STEP_COMPENSATION_FAILED;
import static com.strategicgains.saga.event.SagaEventType.STEP_COMPENSATION_STARTED;
import static com.strategicgains.saga.event.SagaEventType.STEP_COMPLETED;
import static com.strategicgains.saga.event.SagaEventType.STEP_FAILED;
import static com.strategicgains.saga.event.SagaEventType.STEP_STARTED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.strategicgains.saga.builder.SagaBuilder;
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
implements Observable<SagaEvent>, Compensatable<SagaContext>
{
	private List<SagaStep> steps = new ArrayList<>();
	private List<Observer<SagaEvent>> observers;

	public static final SagaBuilder builder()
	{
		return new SagaBuilder();
	}

	public Saga addStep(SagaStep step)
	{
		steps.add(step);
		return this;
	}

	public void execute(SagaContext context)
	throws SagaException
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
			throw new SagaException("Failed to execute Saga", e);
		}
	}

	@Override
	public void compensate(SagaContext context)
	throws SagaException
	{
		compensate(context, steps);
	}

	protected void compensate(SagaContext context, List<SagaStep> executedSteps)
	throws SagaException
	{
		notify(SAGA_COMPENSATION_STARTED, this);

		List<SagaStep> reversed = new ArrayList<>(executedSteps);
		Collections.reverse(reversed);
		List<Exception> errors = new ArrayList<>();

		for (SagaStep step : reversed) try
		{
			if (isCompensatable(step))
			{
				notify(STEP_COMPENSATION_STARTED, this, step);
				((CompensatableStep) step).compensate(context);
				notify(STEP_COMPENSATED, this, step);
			}
		}
		catch (Exception e)
		{
			notify(STEP_COMPENSATION_FAILED, this, step, e);
			notify(SAGA_COMPENSATION_FAILED, this, e);
			errors.add(e);
		}

		if (!errors.isEmpty())
		{
			throw new SagaException("Failed to compensate Saga", errors);
		}
	}
	private boolean isCompensatable(SagaStep step)
	{
		return step instanceof CompensatableStep;
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
