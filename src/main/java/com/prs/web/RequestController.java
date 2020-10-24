package com.prs.web;

import java.time.LocalDateTime;
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
	
	// Add a request with "New" status and submittedDate
	@PostMapping("/")
	public Request addRequest(@RequestBody Request r) { 
		r.setStatus("New");
		r.setSubmittedDate(LocalDateTime.now());
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
	
	//List all requests with "Review" status and not by userId
	@GetMapping("/reviews/{id}")
	public List<Request> getAllRequestsReview(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if (id!=0) {
			return requestRepo.findByStatusAndUserIdNot("Review", id);
		}
		else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request and UserId not found");
		}
		
	}
	
	//Set requests to "Review" if > $50 or requests to "Approved" if < $50
	@PutMapping("/review")
	public Request setRequestReview(@RequestBody Request r) {
		
		if (r.getTotal() < 50) {
			r.setStatus("Approved");
			r.setSubmittedDate(LocalDateTime.now());
		}
		else {
			r.setStatus("Review");
			r.setSubmittedDate(LocalDateTime.now());
		}
		return requestRepo.save(r);			
	}
		
	//Set request to "Approved"
	@PutMapping("/approve")
	public Request setRequestApproved(@RequestBody Request r) {
		r.setStatus("Approved");
		return requestRepo.save(r);
	}
	
	//Set request to "Rejected"
	@PutMapping("/reject")
	public Request setRequestRejected(@RequestBody Request r) {
		r.setStatus("Rejected");
		return requestRepo.save(r);
		}
}
