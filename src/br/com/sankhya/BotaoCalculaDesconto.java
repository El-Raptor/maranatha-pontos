package br.com.sankhya;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.EntityFacade;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.core.JapeSession.SessionHandle;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.modelcore.MGEModelException;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

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
	}

	@Override
	public void beforeDelete(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void afterInsert(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void afterUpdate(PersistenceEvent persistEvent) throws Exception {
		DynamicVO newValue = (DynamicVO) persistEvent.getVo();
		DynamicVO oldValue = (DynamicVO) persistEvent.getOldVO();

		BigDecimal nunota = newValue.asBigDecimal("NUNOTA");
		BigDecimal codpremio = newValue.asBigDecimal("AD_CODPREMIO") != null 
				? newValue.asBigDecimal("AD_CODPREMIO")
				: null;

		if (codpremio == null) 
			return;
		
		BigDecimal vlrnota = newValue.asBigDecimal("VLRNOTA");
		
		JdbcWrapper jdbc = null;
		NativeSql sql = null;
		ResultSet rset = null;
		SessionHandle hnd = null;
		BigDecimal vlrDesconto = null;
		
		try {
			hnd = JapeSession.open();
			hnd.setFindersMaxRows(-1);
			EntityFacade entity = EntityFacadeFactory.getDWFFacade();
			jdbc = entity.getJdbcWrapper();
			jdbc.openSession();
			
			sql = new NativeSql(jdbc);
			
			jdbc.openSession();
			
			sql.appendSql("SELECT CASE WHEN PTS.TIPO = 'B' THEN PTS.VALOR ");
			sql.appendSql("ELSE (PTS.VALOR/100) * :VLRNOTA END ");
			sql.appendSql("FROM AD_TGFPTS JOIN AD_TGFPRE PRM ON PTS.NIVEL = PRM.NIVEL ");
			sql.appendSql("WHERE PRM.CODPREMIO = :CODPREMIO");
			
			sql.setNamedParameter("VLRNOTA", vlrnota);
			sql.setNamedParameter("CODPREMIO", codpremio);
			
			rset = sql.executeQuery();
			
			if (rset.next())
				vlrDesconto = rset.getBigDecimal(0);
			
			JapeWrapper cabecalhoDAO = JapeFactory.dao("Nota/Pedido");
			DynamicVO save = cabecalhoDAO.create()
					.set("AD_DESCPREMIO", vlrDesconto)
					.set("VLRDESCTOT", vlrDesconto)
					.save();
			
		} catch (Exception e) {
			MGEModelException.throwMe(e);
		} finally {
			rset.close();
			NativeSql.releaseResources(sql);
			JdbcWrapper.closeSession(jdbc);
			JapeSession.close(hnd);
		}
		
		
	}

	@Override
	public void afterDelete(PersistenceEvent persistEvent) throws Exception {
	}

	@Override
	public void beforeCommit(TransactionContext paramTransactionContext) throws Exception {
	}

}
