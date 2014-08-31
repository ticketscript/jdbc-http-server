package org.wololo.jdbc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class RowTest extends ServerTest {
	@Test
	public void testGET() throws IOException, SQLException {
		try (Connection connection = ds.getConnection();
				Statement statement = connection.createStatement()) {
			statement.execute("insert into test (name) values ('test')");
		}
		assertEquals(getJson("Row"),
				target("db/TEST/schemas/PUBLIC/tables/TEST/rows/1").request().get(String.class));
	}
	
	@Test
	public void testPUT() throws IOException, SQLException {
		try (Connection connection = ds.getConnection();
				Statement statement = connection.createStatement()) {
			statement.execute("insert into test (name) values ('test')");
		}
		Entity<String> entity = Entity.json(getJson("RowPUT"));
		Response response = target("db/TEST/schemas/PUBLIC/tables/TEST/rows/1").request().put(entity);
		assertEquals(204, response.getStatus());
		assertEquals(getJson("RowPUT"),
				target("db/TEST/schemas/PUBLIC/tables/TEST/rows/1").request().get(String.class));
	}
	
	@Test
	public void testDELETE() throws IOException, SQLException {
		try (Connection connection = ds.getConnection();
				Statement statement = connection.createStatement()) {
			statement.execute("insert into test (name) values ('test')");
		}
		Response response = target("db/TEST/schemas/PUBLIC/tables/TEST/rows/1").request().delete();
		assertEquals(204, response.getStatus());
	}
	
	@Test
	public void testDELETEFailOnMissingPK() throws IOException, SQLException {
		Response response = target("db/TEST/schemas/PUBLIC/tables/TEST/rows/1").request().delete();
		assertEquals(500, response.getStatus());
	}

}
