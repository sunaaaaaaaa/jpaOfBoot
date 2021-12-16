package com.kh.spring.member;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Entity
@DynamicInsert //insert쿼리 작성시 null값인 필드는 생략하고 넣어줌
@DynamicUpdate //update쿼리 작성시 변경이 감지되지 않는 필드는 생략하고 업뎃 (기본적으로 Entity,DynamicInsert,DynamicUpdate 어노테이션넣어줌)
@Data
public class Member {	
	
	@Id
	private String userId;
	
	private String password;
	private String email;
	private String grade;
	private String tell;
	
	@Column(columnDefinition = "date default sysdate")
	private LocalDateTime rentableDate;
	
	@Column(columnDefinition = "date default sysdate") //타입은 date 디폴트값은 sysdate
	private LocalDateTime regDate;
	
	@Column(columnDefinition = "number default 0")
	private Boolean isLeave;  //oracle은 boolean이 없어서 number로잡아줌
	
	
}
