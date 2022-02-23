package br.com.sankhya.controller;

import java.math.BigDecimal;

public class ControleCalculo {

	public static boolean validate(BigDecimal codpremio, BigDecimal codtipoper) {
		return checkReward(codpremio) && checkTOP(codtipoper);
	}

	private static boolean checkReward(BigDecimal codpremio) {
		return !codpremio.equals(BigDecimal.ZERO);
	}

	private static boolean checkTOP(BigDecimal codtipoper) {
		if (codtipoper.equals(new BigDecimal(1000)))
			return true;

		if (codtipoper.equals(new BigDecimal(1001)))
			return true;

		if (codtipoper.equals(new BigDecimal(1002)))
			return true;

		if (codtipoper.equals(new BigDecimal(1100)))
			return true;

		if (codtipoper.equals(new BigDecimal(1101)))
			return true;

		if (codtipoper.equals(new BigDecimal(1111)))
			return true;

		if (codtipoper.equals(new BigDecimal(1117)))
			return true;

		if (codtipoper.equals(new BigDecimal(1200)))
			return true;

		return false;
	}

}
