package e_commerce.khilat.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import e_commerce.khilat.dtomodels.AddToCartRequest;
import e_commerce.khilat.dtomodels.ReviewMessageDto;
import e_commerce.khilat.service.ReviewService;

@RestController
@RequestMapping("/api/review")
@CrossOrigin
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@PostMapping("/post-review")
	public ResponseEntity<String> postReviewMsg(@RequestBody ReviewMessageDto request) {

		try {
			reviewService.postReviewMsgService(request);

			return ResponseEntity.ok("Thanks for your review!");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}

	}

}
