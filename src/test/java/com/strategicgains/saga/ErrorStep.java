package com.strategicgains.saga;

public class ErrorStep
extends TestStep
{
	@Override
	public void execute(SagaContext context) throws Exception
	{
		super.execute(context);
		throw new Exception("Error Step::execute()");
	}
}
