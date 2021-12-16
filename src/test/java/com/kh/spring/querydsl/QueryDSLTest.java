package com.kh.spring.querydsl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kh.spring.book.Book;
import com.kh.spring.springdata.SpringDataTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class QueryDSLTest {

	@Autowired
	QueryDSLRepository queryDSLRepository;
	
	//And조건절
	@Test
	@DisplayName("대출건 제목이 '디디'로 시작하고, 대출자ID가 test인 대출건을 조회")
	public void WhereAnd() {
		queryDSLRepository.whereAnd("디디","test").forEach(e->log.info(e.toString()));
	}
	
	//Or조건절
	@Test
	@DisplayName("대출건 제목이 '디디'로 시작하거나, 대출자ID가 jpa인 대출건을 조회") //쿼리가 5번이나 돌앗음
	public void WhereOr() {
		queryDSLRepository.whereOr("디디","jpa").forEach(e->log.info(e.toString()));
	}
	
	//right Join
	@Test
	@DisplayName("대출도서등록일자와 도서등록일자가 일치하는 대출도서를 조회") 
	public void fetchtJoin() {
		queryDSLRepository.fetchtJoin("디디","jpa").forEach(e->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("대출자 아이디가 test인 모든 대출건의 대출건 제목과 대출번호 조회") 
	public void projections() {
		queryDSLRepository.projections("test").forEach(e->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("대출자 아이디가 test인 모든 대출건의 대출건 제목과 대출번호 조회") 
	public void tuple() {
		queryDSLRepository.tuple("test").forEach(e->log.info(e.get(1,String.class)));
	}
	
	@Test
	@DisplayName("대출도서등록일자와 가입일자가 같은회원이 존재하는 대출도서")
	public void thetaJoin() {
		queryDSLRepository.thetaJoin().forEach(e->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("도서재고수량을 기준으로 내림차순으로 2권까지 조회")
	public void orderBy() {
		queryDSLRepository.orderBy().forEach(e->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("카테고리별 도서들의 최대 재고, 평균재고, 평균 대출횟수를 조회")
	public void groupBy() {
		queryDSLRepository.groupBy().forEach(e->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("대출도서의 상태가 '대출'인 대출도서가 한권이라도 존재하는 회원을조회 ")
	public void subQuery() {
		queryDSLRepository.subQuery().forEach(e->log.info(e.toString()));
	}
	
	@Test
	@DisplayName("도서 동적쿼리")
	public void dynamicQueryWithBook() {
		//도서재고가 매개변수로 전달받은 값보다 크고,
		//도서 대출횟수가 매개변수로 전달받은 값도다 작은 도서를 조회
		//도서재고나, 도서대출횟수가 null인 경우 조건절에서 해당 and절을 삭제
		Book book = new Book();
		book.setBookAmt(4); //이거보다 크고,
		book.setRentCnt(5); //이거보다 작은 도서를 조회 (null이면 해당 x)
		queryDSLRepository.dynamicQueryWithBook(book).forEach(e->log.info(e.toString()));;
	}
	
	@Test
	@DisplayName("회원 동적쿼리")
	public void dynamicQueryWithMember() {
		//사용자가 입력한 키워드가 
		//이메일 또는 아이디에 포함되면서 (or)(like)
		//전화번호가 입력받은 번호와 같은 회원을 조회 (and)(eq)
		queryDSLRepository.dynamicQueryWithMember("test","010-0112-0122").forEach(e->log.info(e.toString()));;
	}
	
	
}
