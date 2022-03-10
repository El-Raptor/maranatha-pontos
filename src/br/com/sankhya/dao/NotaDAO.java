package br.com.sankhya.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;

import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;
import br.com.sankhya.jape.vo.DynamicVO;

public class NotaDAO {

	public static BigDecimal getRewardId(DynamicVO cabVO) {
		BigDecimal codpremio = cabVO.asBigDecimal("AD_CODPREMIO") != null ? cabVO.asBigDecimal("AD_CODPREMIO")
				: BigDecimal.ZERO;

		return codpremio;
	}

	public static BigDecimal getTotalValue(DynamicVO cabVO, JdbcWrapper jdbc) throws Exception {
		BigDecimal nunota = cabVO.asBigDecimal("NUNOTA");
		BigDecimal vlrtot = BigDecimal.ZERO;

		ResultSet rset = getTotalValue(nunota, jdbc);

		if (rset.next())
			vlrtot = rset.getBigDecimal(1);

		return vlrtot;
	}

	private static ResultSet getTotalValue(BigDecimal nunota, JdbcWrapper jdbc) throws Exception {
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT SUM(COALESCE(VLRTOT, 0)) ");
		sql.appendSql("FROM TGFITE ");
		sql.appendSql("WHERE NUNOTA = :NUNOTA");

		sql.setNamedParameter("NUNOTA", nunota);

		ResultSet rset = sql.executeQuery();

		return rset;
	}

	public static BigDecimal getDiscount(BigDecimal codpremio, BigDecimal vlrtot, JdbcWrapper jdbc) throws Exception {
		BigDecimal descontoPremio = BigDecimal.ZERO;
		NativeSql sql = new NativeSql(jdbc);

		sql.appendSql("SELECT CASE WHEN PTS.TIPO = 'B' THEN PTS.VALOR ");
		sql.appendSql("ELSE (PTS.VALOR/100) * :VLRTOT END ");
		sql.appendSql("FROM AD_TGFPTS PTS JOIN AD_TGFPRE PRM ON PTS.NIVEL = PRM.NIVEL ");
		sql.appendSql("WHERE PRM.CODPREMIO = :CODPREMIO");

		sql.setNamedParameter("VLRTOT", vlrtot);
		sql.setNamedParameter("CODPREMIO", codpremio);

		ResultSet rset = sql.executeQuery();

		if (rset.next())
			descontoPremio = rset.getBigDecimal(1);

		return descontoPremio;
	}

	public static int getItemsQuantity(DynamicVO cabVO, JdbcWrapper jdbc) throws Exception {
		NativeSql sql = new NativeSql(jdbc);
		BigDecimal nunota = cabVO.asBigDecimal("NUNOTA");
		int itemsQty = 0;
		
		sql.appendSql("SELECT COUNT(ite.sequencia) ");
		sql.appendSql("FROM TGFITE ite ");
		sql.appendSql("WHERE ite.NUNOTA = " + nunota);
		
		ResultSet rset = sql.executeQuery();
		
		if (rset.next())
			itemsQty = 0;
		
		return itemsQty;
	}
	
	public static BigDecimal getManualDiscount(DynamicVO cabVO) {
		return cabVO.asBigDecimal("AD_VLRDESC") != null ? cabVO.asBigDecimal("AD_VLRDESC") : BigDecimal.ZERO;
	}

	public static BigDecimal getCodtipoper(DynamicVO cabVO) {
		return cabVO.asBigDecimal("CODTIPOPER");
	}

}
