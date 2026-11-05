package com.caio.overwatch_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class OverwatchTrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}
