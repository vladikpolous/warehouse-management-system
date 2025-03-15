package com.warehouse.backend;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@SuiteDisplayName("All Tests Suite")
@SelectPackages("com.warehouse.backend")
@SpringBootTest
class BackendApplicationTests {

}
