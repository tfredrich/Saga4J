package com.strategicgains.saga;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.strategicgains.saga.event.ContextCreatedEvent;
import com.strategicgains.saga.event.ContextEvent;
import com.strategicgains.saga.event.ContextUpdatedEvent;

/**
 * A SagaContext is a key-value store that can have a parent Context. It is used to
 * store and retrieve data for a given Saga instance. Contexts can have
 * Observers that are notified when data is added or updated.
 */
public class SagaContext
{
	private SagaContext parent;
	private Map<String, Object> values = new HashMap<>();
	private List<Observer<ContextEvent>> observers;

	public SagaContext()
	{
		super();
	}

	public SagaContext(SagaContext context)
	{
		this();
		this.parent = context;
	}

	public boolean hasParent()
	{
		return parent != null;
	}

	public SagaContext getParent()
	{
		return parent;
	}

	public void setValue(String key, Object value)
	{
		Object before = values.put(key, value);

		if (hasObservers())
		{
			if (before != null) notify(key, value, before);
			else notify(key, value);
		}
	}

	public void setValue(String namespace, String key, Object value)
	{
		setValue(of(namespace, key), value);
	}

	public Object getValue(String key)
	{
		return values.get(key);
	}

	public <T> T getValue(String key, Class<T> type)
	{
		return type.cast(values.get(key));
	}

	public Object getValue(String namespace, String key)
	{
		return getValue(of(namespace, key));
	}

	public <T> T getValue(String namespace, String key, Class<T> type)
	{
		return type.cast(getValue(of(namespace, key)));
	}

	public SagaContext addObserver(Observer<ContextEvent> observer)
	{
		if (observers == null)
		{
			observers = new ArrayList<>();
		}

		observers.add(observer);
		return this;
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

	private String of(String namespace, String key)
	{
		return namespace + "." + key;
	}
}
