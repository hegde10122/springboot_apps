package com.hegde.springbootjdbc.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class SpringbootjbdcApplicationTests {

	private Calculator calculator = new Calculator();

	@Test
	void contextLoads() {
	}

	@Test
	void testSum(){

		long expectedResult = 3L * Integer.MAX_VALUE;
		long actualResult = calculator.sum(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void testProd(){
		long expectedResult = (long) Integer.MAX_VALUE * Integer.MAX_VALUE;
		long actualResult = calculator.mult(Integer.MAX_VALUE,Integer.MAX_VALUE);
		long actualR2 = calculator.mult(Integer.MIN_VALUE,Integer.MIN_VALUE);

		assertThat(actualResult).isEqualTo(expectedResult);
		assertThat(actualR2).isEqualTo(Integer.MIN_VALUE * (long) Integer.MIN_VALUE);
	}

	@Test
	void testCompareNums(){

		boolean actualResult = calculator.compare2Nums(Integer.MIN_VALUE,Integer.MIN_VALUE);
		assertThat(actualResult).isTrue();

		boolean actualResult2 = calculator.compare2Nums(Integer.MAX_VALUE,Integer.MIN_VALUE);
		assertThat(actualResult2).isFalse();
	}

}
