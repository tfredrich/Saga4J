package com.strategicgains.saga;

/**
 * A Step is a basic execute-only step in a Saga. It is responsible for executing a single operation but will
 * not be compensated if the saga fails.
 * 
 * @see Saga
 * @see CompensatableStep
 */
public interface Step
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
