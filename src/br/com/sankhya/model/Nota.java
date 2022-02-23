package br.com.sankhya.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.sankhya.dao.NotaDAO;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.vo.DynamicVO;

public class Nota {
	private BigDecimal vlrdesctot;
	private BigDecimal percdesc;
	private BigDecimal adVlrdesc;
	private BigDecimal vlrtot;
	private BigDecimal descontoPremio;
	private BigDecimal codpremio;
	private BigDecimal codtipoper;

	public Nota(DynamicVO cabVO, JdbcWrapper jdbc) throws Exception {
		buildNota(cabVO, jdbc);
	}

	public BigDecimal getVlrdesctot() {
		return vlrdesctot;
	}

	public void setVlrdesctot(BigDecimal vlrdesctot) {
		this.vlrdesctot = vlrdesctot;
	}

	public BigDecimal getPercdesc() {
		return percdesc;
	}

	public void setPercdesc(BigDecimal percdesc) {
		this.percdesc = percdesc;
	}

	public BigDecimal getAdVlrdesc() {
		return adVlrdesc;
	}

	public void setAdVlrdesc(BigDecimal adVlrdesc) {
		this.adVlrdesc = adVlrdesc;
	}

	public BigDecimal getVlrtot() {
		return vlrtot;
	}

	public void setVlrtot(BigDecimal vlrtot) {
		this.vlrtot = vlrtot;
	}

	public BigDecimal getDescontoPremio() {
		return descontoPremio;
	}

	public void setDescontoPremio(BigDecimal descontoPremio) {
		this.descontoPremio = descontoPremio;
	}

	public BigDecimal getCodpremio() {
		return codpremio;
	}

	public void setCodpremio(BigDecimal codpremio) {
		this.codpremio = codpremio;
	}

	public BigDecimal getCodtipoper() {
		return codtipoper;
	}

	public void setCodtipoper(BigDecimal codtipoper) {
		this.codtipoper = codtipoper;
	}

	private void buildNota(DynamicVO cabVO, JdbcWrapper jdbc) throws Exception {
		codpremio = NotaDAO.getRewardId(cabVO);
		codtipoper = NotaDAO.getCodtipoper(cabVO);
		adVlrdesc = NotaDAO.getManualDiscount(cabVO);
		vlrtot = NotaDAO.getTotalValue(cabVO, jdbc);
		descontoPremio = NotaDAO.getDiscount(codpremio, vlrtot, jdbc);
		vlrdesctot = descontoPremio.add(adVlrdesc);
		percdesc = vlrdesctot.divide(vlrtot, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

		setVlrtot(vlrtot.subtract(vlrdesctot));
	}
}
