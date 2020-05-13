package com.frazao.escritor.java_springboot;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

class DominioInfo {

	public List<String> local;

	public String nome;

	public String[] valor;
	
	public String pacote;
	
	public String pacoteFinal;

	DominioInfo(String[] valor, List<String> local, String nome) {
		set(valor, local, nome);
	}

	DominioInfo(String[] valor, String local, String nome) {
		List<String> l = new ArrayList<>();
		l.add(local);
		set(valor, l, nome);
	}

	DominioInfo(String[] valor, String nome) {
		List<String> l = new ArrayList<>();
		set(valor, l, nome);
	}

	private void set(String[] valor, List<String> local, String nome) {
		this.valor = valor;
		this.local = local;
		this.nome = nome;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}