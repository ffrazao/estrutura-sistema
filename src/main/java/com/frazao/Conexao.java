package com.frazao;

public interface Conexao<T> {

	public T criarConexao(Argumentos argumentos) throws Exception;

}
