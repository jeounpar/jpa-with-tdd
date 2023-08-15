package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;


	@DisplayName("회원을 생성하고 회원 가입을 시도한다.")
	@Test
	void 회원가입() {
		// given
		Member member = new Member();
		member.setName("kim");

		// when
		Long savedId = memberService.join(member);
		Member savedMember = memberRepository.findOne(savedId);

		//then
		assertThat(member).isEqualTo(savedMember);
	}

	@DisplayName("회원을 중복 생성하고 회원 가입을 시도한다")
	@Test
	void 중복_회원_예외() {
		// given
		Member member1 = new Member();
		member1.setName("kim");

		Member member2 = new Member();
		member2.setName("kim");

		// when
		memberService.join(member1);
		assertThrows(IllegalStateException.class, () -> {
			memberService.join(member2);
		});
	}
}