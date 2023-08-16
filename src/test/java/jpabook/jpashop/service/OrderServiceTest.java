package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

	@Autowired
	EntityManager em;
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderRepository;

	@DisplayName("상품을 주문한다")
	@Test
	void 상품주문() {

		// given
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("서울", "강가", "123 - 123"));
		em.persist(member);

		Item item = new Book();
		item.setName("JPA BOOK");
		item.setPrice(10000);
		item.setStockQuantity(10);
		em.persist(item);

		// when
		int orderCount = 5;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		//then
		Order order = orderRepository.findOne(orderId);

		assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
		assertThat(order.getOrderItems().size()).isEqualTo(1);
		assertThat(order.getTotalPrice()).isEqualTo(50000);
		assertThat(item.getStockQuantity()).isEqualTo(5);
		assertThat(order.getMember()).isEqualTo(member);

	}

	@DisplayName("재고 수량을 초과하여 상품을 주문한다.")
	@Test
	void 재고초과() {
		// given
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("서울", "강가", "123 - 123"));
		em.persist(member);

		Item item = new Book();
		item.setName("JPA BOOK");
		item.setPrice(10000);
		item.setStockQuantity(10);
		em.persist(item);

		// when
		int orderCount = 11;
//		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		//then
//		Order order = orderRepository.findOne(orderId);

		assertThrows(NotEnoughStockException.class, () -> {
			orderService.order(member.getId(), item.getId(), orderCount);
		});

	}

	@DisplayName("상품을 주문하고 취소한다.")
	@Test
	void 주문취소() {
		// given
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("서울", "강가", "123 - 123"));
		em.persist(member);

		Item item = new Book();
		item.setName("JPA BOOK");
		item.setPrice(10000);
		item.setStockQuantity(10);
		em.persist(item);

		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

		// when
		orderService.cancelOrder(orderId);

		// then
		Order getOrder = orderRepository.findOne(orderId);
		assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
		assertThat(item.getStockQuantity()).isEqualTo(10);
	}
}