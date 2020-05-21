package com.frazao.escritor.java_springboot.estrutura;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Dao extends EstruturaBasica {

	public Dao(Escritor escritor) {
		super(escritor);
	}

	@Override
	public void escrever() throws Exception {
		File dir = this.getEscritor().diretorios.get("dao");

		String pacote = String.format("%s.dao", this.getEscritor().argumentos.pacoteRaiz);
		
		String pacoteFiltroDTO = String.format("%s.modelo.dto", this.getEscritor().argumentos.pacoteRaiz);

		for (EntidadeInfo item : this.getEscritor().entidade.mapa.values()) {
			if (item.pacote == null) {
				continue;
			}

			String pacoteFinal = pacote.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");
			String pacoteFinalFiltroDTO = pacoteFiltroDTO
					.concat(StringUtils.isNotBlank(item.pacote) ? ".".concat(item.pacote) : "");


			// escrever o DAO
			File dirLocal = dir;
			if (StringUtils.isNotBlank(item.pacote)) {
				dirLocal = new File(dir, item.pacote);
			}
			dirLocal.mkdirs();
			
			File arquivoLocal = new File(dirLocal, item.nome.concat("DAO.java"));
			
			String tipoPk = null;
			
			for (PropriedadeInfo pi : item.propriedadeInfoList) {
				if (pi.getColuna().isChavePrimaria()) {
					tipoPk = pi.getTipoJava() == null ? "Integer" : pi.getTipoJava().getCanonicalName();
				}
			}
			
			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {
				// pacote
				w.append(String.format("package %s;", pacoteFinal));
				w.newLine();
				w.newLine();

				// importações
				w.append("import org.springframework.data.jpa.repository.JpaRepository;");
				w.newLine();
				w.append("import org.springframework.stereotype.Repository;");
				w.newLine();
				w.newLine();
				
				w.append("import ").append(item.pacoteFinal).append(".").append(item.nome).append(";");
				w.newLine();
				w.newLine();


				// declarar a classe
				w.append(String.format("@Repository"));
				w.newLine();
				w.append(String.format("public interface %sDAO extends JpaRepository<%s, %s>, %sDAOFiltro {", item.nome, item.nome,
						tipoPk, item.nome));
				w.newLine();
				w.newLine();

				w.append("}");
				w.newLine();
			}
			
			// escrever o DAOFiltro
			arquivoLocal = new File(dirLocal, item.nome.concat("DAOFiltro.java"));
			
			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {
				// pacote
				w.append(String.format("package %s;", pacoteFinal));
				w.newLine();
				w.newLine();
				
				// importações
				w.append(String.format("import %s.Filtro;", pacote));
				w.newLine();
				w.append(String.format("import %s.%s;", item.pacoteFinal, item.nome));
				w.newLine();
				w.append(String.format("import %s.%sFiltroDTO;", pacoteFinalFiltroDTO, item.nome));
				w.newLine();

				w.newLine();
				
				// declarar a classe
				w.append(String.format("public interface %sDAOFiltro extends Filtro<%s, %sFiltroDTO> {", item.nome, item.nome,
						item.nome));
				w.newLine();
				w.newLine();
				
				w.append("}");
				w.newLine();
			}
			
			// escrever a implementação do DAOFiltro
			File impl = new File(dirLocal, "impl");
			impl.mkdirs();
			arquivoLocal = new File(impl, item.nome.concat("DAOFiltroImpl.java"));
			
			try (BufferedWriter w = new BufferedWriter(new FileWriter(arquivoLocal, false))) {
				// pacote
				w.append(String.format("package %s.impl;", pacoteFinal));
				w.newLine();
				w.newLine();
				
				// importações
				w.append(String.format("import java.util.Collection;", pacote));
				w.newLine();
				w.append(String.format("import java.util.stream.Collectors;", pacote));
				w.newLine();
				w.newLine();
				w.append(String.format("import javax.persistence.EntityManager;", pacote));
				w.newLine();
				w.append(String.format("import javax.persistence.PersistenceContext;", pacote));
				w.newLine();
				w.append(String.format("import javax.persistence.Query;", pacote));
				w.newLine();
				w.newLine();
				w.append(String.format("import org.apache.commons.lang3.ObjectUtils;", pacote));
				w.newLine();
				w.append(String.format("import org.apache.commons.lang3.StringUtils;", pacote));
				w.newLine();
				w.newLine();

				w.newLine();
				w.append(String.format("import %s.%sDAOFiltro;", pacoteFinal, item.nome));
				w.newLine();
				w.append(String.format("import %s.%sFiltroDTO;", pacoteFinalFiltroDTO, item.nome));
				w.newLine();
				w.append(String.format("import %s.%s;", item.pacoteFinal, item.nome));
				w.newLine();

				w.newLine();
				
				// declarar a classe
				w.append(String.format("public class %sDAOFiltroImpl implements %sDAOFiltro {", item.nome, item.nome));
				w.newLine();
				w.newLine();
				
				w.append(String.format("   @PersistenceContext"));
				w.newLine();
				w.append(String.format("   private EntityManager entityManager;"));
				w.newLine();
				w.newLine();
				
				w.append(String.format("   @SuppressWarnings(\"unchecked\")"));
				w.newLine();
				w.append(String.format("   @Override"));
				w.newLine();
				w.append(String.format("   public Collection<%s> filtrar(%sFiltroDTO f) {", item.nome, item.nome));
				w.newLine();
				w.append(String.format(""));
				w.newLine();
				w.append(String.format("      StringBuilder sql = new StringBuilder();"));
				w.newLine();
				w.append(String.format("      sql.append(\"SELECT em.*\").append(\"\\n\");"));
				w.newLine();
				w.append(String.format("      sql.append(\"FROM   %s.%s as em\").append(\"\\n\");", item.esquema, item.tabela.getNome()));
				w.newLine();
				w.append(String.format("      StringBuilder arg = new StringBuilder();"));
				w.newLine();
				w.append(String.format("      // if (StringUtils.isNotBlank(f.getCpfCnpj())) {"));
				w.newLine();
				w.append(String.format("      //    arg.append(adWhere(arg)).append(\"em.cpf_cnpj = :cpfCnpj\").append(\"\\n\");"));
				w.newLine();
				w.append(String.format("      // }"));
				w.newLine();
				w.append(String.format("      // if (ObjectUtils.isNotEmpty(f.getTipo())) {"));
				w.newLine();
				w.append(String.format("      //    arg.append(adWhere(arg)).append(\"em.pessoa_tipo in :tipo\").append(\"\\n\");"));
				w.newLine();
				w.append(String.format("      // }"));
				w.newLine();
				w.append(String.format("      sql.append(arg);"));
				w.newLine();
				w.append(String.format("      sql.append(\"ORDER BY 1\").append(\"\\n\");"));
				w.newLine();
				w.append(String.format("      Query query = entityManager.createNativeQuery(sql.toString(), %s.class);", item.nome));
				w.newLine();
				w.append(String.format("      // if (StringUtils.isNotBlank(f.getCpfCnpj())) {"));
				w.newLine();
				w.append(String.format("      //    query.setParameter(\"cpfCnpj\", f.getCpfCnpj());"));
				w.newLine();
				w.append(String.format("      // }"));
				w.newLine();
				w.append(String.format("      // if (ObjectUtils.isNotEmpty(f.getTipo())) {"));
				w.newLine();
				w.append(String.format("      //    query.setParameter(\"tipo\", f.getTipo().stream().map(v -> v.name()).collect(Collectors.toSet()));"));
				w.newLine();
				w.append(String.format("      // }"));
				w.newLine();
				w.append(String.format("      return query.getResultList();"));
				w.newLine();
				w.append(String.format(""));
				w.newLine();
				w.append(String.format("   }"));
				w.newLine();
				
				w.newLine();				
				w.append("}");
				w.newLine();
			}
		}
	}

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

}