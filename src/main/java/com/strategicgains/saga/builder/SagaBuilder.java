package com.strategicgains.saga.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.strategicgains.saga.NestedSagaStep;
import com.strategicgains.saga.Observer;
import com.strategicgains.saga.Saga;
import com.strategicgains.saga.SagaStep;
import com.strategicgains.saga.event.SagaEvent;

public class SagaBuilder
{
	private List<SagaStep> steps = new ArrayList<>();
	private List<Observer<SagaEvent>> observers;

	public SagaBuilder step(SagaStep step)
	{
		steps.add(step);
		return this;
	}

	public SagaBuilder step(Consumer<StepBuilder> stepBuilder)
	{
		StepBuilder builder = new StepBuilder();
		stepBuilder.accept(builder);
		return step(builder.build());
	}

	public SagaBuilder observer(Observer<SagaEvent> observer)
	{
		if (observers == null)
		{
			observers = new ArrayList<>();
		}

		observers.add(observer);
		return this;
	}

	public SagaBuilder nestedSaga(Saga nested)
	{
		steps.add(new NestedSagaStep(nested));
		return this;
	}

	public SagaBuilder nestedSaga(Consumer<SagaBuilder> nestedBuilder)
	{
		SagaBuilder nested = new SagaBuilder();
		nestedBuilder.accept(nested);
		return step(new NestedSagaStep(nested.build()));
	}

	public Saga build()
	{
		return new Saga();
	}
}