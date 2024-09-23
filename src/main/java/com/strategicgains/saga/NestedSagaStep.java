package com.strategicgains.saga;

public class NestedSagaStep
	implements SagaStep
{
	private final Saga nestedSaga;

	public NestedSagaStep(Saga nestedSaga)
	{
		this.nestedSaga = nestedSaga;
	}

	@Override
	public void execute(SagaContext context) throws Exception
	{
		nestedSaga.execute(context);
	}

	@Override
	public void compensate(SagaContext context) throws Exception
	{
		nestedSaga.compensate(context);
	}
}
