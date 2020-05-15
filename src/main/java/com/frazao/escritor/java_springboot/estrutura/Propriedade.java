package com.frazao.escritor.java_springboot.estrutura;

import com.frazao.escritor.Escritor;
import com.frazao.escritor.java_springboot.EscritorJavaSpringBoot;

public class Propriedade extends EstruturaBasica {
	
	public EscritorJavaSpringBoot getEscritor() {
		return (EscritorJavaSpringBoot) this.escritor;
	}

	public Propriedade(Escritor escritor) {
		super(escritor);
	}
	
	private StringBuilder linha;


	private void l() {
		this.linha = new StringBuilder();
	}
	
	private void e(String mensagem, Object... valores) {
		this.linha.append(String.format(mensagem, valores)).append("\n");
	}


	@Override
	public void escrever() {
//		l();
//		
//		if (coluna.isChavePrimaria()) {
//			e("@Id");
//			e("@GeneratedValue(strategy = GenerationType.AUTO)");
//		}
//		
//		switch (coluna.getTipo().toLowerCase()) {
//		case "char":
//		case "varchar":
//			e("@Column(name = \"%s\") private String %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "enum":
//			DominioInfo d = escritor.dominio.getDominio(this.esquema, this.tabela, this.coluna);
//			e("@Column(name = \"%s\") @Enumerated(EnumType.STRING) private %s.%s %s;", this.coluna.getNome(),
//					d.pacoteFinal, d.nome, this.nome);
//			break;
//		case "set":
//			break;
//
//		case "date":
//		case "datetime":
//		case "timestamp":
//			e("@Column(name = \"%s\") @Basic private java.time.LocalDateTime %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "bigint":
//			e("@Column(name = \"%s\") private Long %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "decimal":
//			e("@Column(name = \"%s\") private BigDecimal %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "double":
//		case "float":
//			e("@Column(name = \"%s\") private Double %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "smallint":
//		case "tinyint":
//		case "int":
//		case "mediumint":
//			e("@Column(name = \"%s\") private Integer %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "point":
//			break;
//		case "polygon":
//			break;
//
//		case "tinytext":
//		case "text":
//		case "longtext":
//		case "mediumtext":
//			e("@Column(name = \"%s\") @Lob private String %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		case "varbinary":
//		case "blob":
//		case "longblob":
//			e("@Column(name = \"%s\") @Lob private byte[] %s;", this.coluna.getNome(), this.nome);
//			break;
//
//		default:
//			throw new IllegalArgumentException("tipo não reconhecido " + coluna.getTipo());
//		}
//		return this.linha.toString();
//	
	}


}
