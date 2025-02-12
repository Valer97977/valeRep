package com.betacom.bec;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Suite
@SelectClasses({

		ProdottoControllerTest.class, ProdottoTest.class,
		CarrelloControllerTest.class, CarrelloTest.class,

})

@SpringBootTest
@ActiveProfiles(value = "sviluppo") // così capisce quale dei due socioimpl deve usare
class BackendEcommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
