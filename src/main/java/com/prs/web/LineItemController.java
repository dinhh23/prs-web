  
package com.prs.web;

import java.util.List;     
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
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
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem id not found");
		}
		
	}
	
	// Get all lineitem for a request id
	@GetMapping("/for-req/{id}")
	public List<LineItem> getLinesForPR(@PathVariable int id) {
		return lineItemRepo.findByRequestId(id);
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
			li = lineItemRepo.save(li);
			recalculateTotal(li.getRequest().getId());
			return li;
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem & LineItem id does not match");
		}
	}
	
	// Delete a lineitem
	@DeleteMapping("/{id}")
	public Optional<LineItem> deleteLineItem(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		if (li.isPresent()) {
			int requestId = li.get().getRequest().getId();
			lineItemRepo.deleteById(id);
			recalculateTotal(requestId);			
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "LineItem id not found");
		}
		return li;	
	}
	
	// RecalculateTotal Method 
	public void recalculateTotal(int requestId) {
		List<LineItem> lines = lineItemRepo.findByRequestId(requestId);				// Get a list of line items 
		
		double total = 0.0;															// Loop thru list 
		for (LineItem line : lines) {
			
			total += (line.getProduct().getPrice()) * (line.getQuantity());			// Muliply the quantity of the product by the product price
			
		}
		
		Request r = requestRepo.findById(requestId).get();							// Save that total 
		r.setTotal(total);
		requestRepo.save(r);
	} 
	
	
	
}