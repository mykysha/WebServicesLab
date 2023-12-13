package org.example.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TableData implements Serializable {
  @JsonProperty("Name") // Map the JSON property "Name" to the field "Name"
  public String name;

  @JsonProperty("Id") // Map the JSON property "Id" to the field "Id"
  public int id;

  public TableData() {
    // Default constructor
  }

  public TableData(String name, int id) {
    this.name = name;
    this.id = id;
  }
}