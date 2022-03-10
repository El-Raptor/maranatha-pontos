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
	private int itemsQty;


	public Nota(DynamicVO cabVO, JdbcWrapper jdbc) throws Exception {
		codpremio = NotaDAO.getRewardId(cabVO);
		codtipoper = NotaDAO.getCodtipoper(cabVO);
		itemsQty = NotaDAO.getItemsQuantity(cabVO, jdbc);
	}

	public int getItensQty() {
		return itemsQty;
	}
	
	public void setItensQty(int itensQty) {
		this.itemsQty = itensQty;
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

	public void buildNota(DynamicVO cabVO, JdbcWrapper jdbc) throws Exception {
		setAdVlrdesc(NotaDAO.getManualDiscount(cabVO));
		setVlrtot(NotaDAO.getTotalValue(cabVO, jdbc));
		setDescontoPremio(NotaDAO.getDiscount(this.codpremio, vlrtot, jdbc));
		setVlrdesctot(descontoPremio.add(adVlrdesc));
		setPercdesc(vlrdesctot.divide(vlrtot, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
		setVlrtot(vlrtot.subtract(vlrdesctot));	
	}
	
	//TODO: Método que calcula os impostos.
	//TODO: Método que verifica se o valor de desconto é maior q o valor da nota após impostos.
}
