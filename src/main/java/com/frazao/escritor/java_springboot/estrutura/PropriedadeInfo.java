package com.frazao.escritor.java_springboot.estrutura;

import org.apache.commons.text.CaseUtils;

import com.frazao.leitor.bd.Coluna;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Tabela;

public class PropriedadeInfo {

	private Coluna coluna;

	private Esquema esquema;

	private String nome;

	private PropriedadeInfo refExterna;

	private Tabela tabela;

	private String tipoJava;

	public PropriedadeInfo(Esquema esquema, Tabela tabela, Coluna coluna) {
		super();
		this.esquema = esquema;
		this.tabela = tabela;
		this.coluna = coluna;
		this.nome = CaseUtils.toCamelCase(this.coluna.getNome(), false, new char[] { '_', '.' });
	}

	public Coluna getColuna() {
		return coluna;
	}

	public Esquema getEsquema() {
		return esquema;
	}

	public String getNome() {
		return nome;
	}

	public PropriedadeInfo getRefExterna() {
		return refExterna;
	}

	public Tabela getTabela() {
		return tabela;
	}

	public String getTipoJava() {
		return tipoJava;
	}

	public void setRefExterna(PropriedadeInfo refExterna) {
		this.refExterna = refExterna;
	}

}