package com.db.awmd.challenge.domain;

import lombok.Data;
import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class UserTransaction {

	@NotNull
	@NotEmpty
	private String fromAccountId;
	
	@NotNull
	@NotEmpty
	private String toAccountId;
	
	@NotNull
	@Min(value = 0, message = "Initial balance must be positive.")
	private BigDecimal amount;

	@JsonCreator
	public UserTransaction(@JsonProperty("fromAccountId") String fromAccountId, 
			@JsonProperty("toAccountId") String toAccountId, 
			@JsonProperty("amount") BigDecimal amount) {
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}
	



}
