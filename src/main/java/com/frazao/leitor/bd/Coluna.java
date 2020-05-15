package com.frazao.leitor.bd;

public class Coluna extends Nomeavel {
	
	private boolean chavePrimaria;

	private String complemento;
	
	private boolean permiteNulo;
	
	private String tipo;
	
	public Coluna(String nome) {
		super(nome);
	}

	public String getComplemento() {
		return complemento;
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
