package com.strategicgains.saga.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.strategicgains.saga.NestedSagaStep;
import com.strategicgains.saga.Observer;
import com.strategicgains.saga.Saga;
import com.strategicgains.saga.Step;
import com.strategicgains.saga.event.SagaEvent;

public class SagaBuilder
{
	private List<Step> steps = new ArrayList<>();
	private List<Observer<SagaEvent>> observers;

	public SagaBuilder step(Step step)
	{
		steps.add(step);
		return this;
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
		Saga saga = new Saga();
        steps.forEach(saga::addStep);

        if (observers != null)
		{
			observers.forEach(saga::addObserver);
		}

        return saga;
	}
}
