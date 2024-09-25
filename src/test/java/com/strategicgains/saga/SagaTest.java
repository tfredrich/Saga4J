package com.strategicgains.saga;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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

		saga.execute(new SagaContext());
		assertEquals(1, step.getExecutionCount());
		assertEquals(0, step.getCompensatedCount());
	}

	@Test
	void shouldCompensate()
	throws SagaException
	{
		Saga saga = new Saga();
		TestStep step = new TestStep();
		saga.addStep(step);

		saga.compensate(new SagaContext());
		assertEquals(0, step.getExecutionCount());
		assertEquals(1, step.getCompensatedCount());
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

		saga.execute(new SagaContext());
		assertEquals(1, step1.getExecutionCount());
		assertEquals(1, step2.getExecutionCount());
		assertEquals(0, step1.getCompensatedCount());
		assertEquals(0, step2.getCompensatedCount());
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
		assertEquals(0, step1.getExecutionCount());
		assertEquals(0, step2.getExecutionCount());
		assertEquals(1, step1.getCompensatedCount());
		assertEquals(1, step2.getCompensatedCount());
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
			assertEquals(1, step1.getExecutionCount());
			assertEquals(1, step2.getExecutionCount());
			assertEquals(0, step3.getExecutionCount());
			assertEquals(1, step1.getCompensatedCount());
			assertEquals(1, step2.getCompensatedCount());
			assertEquals(0, step3.getCompensatedCount());
			return;
		}

		fail("Should have thrown a SagaException");
	}

	@Test
	void shouldCompensateNested()
	{
		TestStep step0 = new TestStep();
		TestStep step1 = new TestStep();
		TestStep step2 = new TestStep();
		TestStep step3 = new ErrorStep();
		TestStep step4 = new TestStep();
		Saga saga = Saga.builder()
			.step(step0)
			.nestedSaga(nested -> nested.step(step1).step(step2).step(step3))
			.step(step4)
			.build();
		try
		{
			saga.execute(new SagaContext());
		}
		catch (SagaException e)
		{
			assertEquals(1, step0.getExecutionCount());
			assertEquals(1, step1.getExecutionCount());
			assertEquals(1, step2.getExecutionCount());
			assertEquals(1, step3.getExecutionCount());
			assertEquals(0, step4.getExecutionCount());
			assertEquals(1, step0.getCompensatedCount());
			assertEquals(1, step1.getCompensatedCount());
			assertEquals(1, step2.getCompensatedCount());
			assertEquals(1, step3.getCompensatedCount());
			assertEquals(0, step4.getCompensatedCount());
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
			assertEquals(1, step1.getExecutionCount());
			assertEquals(1, step2.getExecutionCount());
			assertEquals(0, step3.getExecutionCount());
			assertEquals(0, step3.getCompensatedCount());
			assertEquals(1, step2.getCompensatedCount());
			assertEquals(1, step1.getCompensatedCount());
			return;
		}

		fail("Should have thrown a SagaException");
	}
}
