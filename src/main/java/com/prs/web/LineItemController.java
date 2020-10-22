package com.prs.web;

import java.util.List;     
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
import com.prs.db.LineItemRepo;


@CrossOrigin
@RestController
@RequestMapping("/api/lines")
public class LineItemController {
	
	@Autowired
	private LineItemRepo lineItemRepo;
	
	// List all lineitem
	@GetMapping("/")
	public List<LineItem> getAllLineItems() {
		return lineItemRepo.findAll();
	}
	
	// Get lineitem by id
	@GetMapping("/{id}")
	public Optional<LineItem> getLineItem(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		if (li.isPresent()) {
			return li;
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem not found");
		}
		
	}
	
	// Add a lineitem
	@PostMapping("/")
	public LineItem addLineItem(@RequestBody LineItem li) {
		return lineItemRepo.save(li);
	}
	
	// Update a lineitem
	@PutMapping("/{id}")
	public LineItem updateLineItem(@RequestBody LineItem li, @PathVariable int id) {
		if (id == li.getId()) {
			return lineItemRepo.save(li);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem id does not match");
		}
	}
	
	// Delete a lineitem
	@DeleteMapping("/{id}")
	public Optional<LineItem> deleteLineItem(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		if (li.isPresent()) {
			lineItemRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem not found");
		}
		return li;	
	}
	
}
