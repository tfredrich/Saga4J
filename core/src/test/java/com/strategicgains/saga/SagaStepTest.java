package com.strategicgains.saga;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SagaStepTest
{
	@Test
	void shouldExecute() throws Exception
	{
		TestStep step = new TestStep();
		step.execute(new SagaContext());
		assertEquals(1, step.getExecutionCount());
		assertEquals(0, step.getCompensatedCount());
	}

	@Test
	void shouldCompensate() throws Exception
	{
		TestStep step = new TestStep();
		step.compensate(new SagaContext());
		assertEquals(0, step.getExecutionCount());
		assertEquals(1, step.getCompensatedCount());
	}
}
