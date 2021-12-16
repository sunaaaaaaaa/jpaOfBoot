package com.kh.spring.rent;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kh.spring.book.Book;

import lombok.Data;
import lombok.ToString;

@Entity
@DynamicInsert //insert쿼리 작성시 null값인 필드는 생략하고 넣어줌
@DynamicUpdate //update쿼리 작성시 변경이 감지되지 않는 필드는 생략하고 업뎃 (기본적으로 Entity,DynamicInsert,DynamicUpdate 어노테이션넣어줌)
@Data
@ToString(exclude = {"rent"})
public class RentBook {
	
	@Id 
	@GeneratedValue 
	private Long rbIdx;//렌트정보
	
	//private String bkIdx; 도서정보
	@ManyToOne //book 1개에 여러번의 렌트
	@JoinColumn(name="bkIdx") //어떤속성으로 외래키 만들어야하는지 잡아줌
	private Book book; 
	
	@ManyToOne 	//List<RentBook>때문에 JPA가 테이블을 하나더 만듦 정규화-->근데 JPA가 대신쿼리짜주니까 이렇게 굳이 안써줘도 상관없음
	@JoinColumn(name="rmIdx") //테이블생성 안되게끔, joincoulmn지정해줌
	private Rent rent; //(양방향관계;Rent에도 넣어주고,RentBook에도 넣어줌) 암튼 이컬럼을 만들있으니 table삭제해줌
	
	@Column(columnDefinition = "date default sysdate")
	private LocalDateTime regDate;
	private String state;
	
	@Column(columnDefinition = "date default sysdate+7")//7일뒤
	private LocalDateTime returnDate;
	
	@Column(columnDefinition = "number default 0")
	private Integer extensionCnt;
	
	public void changRent(Rent rent) {
		this.rent= rent;
		rent.getRentBooks().add(this); //주거니 받거니
	}

}
