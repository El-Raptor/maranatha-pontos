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
	public void beforeInsert(PersistenceEvent persistEvent) throws Exception {}

	@Override
	public void beforeUpdate(PersistenceEvent persistEvent) throws Exception {
		DynamicVO cabVO = (DynamicVO) persistEvent.getVo();
		BigDecimal nunota = cabVO.asBigDecimal("NUNOTA");
		BigDecimal codpremio = cabVO.asBigDecimal("AD_CODPREMIO") != null 
				? cabVO.asBigDecimal("AD_CODPREMIO") : null;

		if (codpremio == null)
			return;

		SessionHandle hnd = null;
		JdbcWrapper jdbc = persistEvent.getJdbcWrapper();
		NativeSql sql = null;
		ResultSet rset = null;

		try {
			hnd = JapeSession.open();
			jdbc.openSession();
			BigDecimal descontoPremio = BigDecimal.ZERO;
			BigDecimal vlrdesctot = BigDecimal.ZERO;
			BigDecimal vlrtot = BigDecimal.ZERO;
			BigDecimal percdesc = BigDecimal.ZERO;
			BigDecimal adVlrdesc = cabVO.asBigDecimal("AD_VLRDESC") != null 
					? cabVO.asBigDecimal("AD_VLRDESC") : BigDecimal.ZERO;
			
			sql = new NativeSql(jdbc);
			sql.appendSql("SELECT SUM(COALESCE(VLRTOT, 0)) ");
			sql.appendSql("FROM TGFITE ");
			sql.appendSql("WHERE NUNOTA = :NUNOTA");

			sql.setNamedParameter("NUNOTA", nunota);

			rset = sql.executeQuery();

			if (rset.next())
				vlrtot = rset.getBigDecimal(1);
				
			sql = new NativeSql(jdbc);
			sql.appendSql("SELECT CASE WHEN PTS.TIPO = 'B' THEN PTS.VALOR ");
			sql.appendSql("ELSE (PTS.VALOR/100) * :VLRTOT END ");
			sql.appendSql("FROM AD_TGFPTS PTS JOIN AD_TGFPRE PRM ON PTS.NIVEL = PRM.NIVEL ");
			sql.appendSql("WHERE PRM.CODPREMIO = :CODPREMIO");

			sql.setNamedParameter("VLRTOT", vlrtot);
			sql.setNamedParameter("CODPREMIO", codpremio);

			rset = sql.executeQuery();

			if (rset.next())
				descontoPremio = rset.getBigDecimal(1);

			vlrdesctot = descontoPremio.add(adVlrdesc);
			percdesc = vlrdesctot.divide(vlrtot, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

			//boolean teste = true;
			
			/*if (teste) {
				throw new MGEModelException("desconto " + adVlrdesc + " " + percdesc + "%");
			}*/
			
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
