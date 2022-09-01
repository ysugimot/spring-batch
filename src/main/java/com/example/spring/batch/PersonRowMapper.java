package com.example.spring.batch;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonRowMapper implements RowMapper {
  @Override
  public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    Person person = new Person();

    person.setPersonId(rs.getInt("person_id"));
    person.setFirstName(rs.getString("first_name"));
    person.setLastName(rs.getString("last_name"));
    
    return person;
  }
}
