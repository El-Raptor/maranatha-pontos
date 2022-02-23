package br.com.sankhya;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.sankhya.controller.CalculaDesconto;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.modelcore.MGEModelException;

/**
 * Este programa realiza o cálculo de desconto do prêmio selecionado disponível
 * para o cliente da nota.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-15
 * @version 0.1
 * 
 */
public class BotaoCalculaDesconto implements EventoProgramavelJava {

	@Override
	public void beforeInsert(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void beforeUpdate(PersistenceEvent persistEvent) throws Exception {
		DynamicVO cabVO = (DynamicVO) persistEvent.getVo();

		BigDecimal codpremio = CalculaDesconto.getRewardId(cabVO);

		if (CalculaDesconto.checkReward(codpremio))
			return;

		/*
		 * boolean teste = true;
		 * 
		 * if (teste) { throw new MGEModelException("passou!"); }
		 */

		SessionHandle hnd = null;
		JdbcWrapper jdbc = persistEvent.getJdbcWrapper();

		try {
			hnd = JapeSession.open();
			jdbc.openSession();

			BigDecimal vlrdesctot = BigDecimal.ZERO;
			BigDecimal percdesc = BigDecimal.ZERO;
			BigDecimal adVlrdesc = CalculaDesconto.getManualDiscount(cabVO);
			BigDecimal vlrtot = CalculaDesconto.getTotalValue(cabVO, jdbc);
			BigDecimal descontoPremio = CalculaDesconto.getDiscount(codpremio, vlrtot, jdbc);

			vlrdesctot = descontoPremio.add(adVlrdesc);
			percdesc = vlrdesctot.divide(vlrtot, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

			// boolean teste = true;

			/*
			 * if (teste) { throw new MGEModelException("desconto " + adVlrdesc + " " +
			 * percdesc + "%"); }
			 */

			cabVO.setProperty("VLRDESCTOT", vlrdesctot);
			cabVO.setProperty("PERCDESC", percdesc);
			cabVO.setProperty("AD_DESCPREMIO", descontoPremio);
			cabVO.setProperty("VLRNOTA", vlrtot.subtract(vlrdesctot));

		} catch (Exception e) {
			e.printStackTrace();
			MGEModelException.throwMe(e);
		} finally {
			JdbcWrapper.closeSession(jdbc);
			JapeSession.close(hnd);
		}
	}

	@Override
	public void beforeDelete(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void afterInsert(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void afterUpdate(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void afterDelete(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void beforeCommit(TransactionContext paramTransactionContext) throws Exception {
	}

}
