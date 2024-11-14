package com.strategicgains.saga;

import java.util.function.Consumer;

/**
 * A Step (non-compensatable) that executes a Consumer function.
 */
public class ConsumerStep
implements Step
{
	private Consumer<ExecutionContext> consumer;

	public ConsumerStep(Consumer<ExecutionContext> consumer)
	{
		this.consumer = consumer;
	}

	@Override
	public void execute(ExecutionContext context) throws Exception
	{
		consumer.accept(context);
	}
}
