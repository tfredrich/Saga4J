package com.strategicgains.saga;

/**
 * A CompensatableStep is a step in a Saga that is compensated if the saga fails.
 */
public interface CompensatableStep
extends SagaStep
{
	void compensate(SagaContext context)
	throws Exception;
}
