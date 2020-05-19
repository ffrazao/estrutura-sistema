package com.frazao.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frazao.modelo.dto.FiltroDTO;
import com.frazao.bo.CRUDBO;

@RestController
public abstract class CRUDREST<T, Id, BO extends CRUDBO<T, Id>> {

	private BO bo;

	public CRUDREST(BO bo) {
		this.bo = bo;
	}

	@PostMapping
	public Id create(T t) {
		Id result = this.getBO().create(t);
		return result;
	}

	@DeleteMapping
	public void delete(Id id) {
		this.getBO().delete(id);
	}

	@GetMapping
	public List<T> filter(@RequestParam("filtro") FiltroDTO filtro) {
		List<T> result = this.getBO().filter(filtro);
		return result;
	}

	public BO getBO() {
		return this.bo;
	}

	@GetMapping("/{id}")
	public T restore(@PathVariable("id") Id id) {
		T result = (T) this.getBO().restore(id);
		return result;
	}

	@PutMapping
	public T update(Id id, T t) {
		T result = (T) this.getBO().update(id, t);
		return result;
	}

}
