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
implements CompensatableStep
{
	private List<Step> steps = new ArrayList<>();
	private List<Observer<SagaEvent>> observers;
	private boolean compensated = false;

	/**
	 * Create a new SagaBuilder instance to build a new Saga.
	 * 
	 * @return a new SagaBuilder instance.
	 */
	public static final SagaBuilder builder()
	{
		return new SagaBuilder();
	}

	/**
	 * Add a step to the Saga.
	 * 
	 * @param step a Step instance to add to the Saga.
	 * @return this Saga instance.
	 */
	public Saga addStep(Step step)
	{
		steps.add(step);
		return this;
	}

	/**
	 * Execute the Saga, which will execute each step in order. If any step fails, the Saga will attempt to rollback all
	 * previous steps using compensation.
	 * 
	 * @param context an ExecutionContext instance to pass to each step.
	 * @throws SagaException if any step fails or if compensation fails.
	 */
	@Override
	public void execute(SagaContext context)
	throws SagaException
	{
		List<Step> executedSteps = new ArrayList<>();

		notify(SAGA_STARTED, this, context);

		for (Step step : steps) try
		{
			notify(STEP_STARTED, this, step, context);
			step.execute(context);
			executedSteps.add(step);
			notify(STEP_COMPLETED, this, step, context);
		}
		catch (Exception e)
		{
			notify(STEP_FAILED, this, step, context, e);
			compensate(context, executedSteps);
			notify(SAGA_COMPENSATED, this, context);
			throw new SagaException("Failed to execute Saga", e);
		}
		finally
		{
			cleanup(context, executedSteps);
		}
	}

	/**
	 * Reflects whether compensation was incurred. Does not indicate success.
	 * 
	 * @return true if compensation was attempted.
	 */
	public boolean isCompensated()
	{
		return compensated;
	}

	/**
	 * Cleanup after the Saga has completed. This method will call cleanup() on each executed step that implements
	 * Cleanable.
	 * 
	 * @param context       an ExecutionContext instance to pass to each step.
	 * @param executedSteps a List of Step instances that were executed.
	 */
	protected void cleanup(SagaContext context, List<Step> executedSteps)
	{
		// Call cleanup() on each executed step that implements Cleanable.
		for (Step step : executedSteps) try
		{
			if (step instanceof Cleanable cleanable)
            {
                cleanable.cleanup(context);
            }
		}
		catch (Exception e)
		{
			// Ignore cleanup errors.
		}
	}

	/**
	 * Compensate the Saga, which will rollback all steps using compensation.
	 * 
	 * @param context an ExecutionContext instance to pass to each step.
	 * @throws SagaException if any compensation fails.
	 */
	@Override
	public void compensate(SagaContext context)
	throws SagaException
	{
		compensate(context, steps);
	}

	/**
	 * Compensate the Saga, which will rollback all previously executed steps using compensation.
	 * This method is called by the execute() method if a {@link Step} fails in a {@link Saga}.
	 * 
	 * @param context       an ExecutionContext instance to pass to each step.
	 * @param executedSteps a List of Step instances that were executed.
	 * @throws SagaException if any compensation fails.
	 */
	protected void compensate(SagaContext context, List<Step> executedSteps)
	throws SagaException
	{
		if (isCompensated()) return;

		notify(SAGA_COMPENSATION_STARTED, this, context);

		this.compensated = true;
		List<Step> reversed = new ArrayList<>(executedSteps);
		Collections.reverse(reversed);
		List<Exception> errors = new ArrayList<>();

		for (Step step : reversed) try
		{
			if (step instanceof CompensatableStep compensatable)
			{
				notify(STEP_COMPENSATION_STARTED, this, step, context);
				compensatable.compensate(context);
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

	/**
	 * Add an observer to the Saga. Observers will be notified of Saga and Step events.
	 * 
	 * @param observer an Observer instance to add to the Saga.
	 * @return this Saga instance.
	 */
	public Saga addObserver(Observer<SagaEvent> observer)
	{
		if (observers == null)
		{
			observers = new ArrayList<>();
		}

		observers.add(observer);
		return this;
	}

	/**
	 * Returns true if the Saga has observers. Otherwise, false.
	 * 
	 * @return true if the Saga has observers. Otherwise, false.
	 */
	private boolean hasObservers()
	{
		return observers != null;
	}

	/**
	 * Notify all observers of the given event.
	 * 
	 * @param event a SagaEvent instance to notify observers of.
	 */
	private void notify(SagaEvent event)
	{
		observers.forEach(observer -> observer.onEvent(event));
	}

	/**
	 * Notify all observers of a step error event.
	 * 
	 * @param type a SagaEvent.Type instance.
	 * @param saga this Saga instance.
	 * @param step the Step that failed.
	 * @param context the execution context.
	 * @param e the Exception that occurred.
	 */
	private void notify(SagaEvent.Type type, Saga saga, Step step, SagaContext context, Exception e)
	{
		if (hasObservers()) notify(new StepErrorEvent(type, saga, step, context, e));		
	}

	/**
	 * Notify all observers of a step event.
	 * 
	 * @param type a SagaEvent.Type instance.
	 * @param saga this Saga instance.
	 * @param step the Step on which the event occurred.
	 * @param context the execution context.
	 */
	private void notify(SagaEvent.Type type, Saga saga, Step step, SagaContext context)
	{
		if (hasObservers()) notify(new StepEvent(type, saga, step, context));
	}

	/**
	 * Notify all observers of a Saga event.
	 * 
	 * @param type    a SagaEvent.Type instance.
	 * @param saga    this Saga instance.
	 * @param context the execution context.
	 */
	private void notify(SagaEvent.Type type, Saga saga, SagaContext context)
	{
		if (hasObservers()) notify(new SagaEvent(type, saga, context));
	}

	/**
	 * Notify all observers of a Saga error event.
	 * 
	 * @param type    a SagaEvent.Type instance.
	 * @param saga    this Saga instance.
	 * @param context the execution context.
	 * @param e       the Exception that occurred.
	 */
	private void notify(SagaEvent.Type type, Saga saga, SagaContext context, Exception e)
	{
		if (hasObservers()) notify(new SagaErrorEvent(type, saga, context, e));
	}
}
