package com.frazao.leitor.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.frazao.Argumentos;
import com.frazao.leitor.Leitor;
import com.frazao.leitor.bd.BDConexao;
import com.frazao.leitor.bd.Coluna;
import com.frazao.leitor.bd.Esquema;
import com.frazao.leitor.bd.Relacionamento;
import com.frazao.leitor.bd.Tabela;

public class LeitorMySQL implements Leitor {

	private Argumentos argumentos;

	private Connection con;

	public LeitorMySQL(Argumentos argumentos) throws SQLException {
		this.argumentos = argumentos;
		this.con = new BDConexao().getConexao(this.argumentos);
	}

	private ResultSet abrirColunas(String esquema, String tabela) throws SQLException {
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

		PreparedStatement ps = this.con.prepareStatement(sql.toString());
		ps.setString(1, esquema);
		ps.setString(2, tabela);

		return ps.executeQuery();
	}

	private ResultSet abrirEsquema(String esquema) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT TABLE_SCHEMA as esquema").append("\n");
		sql.append("FROM            INFORMATION_SCHEMA.TABLES").append("\n");
		sql.append("WHERE           1 = 1").append("\n");
		List<String> naoTemP = new ArrayList<>(), temP = new ArrayList<>();
		if (StringUtils.isNotBlank(esquema)) {
			sql.append("AND         TABLE_SCHEMA = ?");
		} else {
			if (!StringUtils.isAllBlank(this.argumentos.esquema.toArray(new String[this.argumentos.esquema.size()]))) {
				StringBuilder naoTem = new StringBuilder(), tem = new StringBuilder();
				for (int i = 0; i < this.argumentos.esquema.size(); i++) {
					String esquemaArg = this.argumentos.esquema.get(i);
					if (StringUtils.isNotBlank(esquemaArg)) {
						if (esquemaArg.startsWith("!")) {
							naoTem.append(naoTem.length() == 0 ? "AND             TABLE_SCHEMA NOT IN(?" : ", ?");
							naoTemP.add(esquemaArg.replaceFirst("\\!", ""));
						} else {
							tem.append(tem.length() == 0 ? "AND             TABLE_SCHEMA IN(?" : ", ?");
							temP.add(esquemaArg);
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
		}
		sql.append("ORDER BY 1").append("\n");

		PreparedStatement ps = this.con.prepareStatement(sql.toString());
		if (StringUtils.isNotBlank(esquema)) {
			ps.setString(1, esquema);
		} else if (!StringUtils
				.isAllBlank(this.argumentos.esquema.toArray(new String[this.argumentos.esquema.size()]))) {
			int ctd = 0;
			for (String v : naoTemP)
				ps.setString(++ctd, v);
			for (String v : temP)
				ps.setString(++ctd, v);
		}
		return ps.executeQuery();
	}

	private ResultSet abrirRelacionamentos(String esquema, String tabela) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT    REFERENCED_TABLE_SCHEMA as ref_esquema,").append("\n");
		sql.append("          REFERENCED_TABLE_NAME as ref_tabela,").append("\n");
		sql.append("          REFERENCED_COLUMN_NAME as ref_coluna,").append("\n");
		sql.append("          COLUMN_NAME as coluna").append("\n");
		sql.append("FROM      INFORMATION_SCHEMA.KEY_COLUMN_USAGE").append("\n");
		sql.append("WHERE     TABLE_SCHEMA = ?").append("\n");
		sql.append("  AND     TABLE_NAME   = ?").append("\n");
		sql.append("  AND     REFERENCED_TABLE_NAME IS NOT NULL").append("\n");
		sql.append("ORDER BY  1,2,3").append("\n");

		PreparedStatement ps = this.con.prepareStatement(sql.toString());
		ps.setString(1, esquema);
		ps.setString(2, tabela);
		return ps.executeQuery();
	}

	private ResultSet abrirTabela(String esquema, String tabela) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT   TABLE_NAME as tabela").append("\n");
		sql.append("FROM     INFORMATION_SCHEMA.TABLES").append("\n");
		sql.append("WHERE    TABLE_SCHEMA = ?").append("\n");
		List<String> naoTemP = new ArrayList<>(), temP = new ArrayList<>();
		if (StringUtils.isNotBlank(tabela)) {
			sql.append("AND             TABLE_NAME = ?");
		} else if (!StringUtils.isAllBlank(this.argumentos.tabela.toArray(new String[this.argumentos.tabela.size()]))) {
			StringBuilder naoTem = new StringBuilder(), tem = new StringBuilder();
			for (int i = 0; i < this.argumentos.tabela.size(); i++) {
				String tabelaArg = this.argumentos.tabela.get(i);
				if (StringUtils.isNotBlank(tabelaArg)) {
					if (tabelaArg.startsWith("!")) {
						naoTem.append(naoTem.length() == 0 ? "AND             TABLE_NAME NOT IN(?" : ", ?");
						naoTemP.add(tabelaArg.replaceFirst("\\!", ""));
					} else {
						tem.append(tem.length() == 0 ? "AND             TABLE_NAME IN(?" : ", ?");
						temP.add(tabelaArg);
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

		PreparedStatement ps = this.con.prepareStatement(sql.toString());
		int ctd = 0;
		ps.setString(++ctd, esquema);
		if (StringUtils.isNotBlank(tabela)) {
			ps.setString(++ctd, tabela);
		} else {
			if (!StringUtils.isAllBlank(this.argumentos.tabela.toArray(new String[this.argumentos.tabela.size()]))) {
				for (String v : naoTemP)
					ps.setString(++ctd, v);
				for (String v : temP)
					ps.setString(++ctd, v);
			}
		}
		return ps.executeQuery();
	}

	private Coluna encontraColuna(Tabela tabela, String colunaNome) {
		if (tabela != null) {
			for (Coluna coluna : tabela.getColunas()) {
				if (colunaNome.equals(coluna.getNome())) {
					return coluna;
				}
			}
		}
		return null;
	}

	private Esquema encontraEsquema(List<Esquema> lista, String esquemaNome) {
		if (lista != null) {
			for (Esquema esquema : lista) {
				if (esquemaNome.equals(esquema.getNome())) {
					return esquema;
				}
			}
		}
		return null;
	}

	private Tabela encontraTabela(List<Esquema> lista, String esquemaNome, String tabelaNome) {
		Esquema esquema = this.encontraEsquema(lista, esquemaNome);
		if (esquema != null) {
			for (Tabela tabela : esquema.getTabelas()) {
				if (esquemaNome.equals(esquema.getNome()) && tabelaNome.equals(tabela.getNome())) {
					return tabela;
				}
			}
		}
		return null;
	}

	@Override
	public List<Esquema> ler() throws Exception {
		return ler(null, null);
	}

	public List<Esquema> ler(List<Esquema> result, String esquemaNome, String tabelaNome) throws Exception {

		if (result == null) {
			result = new ArrayList<>();
		}

		// listar esquemas
		ResultSet esquemaRs = this.abrirEsquema(esquemaNome);
		while (esquemaRs.next()) {
			Esquema esquema = this.encontraEsquema(result, esquemaRs.getString("esquema"));
			if (esquema == null) {
				esquema = new Esquema(esquemaRs.getString("esquema"));
				result.add(esquema);
			}

			// listar tabelas
			ResultSet tabelaRs = this.abrirTabela(esquema.getNome(), tabelaNome);
			while (tabelaRs.next()) {
				// verificar se a tabela já foi montada
				if (encontraTabela(result, esquema.getNome(), tabelaRs.getString("tabela")) != null) {
					continue;
				}
				Tabela tabela = esquema.add(new Tabela(esquema, tabelaRs.getString("tabela")));

				// listar Colunas
				ResultSet colunasRs = this.abrirColunas(esquema.getNome(), tabela.getNome());
				while (colunasRs.next()) {
					Coluna coluna = tabela.add(new Coluna(esquema, tabela, colunasRs.getString("nome")));

					coluna.setChavePrimaria("pri".equalsIgnoreCase(colunasRs.getString("chave_primaria")));
					coluna.setPermiteNulo("yes".equalsIgnoreCase(colunasRs.getString("permite_nulo")));
					coluna.setTipo(colunasRs.getString("tipo"));
					coluna.setComplemento(colunasRs.getString("complemento"));
				}
			}
		}

		// captar os relacionamentos
		for (int i = 0; i < result.size(); i++) {
			Esquema esquema = result.get(i);
			for (int j = 0; j < esquema.getTabelas().size(); j++) {
				Tabela tabela = esquema.getTabelas().get(j);
				if (tabela.getRelacionamentos().size() > 0) {
					continue;
				}

				// listar relacoes
				ResultSet relacionamentoRs = this.abrirRelacionamentos(esquema.toString(), tabela.toString());

				Relacionamento relacionamento = null;
				Tabela t = null;

				String esquemaRef = null;
				String tabelaRef = null;
				String colunaRef = null;
				String coluna = null;

				while (relacionamentoRs.next()) {
					if (!(relacionamentoRs.getString("ref_esquema").equals(esquemaRef)
							&& relacionamentoRs.getString("ref_tabela").equals(tabelaRef))) {
						esquemaRef = relacionamentoRs.getString("ref_esquema");
						tabelaRef = relacionamentoRs.getString("ref_tabela");

						while ((t = this.encontraTabela(result, esquemaRef, tabelaRef)) == null) {
							// recarregar a lista de esquema com o item avulso
							ler(result, esquemaRef, tabelaRef);
						}

						relacionamento = new Relacionamento(t);
						tabela.addRelacionamento(relacionamento);
					}
					colunaRef = relacionamentoRs.getString("ref_coluna");
					coluna = relacionamentoRs.getString("coluna");
					relacionamento.addColuna(this.encontraColuna(tabela, coluna), this.encontraColuna(t, colunaRef));
				}
			}
		}

		return result;

	}

	public List<Esquema> ler(String esquemaNome, String tabelaNome) throws Exception {
		return ler(null, esquemaNome, tabelaNome);
	}

}
