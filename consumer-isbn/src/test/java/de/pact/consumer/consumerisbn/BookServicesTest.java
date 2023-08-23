package de.pact.consumer.consumerisbn;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.fasterxml.jackson.databind.ObjectMapper;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;

import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.Request;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import de.pact.consumer.app.Book;
import de.pact.consumer.app.IsbnValidator;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "providerisbn")
class BookServicesTest {
	@Pact(provider = "bookservice", consumer = "bookconsumer")
	public RequestResponsePact createPact(PactDslWithProvider builder) {
		return builder.given("book with id 1 exists").uponReceiving("a request to get book by id").path("/books/1")
				.method("GET").willRespondWith().status(200)
				.body(new PactDslJsonBody().stringType("isbn", "9780590353427").stringType("author", "author 1")
						.stringType("title", "title 1").integerType("id", 1))
				.toPact();
	}

	@Test
	@PactTestFor(pactMethod = "createPact", pactVersion = PactSpecVersion.V3)

	public void test1(MockServer mockServer) throws ClientProtocolException, IOException {
		IsbnValidator validator = new IsbnValidator();

		String expectedJson = "{\"isbn\":\"9780590353427\",\"author\":\"author 1\",\"title\":\"title 1\",\"id\":1}";

		validator.setUrl(mockServer.getUrl());
		Book book = validator.isValidForBook(1);
		ObjectMapper obj = new ObjectMapper();

		String actualJson = obj.writeValueAsString(book);
		boolean test = validator.isValidIsbnForBookId(1);

		assertTrue(test);
		assertEquals(expectedJson, actualJson);
	}

}
