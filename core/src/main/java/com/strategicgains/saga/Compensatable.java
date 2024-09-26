package com.strategicgains.saga;

public interface Compensatable<T>
{
	void compensate(T context)
	throws Exception;
}
