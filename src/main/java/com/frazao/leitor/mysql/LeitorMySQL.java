package com.frazao.leitor.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.frazao.Argumentos;
import com.frazao.bd.BDConexao;
import com.frazao.bd.Coluna;
import com.frazao.bd.Esquema;
import com.frazao.bd.Tabela;
import com.frazao.leitor.Leitor;

public class LeitorMySQL implements Leitor {

	private Argumentos argumentos;

	public LeitorMySQL(Argumentos argumentos) throws SQLException {
		this.argumentos = argumentos;
	}

	private ResultSet abrirColunas(Connection con, String esquema, String tabela) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COLUMN_NAME AS nome,").append("\n");
		sql.append("       COLUMN_KEY AS chave_primaria,").append("\n");
		sql.append("       IS_NULLABLE AS permite_nulo,").append("\n");
		sql.append("       DATA_TYPE AS tipo,").append("\n");
		sql.append("       COLUMN_TYPE AS complemento,").append("\n");
		sql.append("       CHARACTER_MAXIMUM_LENGTH AS tamanho").append("\n");
		sql.append("FROM   INFORMATION_SCHEMA.columns").append("\n");
		sql.append("WHERE  TABLE_SCHEMA = ?").append("\n");
		sql.append("AND    TABLE_NAME = ?").append("\n");
		sql.append("ORDER BY ORDINAL_POSITION").append("\n");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setString(1, esquema);
		ps.setString(2, tabela);

