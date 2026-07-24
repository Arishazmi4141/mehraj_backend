package e_commerce.khilat.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import e_commerce.khilat.dtomodels.ReviewMessageDto;
import e_commerce.khilat.entity.ReviewMessage;
import e_commerce.khilat.repository.ReviewRepo;




@Service
public class ReviewService {
	
	@Autowired
	private ReviewRepo reviewRepo;
	
	
//	@CacheEvict(value = { "productsDetail", }, allEntries = true)
	@CacheEvict(value = "productDetail", key = "#review.productId", allEntries = true)
	public void postReviewMsgService(ReviewMessageDto request) {
		
		ReviewMessage reviewMessage = new ReviewMessage();
		
		reviewMessage.setProductId(request.getProductId());
		reviewMessage.setReviewerName(request.getReviewerName());
		reviewMessage.setRating(request.getRating());
		reviewMessage.setReviewMsg(request.getReviewMsg());
		reviewMessage.setCreatedAt(LocalDateTime.now());
		
		 reviewRepo.save(reviewMessage);
		 
		
		
	}

}
