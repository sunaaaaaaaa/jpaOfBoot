package com.kh.spring.querydsl;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.spring.member.Member;
import com.kh.spring.rent.Rent;
import com.kh.spring.rent.RentBook;
import com.querydsl.core.Tuple;

public interface QueryDSLRepository extends JpaRepository<Member, String>, QueryDSLRepositoryCustom{ //실질적으로 프록시객체
	
}
