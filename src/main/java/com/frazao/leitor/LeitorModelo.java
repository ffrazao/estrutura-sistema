package com.frazao.leitor;

public enum LeitorModelo {

	MYSQL("jdbc:mysql://localhost/", new String[] { "!information_schema", "!mysql", "!performance_schema", "!sys" });

	public static LeitorModelo getPadrao() {
		return LeitorModelo.MYSQL;
	}
	
	private String[] esquemasAIgnorar;

	private String urlBasica;

	private LeitorModelo(String urlBasica, String[] esquemasAIgnorar) {
		this.urlBasica = urlBasica;
		this.esquemasAIgnorar = esquemasAIgnorar;
	}

	public String[] getEsquemasAIgnorar() {
		return this.esquemasAIgnorar;
	}

	public String getUrlBasica() {
		return this.urlBasica;
	}
}
