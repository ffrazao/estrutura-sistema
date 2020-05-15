package com.frazao.leitor.bd;

public class Nomeavel {
	
	private String nome;

	public Nomeavel(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}
	
	@Override
	public String toString() {
		return this.nome;
	}

}
