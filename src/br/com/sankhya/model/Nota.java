package br.com.sankhya.model;

import java.math.BigDecimal;

public class Nota {
	private BigDecimal nunota;
	private BigDecimal percdesc;
	private BigDecimal vlrdesctot;
	private BigDecimal ad_descpremio;
	
	public Nota() {
		
	}

	public BigDecimal getNunota() {
		return nunota;
	}

	public void setNunota(BigDecimal nunota) {
		this.nunota = nunota;
	}

	public BigDecimal getPercdesc() {
		return percdesc;
	}

	public void setPercdesc(BigDecimal percdesc) {
		this.percdesc = percdesc;
	}

	public BigDecimal getVlrdesctot() {
		return vlrdesctot;
	}

	public void setVlrdesctot(BigDecimal vlrdesctot) {
		this.vlrdesctot = vlrdesctot;
	}

	public BigDecimal getAd_descpremio() {
		return ad_descpremio;
	}

	public void setAd_descpremio(BigDecimal ad_descpremio) {
		this.ad_descpremio = ad_descpremio;
	}
	
	
}
