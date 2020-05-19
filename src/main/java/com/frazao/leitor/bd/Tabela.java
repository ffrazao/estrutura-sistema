package com.frazao.leitor.bd;

import java.util.ArrayList;
import java.util.List;

public class Tabela extends Nomeavel {

	private List<Coluna> colunas = new ArrayList<>();

	private Esquema esquema;

	private List<Relacionamento> relacionamentos = new ArrayList<>();

	public Tabela(Esquema esquema, String nome) {
		super(nome);
		this.esquema = esquema;
	}

	public Coluna add(Coluna valor) {
		this.colunas.add(valor);
		return valor;
	}

	public Relacionamento addRelacionamento(Relacionamento valor) {
		this.relacionamentos.add(valor);
		return valor;
	}

	public List<Coluna> getColunas() {
		return colunas;
	}

	public Esquema getEsquema() {
		return esquema;
	}

	public List<Relacionamento> getRelacionamentos() {
		return relacionamentos;
	}

}
