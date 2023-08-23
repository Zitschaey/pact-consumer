package de.pact.consumer.consumerisbn;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import au.com.dius.pact.core.model.Request;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "test")
class BookServicesTest {
	@Pact(provider = "test", consumer = "testconsumer")
	public RequestResponsePact createPact(PactDslWithProvider builder) {

		return builder.given("test").uponReceiving("test interaction").path("/isbnconsumer/validatebook").method("POST")
				.willRespondWith().status(200)
				.body("{\"isbn\": \"1234567890\", \"author\": \"John Doe\", \"title\": \"Sample Book\"}").toPact();
	}
	
	@Test
	@PactTestFor(pactMethod = "createPact", port = "8080")

	void test(MockServer mockServer) throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClients.createDefault();

		// Erstelle die POST-Anfrage
		HttpPost httpPost = new HttpPost(mockServer.getUrl() + "/isbnconsumer/validatebook");
		httpPost.setHeader("Content-Type", "application/json");
		httpPost.setEntity(new StringEntity("{\"isbn\": \"1234567890\"}"));

		// Sende die Anfrage und erhalte die Antwort
		HttpResponse httpResponse = httpClient.execute((HttpUriRequest) httpPost);

		// Überprüfe den Statuscode der Antwort
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		assertEquals(200, statusCode);

		// Überprüfe den Inhalt der Antwort
		String responseBody = EntityUtils.toString(httpResponse.getEntity());
		assertEquals("{\"isbn\": \"1234567890\", \"author\": \"John Doe\", \"title\": \"Sample Book\"}", responseBody);
	}

}
