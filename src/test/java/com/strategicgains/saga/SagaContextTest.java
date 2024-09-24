package com.strategicgains.saga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.strategicgains.saga.event.ContextEvent;
import com.strategicgains.saga.event.ContextEventType;
import com.strategicgains.saga.event.ContextUpdatedEvent;

class SagaContextTest
{
	@Test
	void shouldSetValues()
	{
		SagaContext context = new SagaContext();
		context.setValue("key", "value");
		assertEquals("value", context.getValue("key"));
		assertEquals("value", context.getValue("key", String.class));
		context.setValue("namespace", "key", "value");
		assertEquals("value", context.getValue("namespace", "key"));
		assertEquals("value", context.getValue("namespace", "key", String.class));
	}

	@Test
	void shouldNotifyObservers()
	{
		SagaContext context = new SagaContext();
		TestObserver observer = new TestObserver();
		context.addObserver(observer);
		context.setValue("key", "value");
		assertTrue(observer.isNotified());

		ContextEvent ce = observer.getEvent();
		assertEquals(ContextEventType.CONTEXT_CREATED, ce.getType());
		assertEquals("key", ce.getKey());
		assertEquals("value", ce.getValue());
		assertEquals("value", ce.getValue(String.class));

		context.setValue("key", "value2");
		assertTrue(observer.isNotified());
		ce = observer.getEvent();
		assertEquals(ContextEventType.CONTEXT_UPDATED, ce.getType());
		ContextUpdatedEvent ce2 = (ContextUpdatedEvent) observer.getEvent();
		assertEquals("key", ce2.getKey());
		assertEquals("value2", ce2.getValue());
		assertEquals("value2", ce2.getValue(String.class));
		assertEquals("value", ce2.getBefore());
		assertEquals("value", ce2.getBefore(String.class));
	}

	public class TestObserver
	implements Observer<ContextEvent>
	{
		private ContextEvent event;

		@Override
		public void onEvent(ContextEvent event)
		{
			this.event = event;
		}

		public boolean isNotified()
		{
			return event != null;
		}

		public ContextEvent getEvent()
		{
			return event;
		}
	};
}
