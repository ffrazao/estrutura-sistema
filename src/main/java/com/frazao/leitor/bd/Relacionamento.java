package com.frazao.leitor.bd;

import java.util.HashMap;
import java.util.Map;

public class Relacionamento {
	
	private Map<Coluna, Coluna> colunas = new HashMap<>();
	
	private Tabela tabelaRef;

	public Relacionamento(Tabela tabelaRef) {
		this.tabelaRef = tabelaRef;
	}

	public Relacionamento addColuna(Coluna chaveEstrangeira, Coluna chavePrimaria) {
		this.colunas.put(chaveEstrangeira, chavePrimaria);
		return this;
	}
	
	public Map<Coluna, Coluna> getColunas() {
		return this.colunas;
	}
	
	public Tabela getTabelaRef() {
		return tabelaRef;
	}

}
