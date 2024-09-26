package com.strategicgains.saga;

import java.util.function.Consumer;

/**
 * A SagaStep (non-compensatable) that executes a Consumer function.
 */
public class ConsumerStep
implements SagaStep
{
	private Consumer<SagaContext> consumer;

	public ConsumerStep(Consumer<SagaContext> consumer)
	{
		this.consumer = consumer;
	}

	@Override
	public void execute(SagaContext context) throws Exception
	{
		consumer.accept(context);
	}
}
