package br.com.iwe.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "trips")
public class Trips {

	@DynamoDBHashKey(attributeName = "country")
	private String country;
	
	@DynamoDBRangeKey(attributeName = "city")
	private String city;

	@DynamoDBAttribute(attributeName = "date")
	private String date;
	@DynamoDBAttribute(attributeName = "reason")
	private String reason;

	public Trips(String country, String city, String date, String reason) {
		super();
		this.country = country;
		this.city = city;
		this.date = date;
		this.reason = reason;
	}

	public Trips() {
		super();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
