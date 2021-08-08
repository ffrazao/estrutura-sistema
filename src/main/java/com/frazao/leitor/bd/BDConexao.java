package com.frazao.leitor.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.frazao.Argumentos;
import com.frazao.Conexao;

public class BDConexao implements Conexao<Connection> {

	@Override
	public Connection criarConexao(Argumentos argumentos) throws SQLException {
		if (StringUtils.isBlank(argumentos.url)) {
			throw new SQLException("URL de conexão ao banco de dados não informada");
		}

		Connection result = null;
		if (StringUtils.isNoneBlank(argumentos.usuario, argumentos.senha)) {
			result = DriverManager.getConnection(argumentos.url, argumentos.usuario, argumentos.senha);
		} else {
			result = DriverManager.getConnection(argumentos.url);
		}

		return result;
	}

}
