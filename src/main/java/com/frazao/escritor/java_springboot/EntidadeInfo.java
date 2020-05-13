package com.frazao.escritor.java_springboot;

import org.apache.commons.text.CaseUtils;

import com.frazao.bd.Esquema;
import com.frazao.bd.Tabela;

public class EntidadeInfo {

	public String pacote;

	public String pacoteFinal;

	public String nome;
	
	public String esquema;
	
	public String tabela;

	public EntidadeInfo(Esquema esquema, Tabela tabela) {
		this.pacote = esquema.getNome();
		this.nome = CaseUtils.toCamelCase(tabela.getNome(), true, new char[] { '_', '.' });
		
		this.esquema = esquema.getNome();
		this.tabela = tabela.getNome();
	}

}
