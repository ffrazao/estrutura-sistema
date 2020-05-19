package com.frazao.escritor.java_springboot.estrutura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.text.CaseUtils;

import com.frazao.leitor.bd.Coluna;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Tabela;

public class EntidadeInfo {

	public String pacote;

	public String pacoteFinal;

	public String nome;

	public Esquema esquema;

	public Tabela tabela;

	public List<PropriedadeInfo> propriedadeInfoList = new ArrayList<>();

	private List<EntidadeInfo> list = new ArrayList<>();

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

	public PropriedadeInfo getPropriedade(String nome) {
		for (PropriedadeInfo pi : propriedadeInfoList) {
			if (pi.getNome().equals(nome)) {
				return pi;
			}
		}
		return null;
	}

	public PropriedadeInfo getPropriedade(Coluna coluna) {
		for (PropriedadeInfo pi : propriedadeInfoList) {
			if (pi.getColuna().getNome().equals(coluna.getNome())) {
				return pi;
			}
		}
		return null;
	}

	public void addList(EntidadeInfo entidadeInfo) {
		if (this.list.stream().filter(i -> i.nome.equals(entidadeInfo.nome)).collect(Collectors.toList()).size() == 0) {
			this.list.add(entidadeInfo);
		}
	}
	
	public List<EntidadeInfo> getList() {
		return this.list;
	}

}
