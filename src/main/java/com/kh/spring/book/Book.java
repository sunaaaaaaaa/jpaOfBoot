package com.kh.spring.book;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@DynamicInsert 
@DynamicUpdate
@Data
public class Book {

	@Id //기본키
	@GeneratedValue() //JPA정책에 따라 시퀀스를 자동생성 (일렬번호,난수로도 가능)
	private Long bkIdx; //시퀀스잡을땐 long으로해야함
	
	private String isbn;
	private String category;
	private String title;
	private String author;
	private String info;
	
	@Column(columnDefinition = "number default 1")
	private Integer bookAmt;
	
	@Column(columnDefinition = "date default sysdate") //타입은 date 디폴트값은 sysdate
	private LocalDateTime regDate;
	
	@Column(columnDefinition = "number default 0")
	private Integer rentCnt;
	
}

