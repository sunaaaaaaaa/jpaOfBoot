package com.kh.spring.querydsl;

import java.util.List;

import com.kh.spring.book.Book;
import com.kh.spring.member.Member;
import com.kh.spring.rent.Rent;
import com.kh.spring.rent.RentBook;
import com.querydsl.core.Tuple;

public interface QueryDSLRepositoryCustom {


	List<Rent> whereAnd(String title,String userId);

	List<Rent> whereOr(String title,String userId);
	
	List<RentBook> fetchtJoin(String title,String userId);
	
	List<Rent> projections(String userId);
	
	List<Tuple> tuple(String userId);
	
	List<RentBook> thetaJoin();
	
	List<Book> orderBy();
	
	List<Tuple> groupBy();
	
	List<Member> subQuery(); 
	
	List<Book> dynamicQueryWithBook(Book book);
	
	List<Member> dynamicQueryWithMember(String keyword,String tell);
}
