package com.strategicgains.saga;

public class TestStep
implements SagaStep
{
	private boolean isExecuted = false;
	private boolean isCompensated = false;

	@Override
	public void execute(SagaContext context) throws Exception
	{
		this.isExecuted = true;
	}

	@Override
	public void compensate(SagaContext context) throws Exception
	{
		this.isCompensated = true;
	}

	public boolean isExecuted()
	{
		return isExecuted;
	}

	public boolean isCompensated()
	{
		return isCompensated;
	}
}
