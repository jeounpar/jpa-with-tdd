package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

	@Autowired
	MemberRepository memberRepository;

	@DisplayName("멤버를 하나 생성해서 데이터베이스에 저장한다")
	@Test
	@Transactional
	void memberSave() {
		// given
		Member member = new Member();
		member.setUsername("memberA");

		// when
		Long saveId = memberRepository.save(member);
		Member findMember = memberRepository.find(saveId);

		//then
		assertThat(saveId).isEqualTo(findMember.getId());
		assertThat(member.getUsername()).isEqualTo(findMember.getUsername());
		assertThat(findMember).isEqualTo(member);
	}
}