package com.strategicgains.saga;

public class TestStep
implements CompensatableStep
{
	private int executed = 0;
	private int compensated = 0;

	@Override
	public void execute(ExecutionContext context) throws Exception
	{
		++executed;
	}

	@Override
	public void compensate(ExecutionContext context) throws Exception
	{
		++compensated;
	}

	public int getExecutionCount()
	{
		return executed;
	}

	public int getCompensatedCount()
	{
		return compensated;
	}
}
