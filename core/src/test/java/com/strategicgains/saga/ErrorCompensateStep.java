package com.strategicgains.saga;

public class ErrorCompensateStep
extends ErrorStep
{
	@Override
	public void execute(ExecutionContext context) throws Exception
	{
		super.execute(context);
		throw new Exception("Error Step::execute()");
	}

	@Override
	public void compensate(ExecutionContext context) throws Exception
	{
		super.compensate(context);
		throw new Exception("Error Step::compensate()");
	}
}
