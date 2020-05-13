package com.frazao.bd;

import java.util.ArrayList;
import java.util.List;

public class Relacionamento {
	
	private List<Coluna> colunas = new ArrayList<>();

	private Tabela tabela;

	public List<Coluna> getColunas() {
		return colunas;
	}
	
	public Tabela getTabela() {
		return tabela;
	}

	public void setColunas(List<Coluna> colunas) {
		this.colunas = colunas;
	}

	public void setTabela(Tabela tabela) {
		this.tabela = tabela;
	}
	
}
