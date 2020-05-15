package com.frazao.leitor.bd;

import java.util.ArrayList;
import java.util.List;

public class Tabela extends Nomeavel {
	
	private List<Coluna> colunas = new ArrayList<>();

	private List<Relacionamento> relacionamentos = new ArrayList<>();
	
	public Tabela(String nome) {
		super(nome);
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

	public List<Relacionamento> getRelacionamentos() {
		return relacionamentos;
	}

}
