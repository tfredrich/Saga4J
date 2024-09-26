package com.strategicgains.saga;

public interface Observable<T>
{
	void addObserver(Observer<T> observer);
}
