package com.kh.spring.springdata;

import javax.transaction.Transactional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.spring.book.Book;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j //이거있음 logger자동~
//@어쩌고Mock도 추가해야댐
public class SpringDataTest {

	@Autowired
	private SpringDataRepository springDataRepository;
	private Book book;
	
	@BeforeEach //jupiter가 붙은거
	public void before() {
		book = new Book();
		book.setTitle("반지의 제왕");
		book.setCategory("문학");
		book.setIsbn("1234");
		book.setAuthor("최선아");
		book.setBookAmt(3);
	}
	
	//select
	@Test
	@DisplayName("모든 모서 검색 테스트")
	public void findAll() {
		springDataRepository.findAll().stream().forEach(e ->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("도서번호로 도서조회")
	public void findById() {
		System.out.println(springDataRepository.findById(1000L).get());
	}
	
	@Test
	@DisplayName("도서명 또는 작가로 도서검색")
	public void findByTitleOrAuthor() { //메소드이름에 따라 쿼리 생성됨(명명규칙JPA.pdf 참고)
		springDataRepository.findByTitleOrAuthor("비행운", "황정음").forEach(e ->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("카테고리가 문학이고, 도서재고가 3권 이상이면서 도서명이 디로 시작하는 도서검색")
	public void findTest() {
		springDataRepository
		.findByCategoryAndBookAmtGreaterThanEqualAndTitleStartingWith("문학", 3, "디")
		.forEach(e->log.info(e.toString()));
	}

	//select count
	@Test
	@DisplayName("도서재고가 2권이상인 도서수량 조회")
	public void countByBookAmtGreaterThanEqual() { //메소드이름 count도 가능
		log.info("재고가 2권이상 도서 :"+springDataRepository.countByBookAmtGreaterThanEqual(2));
	}
	
	@Test
	@DisplayName("전체 도서권수 조회")
	public void count() { 
		log.info("전체 도서권수 조회 :"+springDataRepository.count());
	}
	
	@Test
	@DisplayName("한번도 대출된적없는 도서권수 조회")
	public void test01() { 
		log.info("한번도 대출된적없는 도서권수 조회 :"+springDataRepository.countByRentCnt(0));
	}
	
	@Test
	@DisplayName("한번도 대출된적없는 도서 조회")
	public void test02() { 
		springDataRepository.findByRentCnt(0).forEach(e ->log.info(e.toString()));
	}
	
	//insert
	@Test
	@DisplayName("반지의 제왕 도서 등록")
	public void save() {
		springDataRepository.save(book);
	}
	
	//exist (true/false로 반환해줌)
	@Test
	@DisplayName("도서재고가 5권이상인 도서가 존재하는지 확인")
	public void existsBy() {
		log.info("한번도 대출된적없는 도서권수 조회 :"+springDataRepository.existsByBookAmtGreaterThanEqual(10));
	}
	
	//delete :이거 왜안됨..?
	@Test
	@DisplayName("반지의 제왕 도서 삭제")
	public void deleteBy() {
		springDataRepository.deleteByTitle("반지의 제왕");
	}
	
	
}
