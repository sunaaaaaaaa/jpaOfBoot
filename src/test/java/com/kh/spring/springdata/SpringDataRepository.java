package com.kh.spring.springdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.spring.book.Book;

public interface SpringDataRepository extends JpaRepository<Book,Long>{ //JpaRepository api

	List<Book> findByTitleOrAuthor(String title,String author); //Book에 담아서 보내도 상관없음
	
	//카테고리가 문학이고, 도서재고가 3권 이상이면서 도서명이 디로 시작하는 도서검색
	List<Book> findByCategoryAndBookAmtGreaterThanEqualAndTitleStartingWith(String category,int bookAmt,String title);
	
	int countByBookAmtGreaterThanEqual(int bookAmt);
	
	//한번도 대출된적없는 도서권수조회
	int countByRentCnt(int rentCnt);
	
	//한번도 대출된적없는 도서조회
	List<Book> findByRentCnt(int rentCnt);
	
	boolean existsByBookAmtGreaterThanEqual(int bookAmt);
	
	void deleteByTitle(String title);
	
}
