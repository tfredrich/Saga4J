package com.strategicgains.saga;

/**
 * A step in a saga that is actually a separate, nested saga.
 * 
 * If the nested saga fails during execution, it will attempt compensation before throwing an exception
 * to the parent saga.
 * 
 * Reasons to use a nested saga instead of a regular step include:
 * - The nested saga is a reusable sequence of steps.
 * - The nested saga is complex and should be encapsulated.
 * - The nested saga should be executed in a different transactional context than the parent saga.
 * - The nested saga requires different observers than the parent saga.
 * - The nested saga should have its own SagaContext (default is to NOT create a new context).
 */
public class NestedSagaStep
	implements SagaStep
{
	private boolean isCompensated = false;
	private boolean shouldCreateNewContext;
	private final Saga nestedSaga;

	/**
	 * Creates a new NestedSagaStep and resuses the parent saga's context.
	 * 
	 * @param nestedSaga the nested saga to execute.
	 */
	public NestedSagaStep(Saga nestedSaga)
	{
		this(nestedSaga, false);
	}

	/**
	 * Creates a new NestedSagaStep, optionally creating a new context for the nested saga.
	 * 
	 * @param nestedSaga the nested saga to execute.
	 * @param shouldCreateNewContext true to create a new context for the nested saga; false to reuse the parent saga's context.
	 */
	public NestedSagaStep(Saga nestedSaga, boolean shouldCreateNewContext)
	{
		this.nestedSaga = nestedSaga;
		this.shouldCreateNewContext = shouldCreateNewContext;
	}

	/**
	 * Executes the nested saga. If an exception occurs, the nested saga has already attempted compensation.
	 * Therefore, subsequent compensation attempts via calling {@link compensate(SagaContext)} will be ignored.
	 */
	@Override
	public void execute(SagaContext context) throws Exception
	{
		SagaContext nestedContext = context;

		if (shouldCreateNewContext)
		{
			nestedContext = new SagaContext(context);
		}

		try
		{
			nestedSaga.execute(nestedContext);
		}
		catch (Exception e)
		{
			isCompensated = true;
			throw e;
		}
	}

	/**
     * The parent saga will attempt compensation for errors that occur during execution of this step or
     * any subsequent steps. However, if this nested saga has failed during {@link execute(SagaContext)}, it
     * has already attempted compensation. Therefore, subsequent compensation attempts will be ignored.
     * 
     * On the other hand, the parent saga can also compensate due to a subsequent step failure. In this
     * case, the nested saga should also be compensated.
     * 
     * @param context the parent saga context. Note that context created during execution of the nested saga is not available.
     */
	@Override
	public void compensate(SagaContext context) throws Exception
	{
		if (!isCompensated)
		{
			nestedSaga.compensate(context);
			isCompensated = true;
		}
	}
}
