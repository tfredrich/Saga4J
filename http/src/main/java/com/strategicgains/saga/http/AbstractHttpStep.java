package com.strategicgains.saga.http;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.strategicgains.saga.Step;

public abstract class AbstractHttpStep
implements Step
{
	private static final String ACCEPT = "Accept";
	private static final String AUTHORIZATION = "Authorization";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String DEFAULT_CONTENT_TYPE = "application/json";

	private String url;
	private String contentType;
	private boolean useBasicAuth = false;

	protected AbstractHttpStep(String url)
	{
        this(url, DEFAULT_CONTENT_TYPE);
    }

	protected AbstractHttpStep(String url, String contentType)
	{
		this.url = url;
		this.contentType = contentType;
	}

	protected String getUrl()
	{
		return url;
	}

	protected String getContentType()
	{
		return contentType;
	}

	protected boolean useBasicAuth()
	{
		return useBasicAuth;
	}

	protected void useBasicAuth(boolean useBasicAuth)
	{
		this.useBasicAuth = useBasicAuth;
	}

	protected GetRequest get(String url)
	{
		return Unirest.get(url).header(ACCEPT, contentType);
	}

	protected HttpResponse<JsonNode> get(String url, String authorization)
	throws UnirestException
	{
		return get(url)
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected HttpRequestWithBody post(String url, String body)
	{
		HttpRequestWithBody request = Unirest.post(url).header(CONTENT_TYPE, contentType);
		request.body(body);
		return request;
	}

	protected HttpResponse<JsonNode> post(String url, String authorization, String body)
	throws UnirestException
	{
		return post(url, body)
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected HttpRequestWithBody put(String url, Object body)
	{
		HttpRequestWithBody request = Unirest.put(url).header(CONTENT_TYPE, contentType);
		request.body(body);
		return request;
	}

	protected HttpResponse<JsonNode> put(String url, String authorization, Object body)
	throws UnirestException
	{
		return put(url, body)
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected HttpRequestWithBody delete(String url)
    {
        return Unirest.delete(url).header(CONTENT_TYPE, contentType);
    }

	protected HttpResponse<JsonNode> delete(String url, String authorization)
	throws UnirestException
	{
		return delete(url)
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected HttpRequestWithBody patch(String url, Object body)
	{
		HttpRequestWithBody request = Unirest.patch(url).header(CONTENT_TYPE, contentType);
		request.body(body);
		return request;
	}

	protected HttpResponse<JsonNode> patch(String url, String authorization, Object body)
	throws UnirestException
	{
		return patch(url, body)
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected boolean isSuccessful(HttpResponse<?> response)
	{
		return response.getStatus() >= 200 && !isRetryable(response) && !isFailed(response);
	}

	protected boolean isRetryable(HttpResponse<?> response)
	{
		int status = response.getStatus();
		return status == 408 || status == 429 || status == 502 || status == 503 || status == 504;
	}

	protected boolean isFailed(HttpResponse<?> response)
	{
		int status = response.getStatus();
		return status == 400 || status == 401 || status == 403 || status == 404 || status == 405 || status == 409 || status == 422 || status == 500;
	}
}
