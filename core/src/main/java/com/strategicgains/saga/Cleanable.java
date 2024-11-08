package com.strategicgains.saga;

/**
 * A Cleanable is an interface for {@link Step}s that need to be cleaned up,
 * or otherwise release resources, when a {@link Saga} completes.
 */
public interface Cleanable
{
	/**
	 * Cleanup resources used by the implementing {@link Step}. This method is called when the {@link Saga} completes
	 * (either successfully or unsuccessfully) for all successfully executed steps.
	 * 
	 * @param context the ExecutionContext used by the {@link Saga}.
	 */
	void cleanup(ExecutionContext context);
}
