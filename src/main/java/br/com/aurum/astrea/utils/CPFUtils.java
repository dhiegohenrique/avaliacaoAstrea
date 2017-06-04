package br.com.aurum.astrea.utils;

import org.apache.commons.lang3.StringUtils;

public class CPFUtils {
	
	private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

	private static int calcularDigito(String str, int[] peso) {
		int soma = 0;
		for (int indice=str.length()-1, digito; indice >= 0; indice-- ) {
			digito = Integer.parseInt(str.substring(indice,indice+1));
			soma += digito*peso[peso.length-str.length()+indice];
		}
		soma = 11 - soma % 11;
		return soma > 9 ? 0 : soma;
	}

	public static boolean isValidCPF(String cpf) {
		if (StringUtils.isBlank(cpf)) {
			return false;
		}
		
		cpf = normalizeCpf(cpf);
		if (cpf.length() != 11) {
			return false;
		}
		
		Integer digito1 = calcularDigito(cpf.substring(0,9), pesoCPF);
		Integer digito2 = calcularDigito(cpf.substring(0,9) + digito1, pesoCPF);
		return cpf.equals(cpf.substring(0,9) + digito1.toString() + digito2.toString());
	}
	
	public static String normalizeCpf(String cpf) {
		String normalizeCpf = cpf;
		return normalizeCpf.replaceAll("[^0-9]", "");
	}
}