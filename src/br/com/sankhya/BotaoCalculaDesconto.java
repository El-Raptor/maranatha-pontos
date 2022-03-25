package br.com.sankhya;

import br.com.sankhya.controller.ControleCalculo;
import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
//import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.model.Nota;
import br.com.sankhya.modelcore.MGEModelException;

/**
 * Este programa realiza o cálculo de desconto do prêmio selecionado disponível
 * para o cliente da nota.
 * 
 * @author Felipe S. Lopes (felipe.lopes@sankhya.com.br)
 * @since 2022-02-15
 * @version 1.1.0
 * 
 */
public class BotaoCalculaDesconto implements EventoProgramavelJava {

	@Override
	public void beforeInsert(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void beforeUpdate(PersistenceEvent persistEvent) throws Exception {

		/*
		 * String name = persistEvent.getTargetDAO().getEntityName(); boolean teste =
		 * true;
		 * 
		 * if (teste) throw new MGEModelException(name);
		 */

		DynamicVO cabVO = (DynamicVO) persistEvent.getVo();

		SessionHandle hnd = null;
		JdbcWrapper jdbc = persistEvent.getJdbcWrapper();

		try {
			hnd = JapeSession.open();
			jdbc.openSession();

			Nota nota = new Nota(cabVO, jdbc);

			if (ControleCalculo.validate(nota.getCodpremio(), nota.getCodtipoper())) {
				/* Verifica se tem pelo menos um item selecionado antes de continuar */
				/* a execução do código. */
				if (!ControleCalculo.haveItemsCheck(nota.getItensQty()))
					throw new MGEModelException(nota.getCodpremio() + " Por favor! Selecione pelo menos um item"
							+ " antes de selecionar o prêmio.");

				nota.buildNota(cabVO, jdbc);
			} else
				return;

			cabVO.setProperty("VLRDESCTOT", nota.getVlrdesctot());
			cabVO.setProperty("PERCDESC", nota.getPercdesc());
			cabVO.setProperty("AD_DESCPREMIO", nota.getDescontoPremio());
			//cabVO.setProperty("VLRNOTA", nota.getVlrtot());
			//cabVO.setProperty("BASEICMS", nota.getBaseicms());

			//calculaImposto(jdbc, nota, cabVO);

		} catch (MGEModelException m) {
			throw m;
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

	/*private void calculaImposto(JdbcWrapper jdbc, Nota nota, DynamicVO cabVO) throws Exception {
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("UPDATE TGFDIN ");
		sql.appendSql("SET BASE = " + nota.getVlrtot());
		sql.appendSql(", BASERED = " + nota.getVlrtot());
		sql.appendSql(", VALOR = " + cabVO.asBigDecimal("VLRICMS"));
		sql.appendSql(" WHERE NUNOTA = " + nota.getNunota());

		sql.executeUpdate();
	}*/
}
