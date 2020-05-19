package com.frazao.leitor.bd;

public class Coluna extends Nomeavel {
	
	private boolean chavePrimaria;

	private String complemento;
	
	private Esquema esquema;
	
	private boolean permiteNulo;
	
	private Tabela tabela;
	
	private String tipo;
	
	public Coluna(Esquema esquema, Tabela tabela, String nome) {
		super(nome);
		this.esquema = esquema;
		this.tabela = tabela;
	}

	public String getComplemento() {
		return complemento;
	}

	public Esquema getEsquema() {
		return esquema;
	}

	public Tabela getTabela() {
		return tabela;
	}

	public String getTipo() {
		return tipo;
	}

	public boolean isChavePrimaria() {
		return chavePrimaria;
	}

	public boolean isPermiteNulo() {
		return permiteNulo;
	}

	public void setChavePrimaria(boolean chavePrimaria) {
		this.chavePrimaria = chavePrimaria;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setPermiteNulo(boolean permiteNulo) {
		this.permiteNulo = permiteNulo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
