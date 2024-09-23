package com.strategicgains.saga;

public interface Observer<T>
{
	void onEvent(T event);
}
