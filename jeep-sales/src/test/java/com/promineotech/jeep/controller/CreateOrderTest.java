/**
 * 
 */
package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

/**
 * @author Bryce Cash
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = {
		"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
		"classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
		config = @SqlConfig(encoding = "utf-8"))
class CreateOrderTest {
	  protected String createOrderBody() {
		    //   @formatter:off
		    return "{\n"
		        + "  \"customer\":\"ATTAWAY_HECKTOR\",\n"
		        + "  \"model\":\"WRANGLER\",\n"
		        + "  \"trim\":\"Sport Altitude\",\n"
		        + "  \"doors\":4,\n"
		        + "  \"color\":\"EXT_COLORADO_RED\",\n"
		        + "  \"engine\":\"2_0_TURBO\",\n"
		        + "  \"tire\":\"37_COOPER\",\n"
		        + "  \"options\":[\n"
		        + "     \"EXT_MOPAR_TRAILER\",\n"
		        + "     \"INT_MOPAR_MATS\"\n"
		        + "  ]\n"
		        + "}";
//		      @formatter:on
		  }
	  
	@LocalServerPort
	private int serverPort; 
	@Autowired
	private TestRestTemplate restTemplate;
	@Test
	void testCreateOrderReturnsSuccess201() {
	// Given: an order as JSON
	String body = createOrderBody();
	String uri = String.format("http://localhost:%d/orders", serverPort);
	
	HttpHeaders headers = new HttpHeaders();
	headers.setContentType(MediaType.APPLICATION_JSON);
	// When: the order is sent
	HttpEntity<String> bodyEntity = new HttpEntity<>(body, headers);
	ResponseEntity<Order> response = restTemplate.exchange(uri, HttpMethod.POST, bodyEntity, Order.class);
	// Then: a 201 status is returned
	assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	// And: the returned order is correct
	assertThat(response.getBody()).isNotNull();
	
	Order order = response.getBody();
	assertThat(order.getCustomer().getCustomerId()).isEqualTo("ATTAWAY_HECKTOR");
	assertThat(order.getModel().getModelId()).isEqualTo(JeepModel.WRANGLER);
	assertThat(order.getModel().getTrimLevel()).isEqualTo("Sport Altitude");
    assertThat(order.getModel().getNumDoors()).isEqualTo(4);
	assertThat(order.getColor().getColorId()).isEqualTo("EXT_COLORADO_RED");
	assertThat(order.getEngine().getEngineId()).isEqualTo("2_0_TURBO");
	assertThat(order.getTire().getTireId()).isEqualTo("37_COOPER");
	assertThat(order.getOptions()).hasSize(2);
	
	}

}
