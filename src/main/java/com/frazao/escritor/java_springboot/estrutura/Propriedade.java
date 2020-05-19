package com.frazao.escritor.java_springboot.estrutura;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.apache.commons.text.CaseUtils;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Propriedade {

	private EscritorJavaSpringBoot escritor;

	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

	public Propriedade(Escritor escritor) {
		this.escritor = (EscritorJavaSpringBoot) escritor;
	}

	private StringBuilder linha;

	private void l() {
		this.linha = new StringBuilder();
	}

	private void e(String mensagem, Object... valores) {
		this.linha.append(String.format(mensagem, valores)).append("\n");
	}

	public String escrever(PropriedadeInfo pi) {
		l();

		if (pi.getRefExterna() == null) {

			// indicar que é chave primária
			if (pi.getColuna().isChavePrimaria()) {
				e("@Id");
				e("@GeneratedValue(strategy = GenerationType.AUTO)");
			}

			switch (pi.getColuna().getTipo().toLowerCase()) {
			case "char":
			case "varchar":
				e("@Column(name = \"%s\") private String %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(String.class);
				break;

			case "enum":
				DominioInfo d = escritor.dominio.getDominio(pi.getEsquema(), pi.getTabela(), pi.getColuna());
				e("@Column(name = \"%s\") @Enumerated(EnumType.STRING) private %s.%s %s;", pi.getColuna().getNome(),
						d.pacoteFinal, d.nome, pi.getNome());
				pi.setTipoJava(Enum.class);
				break;
			case "set":
				break;

			case "date":
			case "datetime":
			case "timestamp":
				e("@Column(name = \"%s\") @Basic private java.time.LocalDateTime %s;", pi.getColuna().getNome(),
						pi.getNome());
				pi.setTipoJava(LocalDateTime.class);
				break;

			case "bigint":
				e("@Column(name = \"%s\") private Long %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(Long.class);
				break;

			case "decimal":
				e("@Column(name = \"%s\") private BigDecimal %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(BigDecimal.class);
				break;

			case "double":
			case "float":
				e("@Column(name = \"%s\") private Double %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(Double.class);
				break;

			case "smallint":
			case "tinyint":
			case "int":
			case "mediumint":
				e("@Column(name = \"%s\") private Integer %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(Integer.class);
				break;

			case "point":
				break;
			case "polygon":
				break;

			case "tinytext":
			case "text":
			case "longtext":
			case "mediumtext":
				e("@Column(name = \"%s\") @Lob private String %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(String.class);
				break;

			case "varbinary":
			case "blob":
			case "longblob":
				e("@Column(name = \"%s\") @Lob private byte[] %s;", pi.getColuna().getNome(), pi.getNome());
				pi.setTipoJava(byte[].class);
				break;

			default:
				throw new IllegalArgumentException("tipo não reconhecido " + pi.getColuna().getTipo());
			}
		} else {
			// indicar que é chave primária
			if (pi.getColuna().isChavePrimaria()) {
				e("@OneToOne(fetch = FetchType.LAZY) @MapsId @JoinColumn(name = \"%s\") private %s.%s %s;",
						pi.getColuna().getNome(), pi.getRefExterna().getEntidadeInfo().pacoteFinal, pi.getRefExterna().getEntidadeInfo().nome,
						CaseUtils.toCamelCase(pi.getColuna().getNome(), false, new char[] { '_', '.' }));
			} else {				
				e("@ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = \"%s\") private %s.%s %s;",
						pi.getColuna().getNome(), pi.getEntidadeInfo().pacoteFinal, pi.getEntidadeInfo().nome,
						CaseUtils.toCamelCase(pi.getColuna().getNome(), false, new char[] { '_', '.' }));
			}
			e("");

		}

		return this.linha.toString();
	}

}
