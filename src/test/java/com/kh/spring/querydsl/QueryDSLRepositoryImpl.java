package com.kh.spring.querydsl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.kh.spring.book.Book;
import com.kh.spring.book.QBook;
import com.kh.spring.member.Member;
import com.kh.spring.member.QMember;
import com.kh.spring.rent.QRent;
import com.kh.spring.rent.QRentBook;
import com.kh.spring.rent.Rent;
import com.kh.spring.rent.RentBook;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

//클래스이름+Impl은 규약임
public class QueryDSLRepositoryImpl implements QueryDSLRepositoryCustom{ //구현체 역할

	//JPAQuery를 사용하기위해 제공되는 JPAquery옵션
	private final JPAQueryFactory query;
	private QRent rent= QRent.rent;
	private QRentBook rentBook= QRentBook.rentBook;
	private QMember member = QMember.member;
	private QBook book= QBook.book;
			
	public QueryDSLRepositoryImpl(EntityManager em) {
		this.query = new JPAQueryFactory(em);
	}
	
	
	public List<Rent> whereAnd(String title,String userId){
		//where절에 여러개 predicate를 넘겨서 and 연산을 수행할 수도 있다.
		List<Rent> rents = query.select(rent)
								.from(rent)
								.where(rent.title.startsWith(title)
								//.and(rent.member.userId.eq(userId)))
								,rent.member.userId.eq(userId)) //이렇게 넘기면 무조건 and
							.fetch();
		return rents;
	}
	
	public List<Rent> whereOr(String title,String userId){
		List<Rent> rents = query.select(rent)
								.from(rent)
								.where(rent.title.startsWith(title)
								.or(rent.member.userId.eq(userId)))
							.fetch();
		return rents;
	}
	
	@Override
	public List<RentBook> fetchtJoin(String title, String userId) {
		List<RentBook> rentBooks = query.select(rentBook)
				.from(rentBook)
				.rightJoin(rentBook.book).fetchJoin()
				.rightJoin(rentBook.rent,rent).fetchJoin() //rent별칭!
				.rightJoin(rent.member)
				.where(rentBook.regDate.eq(rentBook.book.regDate))
			.fetch();
		return rentBooks;
	}
	
	public List<Rent> projections(String userId){
		List<Rent> rents = query.select(Projections.fields(Rent.class, rent.title,rent.rmIdx))
						.from(rent)
						.where(rent.member.userId.eq(userId))
						.fetch();						
		return rents;
	}
	
	
	public List<Tuple> tuple(String userId){
		
		 List<Tuple> tuples = query.select(rent.rmIdx,rent.title)
				 				  .from(rent)
				 				  .where(rent.member.userId.eq(userId))
				 			.fetch();
		return tuples;
	}
	
	public List<RentBook> thetaJoin(){
		//연관관계 매핑이 설정되어 있지 않아도 조인이 가능함
		//대출도서와 회원은 연관관계 매핑이 없지만, 마치 sql처럼 join할수있도록 지원되는 기능이 thetaJoin임
		List<RentBook> rentBooks=
				query.select(rentBook).distinct()
				.from(rentBook,member) //연관관계 매핑이고 다 무시하고 join됨
				.where(rentBook.regDate.eq(member.regDate))
				.fetch();
		return rentBooks;
	}
	
	public List<Book> orderBy(){
		return null;
	}
	
	@Override
	public List<Tuple> groupBy(){
		List<Tuple> tuple = query.select(book.category, book.bookAmt.max(), book.bookAmt.avg(),book.rentCnt.avg())
									.from(book).
									groupBy(book.category)
									.fetch();
		return tuple;
	}
	
	 //인라인뷰 형태로 쓸수는없음 ->where절에서 써야함
	public List<Member> subQuery(){
		List<Member> members = query.select(member)
									.from(member)			//서브쿼리쓸수있는 JPAExpressions
									.where(member.userId.in(JPAExpressions.select(rent.member.userId)
																		  .from(rent)
																		  .innerJoin(rent.rentBooks,rentBook)
																		  .where(rentBook.state.eq("대출"))
									)).fetch();
		return members;
	}

	@Override
	public List<Book> dynamicQueryWithBook(Book b){
		List<Book> books = query.select(book)
								.from(book) //BooleanExpression 반환해주는 메소드로 이렇게 쓸수있음
								.where( bookAmtGT(b.getBookAmt()), rentCntLT(b.getRentCnt()))
								.fetch();  //and절에서만 됨. or절은 여러개의 파라미터 형태로 못넘김
		return books;
	}
	
	public BooleanExpression bookAmtGT(Integer bookAmt) {
		return bookAmt == null ? null 		//안넘어왔으면 null을 반환
				: book.bookAmt.gt(bookAmt); // 그렇지않으면 book.bookAmt.gt(bookAmt)
	}
	
	public BooleanExpression rentCntLT(Integer rentCnt) {
		return rentCnt == null ? null 		//안넘어왔으면 null을 반환
				: book.rentCnt.lt(rentCnt); // 그렇지않으면 book.rentCnt.gt(rentCnt)
	}


	@Override
	public List<Member> dynamicQueryWithMember(String keyword,String tell) {
		List<Member> members = query.select(member)
									.from(member) 
									.where( emailOrUserIdEq(keyword) , member.tell.eq(tell))
									.fetch();
		return members;
	}
	
	public BooleanExpression emailOrUserIdEq(String keyword) {
		return keyword==null ? null 
				: member.userId.contains(keyword).or(member.email.contains(keyword)); 
	}
}