		return ps.executeQuery();
	}

	private ResultSet abrirEsquema(Connection con) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT TABLE_SCHEMA as esquema").append("\n");
		sql.append("FROM            INFORMATION_SCHEMA.TABLES").append("\n");
		sql.append("WHERE           1 = 1").append("\n");
		List<String> naoTemP = new ArrayList<>(), temP = new ArrayList<>();
		if (!StringUtils.isAllBlank(this.argumentos.esquema.toArray(new String[this.argumentos.esquema.size()]))) {
			StringBuilder naoTem = new StringBuilder(), tem = new StringBuilder();
			for (int i = 0; i < this.argumentos.esquema.size(); i++) {
				String esquema = this.argumentos.esquema.get(i);
				if (StringUtils.isNotBlank(esquema)) {
					if (esquema.startsWith("!")) {
						naoTem.append(naoTem.length() == 0 ? "AND             TABLE_SCHEMA NOT IN(?" : ", ?");
						naoTemP.add(esquema.replaceFirst("\\!", ""));
					} else {
						tem.append(tem.length() == 0 ? "AND             TABLE_SCHEMA IN(?" : ", ?");
						temP.add(esquema);
					}
				}
			}
			if (naoTem.length() > 0) {
				sql.append(naoTem).append(")").append("\n");
			}
			if (tem.length() > 0) {
				sql.append(tem).append(")").append("\n");
			}
		}
		sql.append("ORDER BY 1").append("\n");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		if (!StringUtils.isAllBlank(this.argumentos.esquema.toArray(new String[this.argumentos.esquema.size()]))) {
			int ctd = 0;
			for (String v : naoTemP)
				ps.setString(++ctd, v);
			for (String v : temP)
				ps.setString(++ctd, v);
		}
		return ps.executeQuery();
	}

	private ResultSet abrirRelacionamentos(Connection con, String esquema, String tabela) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT    COLUMN_NAME as nome,").append("\n");
		sql.append("          REFERENCED_TABLE_SCHEMA as ref_esquema,").append("\n");
		sql.append("          REFERENCED_TABLE_NAME as ref_tabela,").append("\n");
		sql.append("          REFERENCED_COLUMN_NAME as ref_coluna").append("\n");
		sql.append("FROM      INFORMATION_SCHEMA.KEY_COLUMN_USAGE").append("\n");
		sql.append("WHERE     TABLE_SCHEMA = ?").append("\n");
		sql.append("  AND     TABLE_NAME   = ?").append("\n");
		sql.append("  AND     REFERENCED_TABLE_NAME IS NOT NULL").append("\n");
		sql.append("ORDER BY 1,2,3").append("\n");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		ps.setString(1, esquema);
		ps.setString(2, tabela);
		return ps.executeQuery();
	}

	private ResultSet abrirTabela(Connection con, String esquema) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT   TABLE_NAME as tabela").append("\n");
		sql.append("FROM     INFORMATION_SCHEMA.TABLES").append("\n");
		sql.append("WHERE    TABLE_SCHEMA = ?").append("\n");
		List<String> naoTemP = new ArrayList<>(), temP = new ArrayList<>();
		if (!StringUtils.isAllBlank(this.argumentos.tabela.toArray(new String[this.argumentos.tabela.size()]))) {
			StringBuilder naoTem = new StringBuilder(), tem = new StringBuilder();
			for (int i = 0; i < this.argumentos.tabela.size(); i++) {
				String tabela = this.argumentos.tabela.get(i);
				if (StringUtils.isNotBlank(tabela)) {
					if (tabela.startsWith("!")) {
						naoTem.append(naoTem.length() == 0 ? "AND             TABLE_NAME NOT IN(?" : ", ?");
						naoTemP.add(tabela.replaceFirst("\\!", ""));
					} else {
						tem.append(tem.length() == 0 ? "AND             TABLE_NAME IN(?" : ", ?");
						temP.add(tabela);
					}
				}
			}
			if (naoTem.length() > 0) {
				sql.append(naoTem).append(")").append("\n");
			}
			if (tem.length() > 0) {
				sql.append(tem).append(")").append("\n");
			}
		}
		sql.append("ORDER BY 1").append("\n");

		PreparedStatement ps = con.prepareStatement(sql.toString());
		int ctd = 0;
		ps.setString(++ctd, esquema);
		if (!StringUtils.isAllBlank(this.argumentos.tabela.toArray(new String[this.argumentos.tabela.size()]))) {
			for (String v : naoTemP)
				ps.setString(++ctd, v);
			for (String v : temP)
				ps.setString(++ctd, v);
		}
		return ps.executeQuery();
	}

	public List<Esquema> ler() throws Exception {

		try (Connection con = new BDConexao().getConexao(this.argumentos)) {

			List<Esquema> result = new ArrayList<>();

			// listar esquemas
			ResultSet esquemaRs = this.abrirEsquema(con);
			while (esquemaRs.next()) {
				Esquema esquema = new Esquema(esquemaRs.getString("esquema"));
				result.add(esquema);

				// listar tabelas
				ResultSet tabelaRs = this.abrirTabela(con, esquema.toString());
				while (tabelaRs.next()) {
					Tabela tabela = esquema.add(new Tabela(tabelaRs.getString("tabela")));

					// listar Colunas
					ResultSet colunasRs = this.abrirColunas(con, esquema.toString(), tabela.toString());

					while (colunasRs.next()) {
						Coluna coluna = tabela.add(new Coluna(colunasRs.getString("nome")));

						coluna.setChavePrimaria("pri".equalsIgnoreCase(colunasRs.getString("chave_primaria")));
						coluna.setPermiteNulo("yes".equalsIgnoreCase(colunasRs.getString("permite_nulo")));
						coluna.setTipo(colunasRs.getString("tipo"));
						coluna.setComplemento(colunasRs.getString("complemento"));
					}
				}
			}

//			for (Esquema esquema : result) {
//				for (Tabela tabela : esquema.getTabelas()) {
//					// listar relacoes
//					ResultSet relacionamentoRs = this.abrirRelacionamentos(con, esquema.toString(), tabela.toString());
//					while (relacionamentoRs.next()) {
//						Relacionamento relacionamento = new Relacionamento();
//
//						List<String> list = new ArrayList<>();
//						list.add(relacionamentoRs.getString("ref_esquema"));
//						list.add(relacionamentoRs.getString("ref_tabela"));
//						list.add(relacionamentoRs.getString("ref_coluna"));
//						relacionamentoMap.put(relacionamentoRs.getString("nome"), list);
//					}
//				}
//			}

			return result;
		}

	}

}
