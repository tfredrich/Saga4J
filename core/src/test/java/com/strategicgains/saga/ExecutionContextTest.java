package com.strategicgains.saga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.strategicgains.saga.event.ContextEvent;
import com.strategicgains.saga.event.ContextUpdatedEvent;

class ExecutionContextTest
{
	@Test
	void shouldSetValues()
	{
		ExecutionContext grandParent = new ExecutionContext();
		ExecutionContext parent = new ExecutionContext(grandParent);
		parent.setValue("key", "parent");
		parent.setValue("namespace", "key", "parent2");

		ExecutionContext context = new ExecutionContext(parent);
		context.setValue("key", "value");
		assertEquals("value", context.getValue("key"));
		assertEquals("value", context.getValue("key", String.class));
		context.setValue("namespace", "key", "value2");
		assertEquals("value2", context.getValue("namespace", "key"));
		assertEquals("value2", context.getValue("namespace", "key", String.class));

		parent = context.getParent();
		assertNotNull(parent);
		assertEquals("parent", parent.getValue("key"));
		assertEquals("parent", parent.getValue("key", String.class));
		assertEquals("parent2", parent.getValue("namespace", "key"));
		assertEquals("parent2", parent.getValue("namespace", "key", String.class));

		assertNotNull(parent.getParent());
	}

	@Test
	void shouldNotifyObservers()
	{
		ExecutionContext context = new ExecutionContext();
		context.addObserver(event ->
		{
			if (event.getType() == ContextEvent.Type.CONTEXT_CREATED)
			{
				assertEquals("key", event.getKey());
				assertEquals("value", event.getValue());
				assertEquals("value", event.getValue(String.class));
				return;
			}
			else if (event.getType() == ContextEvent.Type.CONTEXT_UPDATED)
			{
				assertEquals("key", event.getKey());
				assertEquals("value2", event.getValue());
				assertEquals("value2", event.getValue(String.class));
				assertEquals("value", ((ContextUpdatedEvent) event).getBefore());
				assertEquals("value", ((ContextUpdatedEvent) event).getBefore(String.class));
				return;
			}

			fail("Unexpected event type: " + event.getType());
		});
		context.setValue("key", "value");
		context.setValue("key", "value2");
	}
}
