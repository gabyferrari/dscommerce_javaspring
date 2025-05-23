package com.devsuperior.dscommerce.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscommerce.dto.ProductDTO;
import com.devsuperior.dscommerce.dto.ProductMinDTO;
import com.devsuperior.dscommerce.services.ProductService;

import jakarta.validation.Valid;

@RestController //configurar para q quando a aplicação rodar, o que implementar nessa classe vai estar respondendo pela web
@RequestMapping(value = "/products") //configura a rota
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@GetMapping(value = "/{id}") //responder pela rota /products e pelo método get
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) { //ResponseEntity -> o corpo da resposta vai ser um ProductDTO
	   ProductDTO dto = service.findById(id);
	   return ResponseEntity.ok(dto); //customizando a resposta 200 do Postman onde o corpo vai ser o objeto dto
	}

	@GetMapping
	public ResponseEntity<Page<ProductMinDTO>> findAll(
			@RequestParam(name = "name", defaultValue = "") String name, //procurar nome de produto no banco
			Pageable pageable) { //busca de produtos pageada(de 10 em 10 ou 12 em 12)
	   Page<ProductMinDTO> dto = service.findAll(name, pageable);
	   return ResponseEntity.ok(dto);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) { //vai inserir e salvar um novo produto, esse é o corpo da requisição
	   dto = service.insert(dto);
	   URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}") //link do recurso criado
			   .buildAndExpand(dto.getId()).toUri();
	   return ResponseEntity.created(uri).body(dto); //resposta 201 do Postman
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping(value = "/{id}") 
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) { //atualizar um produto
	   dto = service.update(id, dto);
	   return ResponseEntity.ok(dto); 
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping(value = "/{id}") 
	public ResponseEntity<Void> delete(@PathVariable Long id) { //deletar um produto
	   service.delete(id);
	   return ResponseEntity.noContent().build(); //resposta 204 do Postman (deu certo e não tem corpo na resposta)
	}
}
