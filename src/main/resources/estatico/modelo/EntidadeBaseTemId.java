package com.frazao.lacodeamorrest.modelo;

import java.io.Serializable;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public abstract class EntidadeBaseTemId extends EntidadeBase implements Serializable, TemId<Integer> {

	private static final long serialVersionUID = 1L;
	
}
