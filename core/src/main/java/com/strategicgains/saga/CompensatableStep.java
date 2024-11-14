package com.strategicgains.saga;

/**
 * A CompensatableStep is a step in a Saga that is compensated if the saga fails.
 */
public interface CompensatableStep
extends Step
{
	void compensate(ExecutionContext context)
	throws Exception;
}
