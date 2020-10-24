  
package com.prs.web;

import java.util.List;     
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
import com.prs.business.Product;
import com.prs.business.Request;
import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;


@CrossOrigin
@RestController
@RequestMapping("/api/lines")
public class LineItemController {
	
	@Autowired
	private LineItemRepo lineItemRepo;
	
	@Autowired
	private RequestRepo requestRepo;
	
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
		li = lineItemRepo.save(li);
		recalculateTotal(li.getRequest().getId());
		
		return li;
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
	
	  private void recalculateTotal(int requestID) {
		// get a list of line items 
		List<LineItem> lines = lineItemRepo.findAllByRequestId(requestID);
		// loop through list to get total
		double total = 0.0;
		for (LineItem line : lines) {
			Product p = line.getProduct();
			total += p.getPrice()*line.getQuantity();
		}
		// save that total in the instance of request
		Request r = requestRepo.findById(requestID).get();
		r.setTotal(total);
		requestRepo.save(r);
	} 
	
	
	
}