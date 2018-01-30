package de.fhws.posy.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class PosyRestServiceImplTest {

	private static JsonObject credentials;

	private static final String REST_URL = "https://java-test.fhws.de/posy-rest/rest/posy-rest-service/";

	@BeforeClass
	public static void beforeClass() throws Exception {

		String credentialsStr = IOUtils.toString(PosyRestServiceImplTest.class.getResourceAsStream("/credentials.json"), "UTF-8");
		credentials = Json.createReader(new StringReader(credentialsStr)).readObject();
	}

	@Test
	public void testRecordManyGrades100() throws Exception {

		Response response = testRecordManyGrades("/json-many-grades-100-period-fail.json", "record-many-grades");

		// expected response status 500 Internal Server Error
		assertEquals("expected Internal Server Error", 500, response.getStatus());

		// expected composite exceptions
		String jsonResponseStr = IOUtils.toString((InputStream) response.getEntity(), "UTF-8");
		JsonObject jsonExceptions = Json.createReader(new StringReader(jsonResponseStr)).readObject();
		assertNotNull("expected composite json exceptions", jsonExceptions);

		// get the first exception which is related to a schedule
		JsonObject scheduleExc = jsonExceptions.getJsonArray("exceptions").getJsonObject(0);
		assertEquals("BEC", scheduleExc.getString("course"));
		assertEquals("6100200", scheduleExc.getString("examCatalogId"));
	}

	private Response testRecordManyGrades(String resource, String path) throws Exception {

		String jsonStr = IOUtils.toString(PosyRestServiceImplTest.class.getResourceAsStream(resource), "UTF-8");
		JsonObject json = Json.createReader(new StringReader(jsonStr)).readObject();

		Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basicBuilder().nonPreemptive()
				.credentials(credentials.getString("username"), credentials.getString("password")).build();
		client.register(feature);
		WebTarget webTarget = client.target(REST_URL).path(path);
		Response response = webTarget.request().post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON));

		return response;

	}

}
