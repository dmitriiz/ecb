package ru.dazzled.ecb.jpa;

import java.io.Serializable;
import java.lang.Float;
import java.lang.Long;
import java.lang.String;
import java.sql.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;

/**
 * Entity implementation class for Entity: CurrencyRate
 *
 */
@Entity
@Table(name="currency_rates")
public class CurrencyRate implements Serializable {
	private static final long serialVersionUID = -6677775237362831148L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="currency_rates_seq")
	@SequenceGenerator(name="currency_rates_seq", sequenceName="currency_rates_seq")
	private Long id;
	@Index(name = "cr_date")
	@NotNull
	private Date date;
	@Index(name = "cr_currency")
	@NotNull
	@Size(min = 3, max = 3)
	private String currency;
	@NotNull
	private Float rate;

	public CurrencyRate() {
		super();
	}   
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}   
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}   
	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}   
	public Float getRate() {
		return this.rate;
	}

	public void setRate(Float rate) {
		this.rate = rate;
	}
   
}
