package com.prs.web;

import java.util.List;     
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.Request;
import com.prs.db.RequestRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {
	
	@Autowired
	private RequestRepo requestRepo;
	
	// List all request
	@GetMapping("/")
	public List<Request> getAllRequests() {
		return requestRepo.findAll();
	}
	
	// Get request by id
	@GetMapping("/{id}")
	public Optional<Request> getRequest(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if (r.isPresent()) {
			return r;
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found");
		}
		
	}
	
	// Add a request
	@PostMapping("/")
	public Request addRequest(@RequestBody Request r) {
		return requestRepo.save(r);
	}
	
	// Update a request
	@PutMapping("/{id}")
	public Request updateRequest(@RequestBody Request r, @PathVariable int id) {
		if (id == r.getId()) {
			return requestRepo.save(r);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request id does not match");
		}
	}
	
	// Delete a request
	@DeleteMapping("/{id}")
	public Optional<Request> deleteRequest(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if (r.isPresent()) {
			requestRepo.deleteById(id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found");
		}
		return r;	
	}
	
}
