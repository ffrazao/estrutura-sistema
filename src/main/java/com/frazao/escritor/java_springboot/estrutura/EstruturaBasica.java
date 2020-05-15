package com.frazao.escritor.java_springboot.estrutura;

import com.frazao.escritor.Escritor;

public abstract class EstruturaBasica {

	protected Escritor escritor;

	public EstruturaBasica(Escritor escritor) {
		super();
		this.escritor  = escritor;
	}
	
	public abstract void escrever() throws Exception;

}
