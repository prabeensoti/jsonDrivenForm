package com.jsondriventemplate.app.impl;

import com.jsondriventemplate.app.AbstractJDTScriptTest;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * full flow of application
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JDTScriptIT extends AbstractJDTScriptTest {


    public JDTScriptIT() {
        super(false);
    }

}
