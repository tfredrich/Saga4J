package com.strategicgains.saga;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class SagaStepTest
{
	@Test
	void shouldExecute() throws Exception
	{
		TestStep step = new TestStep();
		step.execute(new SagaContext());
		assertTrue(step.isExecuted());
		assertFalse(step.isCompensated());
	}

	@Test
	void shouldCompensate() throws Exception
	{
		TestStep step = new TestStep();
		step.compensate(new SagaContext());
		assertFalse(step.isExecuted());
		assertTrue(step.isCompensated());
	}
}
