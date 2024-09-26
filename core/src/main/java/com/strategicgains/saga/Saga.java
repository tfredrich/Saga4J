package com.strategicgains.saga;

import static com.strategicgains.saga.event.SagaEvent.Type.SAGA_COMPENSATED;
import static com.strategicgains.saga.event.SagaEvent.Type.SAGA_COMPENSATION_FAILED;
import static com.strategicgains.saga.event.SagaEvent.Type.SAGA_COMPENSATION_STARTED;
import static com.strategicgains.saga.event.SagaEvent.Type.SAGA_STARTED;
import static com.strategicgains.saga.event.SagaEvent.Type.STEP_COMPENSATED;
import static com.strategicgains.saga.event.SagaEvent.Type.STEP_COMPENSATION_FAILED;
import static com.strategicgains.saga.event.SagaEvent.Type.STEP_COMPENSATION_STARTED;
import static com.strategicgains.saga.event.SagaEvent.Type.STEP_COMPLETED;
import static com.strategicgains.saga.event.SagaEvent.Type.STEP_FAILED;
import static com.strategicgains.saga.event.SagaEvent.Type.STEP_STARTED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.strategicgains.saga.builder.SagaBuilder;
import com.strategicgains.saga.event.SagaErrorEvent;
import com.strategicgains.saga.event.SagaEvent;
import com.strategicgains.saga.event.StepErrorEvent;
import com.strategicgains.saga.event.StepEvent;

/**
 * A Saga is a sequence of steps that must be executed in order. If any step fails, the Saga will attempt to rollback
 * all previous steps using compensation.
 */
public class Saga
{
	private List<Step> steps = new ArrayList<>();
	private List<Observer<SagaEvent>> observers;

	public static final SagaBuilder builder()
	{
		return new SagaBuilder();
	}

	public Saga addStep(Step step)
	{
		steps.add(step);
		return this;
	}

	public void execute(ExecutionContext context)
	throws SagaException
	{
		List<Step> executedSteps = new ArrayList<>();

		notify(SAGA_STARTED, this, context);

		for (Step step : steps) try
		{
			executedSteps.add(step);

			notify(STEP_STARTED, this, step, context);
			step.execute(context);
			notify(STEP_COMPLETED, this, step, context);
		}
		catch (Exception e)
		{
			notify(STEP_FAILED, this, step, context, e);
			compensate(context, executedSteps);
			notify(SAGA_COMPENSATED, this, context);
			throw new SagaException("Failed to execute Saga", e);
		}
	}

	public void compensate(ExecutionContext context)
	throws SagaException
	{
		compensate(context, steps);
	}

	protected void compensate(ExecutionContext context, List<Step> executedSteps)
	throws SagaException
	{
		notify(SAGA_COMPENSATION_STARTED, this, context);

		List<Step> reversed = new ArrayList<>(executedSteps);
		Collections.reverse(reversed);
		List<Exception> errors = new ArrayList<>();

		for (Step step : reversed) try
		{
			if (isCompensatable(step))
			{
				notify(STEP_COMPENSATION_STARTED, this, step, context);
				((CompensatableStep) step).compensate(context);
				notify(STEP_COMPENSATED, this, step, context);
			}
		}
		catch (Exception e)
		{
			notify(STEP_COMPENSATION_FAILED, this, step, context, e);
			notify(SAGA_COMPENSATION_FAILED, this, context, e);
			errors.add(e);
		}

		if (!errors.isEmpty())
		{
			throw new SagaException("Failed to compensate Saga", errors);
		}
	}

	public void addObserver(Observer<SagaEvent> observer)
	{
		if (observers == null)
		{
			observers = new ArrayList<>();
		}

		observers.add(observer);
	}

	private boolean isCompensatable(Step step)
	{
		return step instanceof CompensatableStep;
	}

	private boolean hasObservers()
	{
		return observers != null;
	}

	private void notify(SagaEvent event)
	{
		observers.forEach(observer -> observer.onEvent(event));
	}

	private void notify(SagaEvent.Type type, Saga saga, Step step, ExecutionContext context, Exception e)
	{
		if (hasObservers()) notify(new StepErrorEvent(type, saga, step, context, e));		
	}

	private void notify(SagaEvent.Type type, Saga saga, Step step, ExecutionContext context)
	{
		if (hasObservers()) notify(new StepEvent(type, saga, step, context));
	}

	private void notify(SagaEvent.Type type, Saga saga, ExecutionContext context)
	{
		if (hasObservers()) notify(new SagaEvent(type, saga, context));
	}

	private void notify(SagaEvent.Type type, Saga saga, ExecutionContext context, Exception e)
	{
		if (hasObservers()) notify(new SagaErrorEvent(type, saga, context, e));
	}
}
