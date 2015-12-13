package com.babydear.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.babydear.dto.ResponseDTO;
import com.babydear.dto.CardListDTO;
import com.babydear.dto.ResponseDTO;
import com.babydear.exception.NotGoodExtention;
import com.babydear.exception.NotToken;
import com.babydear.model.Baby;
import com.babydear.model.Card;
import com.babydear.model.User;
import com.babydear.repository.CardRepository;
import com.babydear.service.AuthService;
import com.babydear.service.ImgService;
import com.babydear.service.TagService;
import com.mysql.fabric.Response;

import sun.net.www.protocol.http.AuthScheme;


@RestController
//@RequestMapping(value = "/api/card")
public class CardController {
	private static final Logger logger = LoggerFactory.getLogger(CardController.class);
	@Autowired
	CardRepository cardRepo;
	@Autowired
	TagService tagService;
	@Autowired AuthService authService;
	
	@Autowired
	ImgService imgService;
	
	@RequestMapping(value = "/api/card",  method = RequestMethod.GET)
	public CardListDTO selectCards(Long uId, User user){
		System.out.println("it's uId");
//		System.out.println(uId);
//		System.out.println(user.getUId());
		List<Card> cardList = cardRepo.findAllByOrderByCIdDesc();
		
		CardListDTO cardListDTO = new CardListDTO();
		cardListDTO.setCardList(cardList);
		return cardListDTO;
	}
	@RequestMapping(value = "/api/card", method = RequestMethod.POST)
	public ResponseDTO createCard(String token, Card card, MultipartFile image){
		logger.info(card.toString());
		if(token == null || token.isEmpty()) return new ResponseDTO(false, "토큰이 없습니다.");
		try {
			User user = authService.getUser(token);
		} catch (NotToken e1) {
			return new ResponseDTO(false, "토큰이 유효하지 않습니다.");
		}
		final List<Baby> babies = tagService.processTags(card.getBIds());
		card.setBabies(babies);
		try {
			card.setCardImg(imgService.processImgCard(image));
		} catch (IllegalStateException e) {
			return new ResponseDTO(false, "이미지가 너무 크거나 잘못되었습니다.");
		} catch (IOException e) {
			return new ResponseDTO(false, "이미지가 너무 크거나 잘못되었습니다.");
		} catch (NotGoodExtention e) {
			return new ResponseDTO(false, "이미지 형식이 잘못 되었습니다.");
		}
		card = cardRepo.save(card);
		return new ResponseDTO(true, "null", card);
	}
	
	@RequestMapping(value = "/api/card", method = RequestMethod.PUT)
	public String updateCard(){
		logger.info("hahah");
		return " { update : true, error: null }";
	}
	@RequestMapping(value = "/api/card", method = RequestMethod.DELETE)
	public String deleteCard(){
		return " { delete: true, error: null }";
	}

}
