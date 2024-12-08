package com.example.bookVillage.bookRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bookVillage.bookCard.bo.BookCardBO;
import com.example.bookVillage.bookCard.entity.BookCardEntity;
import com.example.bookVillage.bookRegister.bo.BookRegisterBO;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/book-register")
public class BookRegisterRestCotroller {
	
	@Autowired
	private BookRegisterBO bookregisterBO;
	
	@Autowired
	private BookCardBO bookCardBO;

	// 책 등록
	@PostMapping("/create")
	public Map<String, Object> BookRegisterCreate(
			@RequestPart("metadata") Map<String, String> metadata, // 이미지랑 받기 위해서 requestPart로 JSON 따로 file 따로 받기
	        @RequestPart(value = "images", required = false) List<MultipartFile> files,
			HttpSession session){
		
		Integer userId = (Integer)session.getAttribute("userId");
		String userLoginId = (String)session.getAttribute("userLoginId");
		String title = metadata.get("title");
		String isbn13 = metadata.get("isbn13");
		String review = metadata.get("review");
		String point = metadata.get("point");
		String b_condition = metadata.get("b_condition");
		String description = metadata.get("description");
		String exchange_YN = metadata.get("exchange_YN");
		String place = metadata.get("place");

		Map<String, Object> result = new HashMap<>();
		
		// 등록 및 중복 확인
		Integer BookRegisterId = bookregisterBO.addBookRegisterAndImage(userId, userLoginId, title, isbn13,
				review, point, b_condition, description, exchange_YN, place, files);
	
		

		if(BookRegisterId != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("result", "책 등록 실패"); // 이미 책이 등록되어 있는 경우
		}
		
		return result;
	}
	
	// 책 수정
	@PostMapping("/update")
	public Map<String, Object> BookRegisterUpdate(
			@RequestBody Map<String, String> requestBody, 
			// 이미지 수정 가능하게 할 지 고민
			HttpSession session){
		
		Integer userId = (Integer)session.getAttribute("userId");
		Integer bookRegisterId = Integer.parseInt(requestBody.get("bookRegisterId"));
		String review = requestBody.get("review");
		String point = requestBody.get("point");
		String b_condition = requestBody.get("b_condition");
		String description = requestBody.get("description");
		String place = requestBody.get("place");
		String stauts = requestBody.get("status");
		String exchange_YN = requestBody.get("exchange_YN");

		Integer BookRegisterId = bookregisterBO.updateBookRegister(userId, bookRegisterId, review, point,
				b_condition, description, place, stauts, exchange_YN);
		
		Map<String, Object> result = new HashMap<>();

		if(BookRegisterId != null) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("result", "책 수정 실패");
		}
		
		return result;
	}
	
	// 책 삭제
	@DeleteMapping("/delete")
	public Map<String, Object> BookRegisterDelete(
			@RequestBody Map<String, String> requestBody, 
			HttpSession session){
		
		Integer userId = (Integer)session.getAttribute("userId");
		Integer bookRegisterId = Integer.parseInt(requestBody.get("bookRegisterId"));

		Integer delete = bookregisterBO.deleteBookRegister(userId, bookRegisterId);
		
		Map<String, Object> result = new HashMap<>();
		if (delete == 1) {
			result.put("code", 200);
			result.put("result", "성공");
		} else {
			result.put("code", 500);
			result.put("result", "책 삭제 실패");
		}
	
		return result;
	}
	
	//isbn13 값으로 해당 책 등록 올린 사람 뜨게
	@GetMapping("/list")
	public Map<String, Object> BookRegisterListByUserId( 
			HttpSession session){
		
		int userId = (Integer)session.getAttribute("userId");
		List<BookCardEntity> bookCardList = bookCardBO.BookCard(userId);
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("result", "성공");
		result.put("data", bookCardList);
		
	
		return result;
		
		
	}
	
	
	
	
	
}