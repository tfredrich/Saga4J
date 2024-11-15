package com.strategicgains.saga;

import java.util.function.Consumer;

/**
 * A Step (non-compensatable) that executes a Consumer function.
 */
public class ConsumerStep
implements CompensatableStep
{
	private Consumer<SagaContext> execute;
	private Consumer<SagaContext> compensate;

	public ConsumerStep(Consumer<SagaContext> executeConsumer)
	{
		this(executeConsumer, null);
	}

	public ConsumerStep(Consumer<SagaContext> executeConsumer, Consumer<SagaContext> compensateConsumer)
	{
		this.execute = executeConsumer;
		this.compensate = compensateConsumer;
	}

	@Override
	public void execute(SagaContext context) throws Exception
	{
		execute.accept(context);
	}

	@Override
	public void compensate(SagaContext context)
	throws Exception
	{
		if (compensate != null)
        {
            compensate.accept(context);
        }
	}
}
