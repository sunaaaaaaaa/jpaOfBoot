package com.kh.spring.rent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.kh.spring.member.Member;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

@Entity
@DynamicInsert //insert쿼리 작성시 null값인 필드는 생략하고 넣어줌
@DynamicUpdate //update쿼리 작성시 변경이 감지되지 않는 필드는 생략하고 업뎃 (기본적으로 Entity,DynamicInsert,DynamicUpdate 어노테이션넣어줌)
@Data
@ToString(exclude = {"rentBooks"})
public class Rent {

	@Id 
	@GeneratedValue 
	private Long rmIdx;
	
	@Column(columnDefinition = "date default sysdate")
	private LocalDateTime regDate;
	
	@Column(columnDefinition = "number default 0")
	private Boolean isReturn;
	
	private String title;
	
	@Column(columnDefinition = "number default 0")
	private Integer rentBookCnt;
	
	//private String userId; userId가 아니라 Member객체를 다받아옴
	@ManyToOne	//연관관계주인(주로 ManyToOne)은 매핑어쩌고가 안뜸
	@JoinColumn(name="userId")
	private Member member; //member 1명이 여러개 Rent를 가질수있음 -->ManyToOne
	
	//CascadeType
	//PERSIST : PERSIST를 수행할때 연관엔티티도 함께 수행
	//MERGE : 준영속상태의 엔티티를 MERGE할 때 연관엔티티도 함께 MERGE
	//REMOVE : 엔티티를 삭제할 때 연관엔티티도 함께 삭제
	//DETACH : 영속상태의 엔티티를 준영속상태로 만들 때 연관엔티티도 함께 수행
	//ALL : PERSIST + MERGE + REMOVE + DETACH
	//이경우 필드를 초기화 해둘것. nullpoint막기위해..
	//@JoinColumn(name="rmIdx")
	@Setter(value=AccessLevel.NONE) //롬복을사용해서 setter가 안생기게 막음(AccessLevel:접근제한 막음)->양방향 매핑 할때
	@OneToMany(cascade=CascadeType.ALL,mappedBy ="rent", fetch=FetchType.EAGER)//(mappedBy ="rent") 변수 rent와 매핑해줌(Rent가 연관관계주인임;보통여러개인쪽이) 
	private List<RentBook> rentBooks =new ArrayList<RentBook>(); //Rent는 1갠데 RentBook은 여러개
	
	public void changRentBooks(List<RentBook> rentBooks) {
		this.rentBooks =rentBooks;
		for (RentBook rentBook : rentBooks) {
			rentBook.changRent(this); //주거니 받거니
		}
	}
}
