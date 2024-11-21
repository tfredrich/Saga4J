package com.strategicgains.saga4j.http;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.strategicgains.saga.Step;

public abstract class HttpStep
implements Step
{
	private static final String AUTHORIZATION = "Authorization";
	private static final String CONTENT_TYPE = "Content-Type";
	private static final String DEFAULT_CONTENT_TYPE = "application/json";

	private String url;
	private String contentType;
	private boolean useBasicAuth = false;

	protected HttpStep(String url)
	{
        this(url, DEFAULT_CONTENT_TYPE);
    }

	protected HttpStep(String url, String contentType)
	{
		this.url = url;
		this.contentType = contentType;
	}

	protected String getUrl()
	{
		return url;
	}

	protected String getContentType() {
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

	protected HttpResponse<JsonNode> get(String url, String authorization)
	throws UnirestException
	{
		GetRequest request = Unirest.get(url)
			.header(CONTENT_TYPE, contentType);

		if (authorization != null)
		{
			if (useBasicAuth())
            {
                request.basicAuth(authorization, "");
            }
            else
            {
                request.header(AUTHORIZATION, authorization);
		}
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected HttpResponse<JsonNode> post(String url, String authorization, String body)
	throws UnirestException
	{
		return Unirest.post(url)
			.header(CONTENT_TYPE, contentType)
			.header(AUTHORIZATION, authorization)
			.body(body)
			.asJson();
	}

	protected HttpResponse<JsonNode> put(String url, String authorization, String body)
	throws UnirestException
	{
		return Unirest.put(url)
			.header(CONTENT_TYPE, contentType)
			.header(AUTHORIZATION, authorization)
			.body(body).asJson();
	}

	protected HttpResponse<JsonNode> delete(String url, String authorization)
	throws UnirestException
	{
		return Unirest.delete(url)
			.header(CONTENT_TYPE, contentType)
			.header(AUTHORIZATION, authorization)
			.asJson();
	}

	protected HttpResponse<JsonNode> patch(String url, String authorization, String body)
	throws UnirestException
	{
		return Unirest.patch(url).header(CONTENT_TYPE, contentType).header(AUTHORIZATION, authorization).body(body).asJson();
	}
}
