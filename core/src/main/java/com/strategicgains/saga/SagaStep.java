package com.strategicgains.saga;

/**
 * A SagaStep is a basic exectue-only step in a Saga. It is responsible for executing a single operation but will
 * not be compensated if the saga fails.
 * 
 * @see Saga
 * @see CompensatableStep
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
}
