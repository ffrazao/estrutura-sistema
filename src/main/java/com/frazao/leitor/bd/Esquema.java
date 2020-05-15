package com.frazao.leitor.bd;

import java.util.ArrayList;
import java.util.List;

public class Esquema extends Nomeavel {
	
	public Esquema(String nome) {
		super(nome);
	}

	private List<Tabela> tabelas = new ArrayList<>();

	public List<Tabela> getTabelas() {
		return tabelas;
	}

	public Tabela add(Tabela valor) {
		this.tabelas.add(valor);
		return valor;
	}

}
