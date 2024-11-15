package com.strategicgains.saga;

/**
 * Defines the interface for a Saga or Step to be compensated on failure.
 */
public interface Compensatable
{
	void compensate(SagaContext context)
	throws Exception;
}
