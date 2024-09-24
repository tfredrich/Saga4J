package com.strategicgains.saga;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SagaTest
{
	@Test
	void shouldExecute()
	throws SagaException
	{
		Saga saga = new Saga();
		TestStep step = new TestStep();
		saga.addStep(step);

		saga.execute();
		assertTrue(step.isExecuted());
		assertTrue(!step.isCompensated());
	}

	@Test
	void shouldCompensate()
	throws SagaException
	{
		Saga saga = new Saga();
		TestStep step = new TestStep();
		saga.addStep(step);

		saga.compensate(new SagaContext());
		assertTrue(!step.isExecuted());
		assertTrue(step.isCompensated());
	}

	@Test
	void shouldExecuteMultiple()
	throws SagaException
	{
		TestStep step1 = new TestStep();
		TestStep step2 = new TestStep();
		Saga saga = new Saga()
			.addStep(step1)
			.addStep(step2);

		saga.execute();
		assertTrue(step1.isExecuted());
		assertTrue(step2.isExecuted());
		assertTrue(!step1.isCompensated());
		assertTrue(!step2.isCompensated());
	}

	@Test
	void shouldCompensateMultiple()
	throws SagaException
	{
		TestStep step1 = new TestStep();
		TestStep step2 = new TestStep();
		Saga saga = new Saga()
			.addStep(step1)
			.addStep(step2);

		saga.compensate(new SagaContext());
		assertTrue(!step1.isExecuted());
		assertTrue(!step2.isExecuted());
		assertTrue(step1.isCompensated());
		assertTrue(step2.isCompensated());
	}

	@Test
	void shouldAutomaticallyCompensate()
	{
		TestStep step1 = new TestStep();
		TestStep step2 = new ErrorStep();
		TestStep step3 = new TestStep();
		Saga saga = new Saga()
			.addStep(step1)
			.addStep(step2)
			.addStep(step3);

		try
		{
			saga.execute(new SagaContext());
		}
		catch (SagaException e)
		{
			assertTrue(step1.isExecuted());
			assertTrue(step2.isExecuted());
			assertTrue(!step3.isExecuted());
			assertTrue(step1.isCompensated());
			assertTrue(step2.isCompensated());
			assertTrue(!step3.isCompensated());
			return;
		}

		fail("Should have thrown a SagaException");
	}

	@Test
	void shouldHandleCompensateError()
	{
		TestStep step1 = new TestStep();
		TestStep step2 = new ErrorCompensateStep();
		TestStep step3 = new TestStep();
		Saga saga = new Saga()
			.addStep(step1)
			.addStep(step2)
			.addStep(step3);

		try
		{
			saga.execute(new SagaContext());
		}
		catch (SagaException e)
		{
			assertTrue(step1.isExecuted());
			assertTrue(step2.isExecuted());
			assertTrue(!step3.isExecuted());
			assertTrue(!step3.isCompensated());
			assertTrue(step2.isCompensated());
			assertTrue(step1.isCompensated());
			return;
		}

		fail("Should have thrown a SagaException");
	}
}
