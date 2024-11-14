package com.strategicgains.saga;

import java.util.function.Consumer;

/**
 * A Step (non-compensatable) that executes a Consumer function.
 */
public class ConsumerStep
implements CompensatableStep
{
	private Consumer<ExecutionContext> execute;
	private Consumer<ExecutionContext> compensate;

	public ConsumerStep(Consumer<ExecutionContext> executeConsumer)
	{
		this(executeConsumer, null);
	}

	public ConsumerStep(Consumer<ExecutionContext> executeConsumer, Consumer<ExecutionContext> compensateConsumer)
	{
		this.execute = executeConsumer;
		this.compensate = compensateConsumer;
	}

	@Override
	public void execute(ExecutionContext context) throws Exception
	{
		execute.accept(context);
	}

	@Override
	public void compensate(ExecutionContext context)
	throws Exception
	{
		if (compensate != null)
        {
            compensate.accept(context);
        }
	}
}
