package com.strategicgains.saga;

/**
 * A RunnableStep is a simple implementation of a CompensatableStep that takes two Runnables: one to execute
 * and one to compensate.  The execute() method simply runs the execute Runnable and the compensate() method
 * simply runs the compensate Runnable.
 */
public class RunnableStep
implements CompensatableStep
{
	private Runnable execute;
	private Runnable compensate;

	@Override
	public void execute(SagaContext context)
	throws Exception
	{
		execute.run();
	}

	@Override
	public void compensate(SagaContext context)
	throws Exception
	{
        compensate.run();
    }
}
