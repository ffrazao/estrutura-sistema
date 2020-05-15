package com.frazao.escritor.java_springboot.estrutura;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Filtro extends EstruturaBasica {

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

	public Filtro(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {

	}
}
