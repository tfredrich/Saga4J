package com.strategicgains.saga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strategicgains.saga.event.ContextCreatedEvent;
import com.strategicgains.saga.event.ContextEvent;
import com.strategicgains.saga.event.ContextUpdatedEvent;

public class SagaContext
implements Observable<ContextEvent>
{
	private Map<String, Object> values = new HashMap<>();
	private List<Observer<ContextEvent>> observers;

	public void setValue(String key, Object value)
	{
		Object before = values.put(key, value);

		if (hasObservers())
		{
			if (before != null) notify(key, value, before);
			notify(key, value);
		}
	}

	public Object getValue(String key)
	{
		return values.get(key);
	}

	public <T> T getValue(String key, Class<T> type)
	{
		return type.cast(values.get(key));
	}

	@Override
	public void addObserver(Observer<ContextEvent> observer)
	{
		if (observers == null)
		{
			observers = new ArrayList<>();
		}

		observers.add(observer);
	}

	private boolean hasObservers()
	{
		return observers != null;
	}

	private void notify(ContextEvent event)
	{
		observers.forEach(observer -> observer.onEvent(event));
	}

	private void notify(String key, Object value, Object before)
	{
		notify(new ContextUpdatedEvent(key, value, before));
	}

	private void notify(String key, Object value)
	{
		notify(new ContextCreatedEvent(key, value));
	}
}
