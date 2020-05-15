package com.frazao.escritor.java_springboot.estrutura;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.CaseUtils;

import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Tabela;

public class EntidadeInfo {

	public String pacote;

	public String pacoteFinal;

	public String nome;
	
	public Esquema esquema;
	
	public Tabela tabela;
	
	public List<PropriedadeInfo> propriedadeInfoList = new ArrayList<>();

	public EntidadeInfo(Esquema esquema, Tabela tabela) {
		this.pacote = esquema.getNome();
		this.nome = CaseUtils.toCamelCase(tabela.getNome(), true, new char[] { '_', '.' });
		
		this.esquema = esquema;
		this.tabela = tabela;
	}
	
	public EntidadeInfo addPropriedadeInfo(PropriedadeInfo valor) {
		this.propriedadeInfoList.add(valor);
		return this;
	}

}
