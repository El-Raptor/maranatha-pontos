package br.com.sankhya;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeUpdate(PersistenceEvent persistEvent) throws Exception {
		DynamicVO cabVO = (DynamicVO) persistEvent.getVo();
		// DynamicVO oldValue = (DynamicVO) persistEvent.getOldVO();

		// BigDecimal nunota = cabVO.asBigDecimal("NUNOTA");

		BigDecimal codpremio = cabVO.asBigDecimal("AD_CODPREMIO") != null ? cabVO.asBigDecimal("AD_CODPREMIO") : null;

		if (codpremio == null)
			return;

		//boolean teste = true;

		BigDecimal vlrnota = cabVO.asBigDecimal("VLRNOTA");

		SessionHandle hnd = null;
		JdbcWrapper jdbc = persistEvent.getJdbcWrapper();
		NativeSql sql = null;
		ResultSet rset = null;
		BigDecimal vlrDesconto = BigDecimal.ZERO;

		try {
			hnd = JapeSession.open();

			jdbc.openSession();

			sql = new NativeSql(jdbc);

			jdbc.openSession();

			sql.appendSql("SELECT CASE WHEN PTS.TIPO = 'B' THEN PTS.VALOR ");
			sql.appendSql("ELSE (PTS.VALOR/100) * :VLRNOTA END ");
			sql.appendSql("FROM AD_TGFPTS PTS JOIN AD_TGFPRE PRM ON PTS.NIVEL = PRM.NIVEL ");
			sql.appendSql("WHERE PRM.CODPREMIO = :CODPREMIO");

			sql.setNamedParameter("VLRNOTA", vlrnota);
			sql.setNamedParameter("CODPREMIO", codpremio);

			rset = sql.executeQuery();

			if (rset.next())
				vlrDesconto = rset.getBigDecimal(1);

			BigDecimal percdesc = vlrDesconto.divide(vlrnota, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

			cabVO.setProperty("VLRDESCTOT", vlrDesconto);
			cabVO.setProperty("PERCDESC", percdesc);
			cabVO.setProperty("AD_DESCPREMIO", vlrDesconto);

			/*ServiceContext serviceCtx = ServiceContext.getCurrent();
			PrePersistEntityState cabState = PrePersistEntityState.build(EntityFacadeFactory.getDWFFacade(),
					DynamicEntityNames.CABECALHO_NOTA, cabVO);

			/*if (teste) {
				throw new MGEModelException("service " + serviceCtx);
			}

			CACHelper cacHelper = new CACHelper();
			cacHelper.incluirAlterarCabecalho(serviceCtx, cabState);*/
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
	public void afterUpdate(PersistenceEvent persistEvent) throws Exception {}

	@Override
	public void afterDelete(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void beforeCommit(TransactionContext paramTransactionContext) throws Exception {
	}

}
