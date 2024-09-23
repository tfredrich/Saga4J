package com.strategicgains.saga;

/**
 * A SagaStep is a single step in a Saga. It is responsible for executing a single operation and compensating for that
 * operation if the saga fails.
 */
public interface SagaStep
{
	/**
	 * Execute the step.
	 * 
	 * @param context The saga execution context.
	 * @throws Exception
	 */
	void execute(SagaContext context)
	throws Exception;

	/**
	 * Compensate for the step.
	 * 
	 * @param context The saga execution context.
	 * @throws Exception
	 */
	void compensate(SagaContext context)
	throws Exception;
}
