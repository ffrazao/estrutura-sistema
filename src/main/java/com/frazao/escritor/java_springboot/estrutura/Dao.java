package com.frazao.escritor.java_springboot.estrutura;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Dao extends EstruturaBasica {

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

	public Dao(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {

	}
}