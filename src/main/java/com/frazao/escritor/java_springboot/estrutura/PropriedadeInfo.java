package com.frazao.escritor.java_springboot.estrutura;

import org.apache.commons.text.CaseUtils;

import com.frazao.leitor.bd.Coluna;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Tabela;

public class PropriedadeInfo {

	private Coluna coluna;

	private EntidadeInfo entidadeInfo;

	private Esquema esquema;
	
	private String nome;

	private PropriedadeInfo refExterna;

	private Tabela tabela;

	private Class<?> tipoJava;

	public PropriedadeInfo(EntidadeInfo entidadeInfo, Coluna coluna) {
		super();
		this.entidadeInfo = entidadeInfo;
		this.esquema = entidadeInfo.esquema;
		this.tabela = entidadeInfo.tabela;
		this.coluna = coluna;
		this.nome = CaseUtils.toCamelCase(this.coluna.getNome(), false, new char[] { '_', '.' });
	}

	public Coluna getColuna() {
		return coluna;
	}

	public EntidadeInfo getEntidadeInfo() {
		return entidadeInfo;
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

	public Class<?> getTipoJava() {
		return tipoJava;
	}

	public void setRefExterna(PropriedadeInfo refExterna) {
		this.refExterna = refExterna;
	}

	public void setTipoJava(Class<?> tipoJava) {
		this.tipoJava = tipoJava;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}

}