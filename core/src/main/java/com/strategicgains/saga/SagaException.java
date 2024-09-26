package com.strategicgains.saga;

import java.util.List;

public class SagaException
extends Exception
{
	private static final long serialVersionUID = -3329307321406171013L;

	public SagaException()
	{
	}

	public SagaException(String message)
	{
		super(message);
	}

	public SagaException(Throwable cause)
	{
		super(cause);
	}

	public SagaException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SagaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SagaException(String string, List<Exception> errors)
	{
		this(string, errors.get(errors.size() - 1));

		if (errors.size() > 1) errors.forEach(this::addSuppressed);
	}
}
