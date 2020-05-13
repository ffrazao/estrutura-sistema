package com.frazao.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.lang3.StringUtils;

import com.frazao.Argumentos;
import com.frazao.Conexao;

public class BDConexao implements Conexao<Connection> {

	@Override
	public Connection getConexao(Argumentos argumentos) throws SQLException {
		if (StringUtils.isBlank(argumentos.url)) {
			throw new SQLException("Url de conexão não informada");
		}
		
		String u = argumentos.url;

		Connection con = null;
		if (StringUtils.isNoneBlank(argumentos.usuario, argumentos.senha)) {
			con = DriverManager.getConnection(u, argumentos.usuario, argumentos.senha);
		} else {
			con = DriverManager.getConnection(u);
		}

		return con;
	}

}
