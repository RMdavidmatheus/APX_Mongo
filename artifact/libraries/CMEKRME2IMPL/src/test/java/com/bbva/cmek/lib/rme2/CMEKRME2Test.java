package com.bbva.cmek.lib.rme2;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.elara.utility.api.connector.APIConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.aop.framework.Advised;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/META-INF/spring/CMEKRME2-app.xml",
        "classpath:/META-INF/spring/CMEKRME2-app-test.xml",
        "classpath:/META-INF/spring/CMEKRME2-arc.xml",
        "classpath:/META-INF/spring/CMEKRME2-arc-test.xml"})
public class CMEKRME2Test {

    @Spy
    private Context context;

    @Mock
    private APIConnector apiConnector;

    @Resource(name = "cmekRME2")
    private CMEKRME2 cmekRME2;

    @Resource(name = "applicationConfigurationService")
    private ApplicationConfigurationService applicationConfigurationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        context = new Context();
        ThreadContext.set(context);
        getObjectIntrospection();
    }

    private Object getObjectIntrospection() throws Exception {
        Object result = this.cmekRME2;
        if (this.cmekRME2 instanceof Advised) {
            Advised advised = (Advised) this.cmekRME2;
            result = advised.getTargetSource().getTarget();
        }
        return result;
    }

    @Test
    public void executeTest() {
        /**
         *  Testing EXT Api Conncector
         */
        Map<String,String> params = new HashMap<>();
        params.put("urlContext", "account=1234");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = new ResponseEntity<String>("{id=1234, balance=21414, type=AHORROS, state=ACTIVE, owner=DEFAULT_USER}",header,HttpStatus.OK);
        Mockito.when(apiConnector.getForEntity("getAccount",String.class, params)).thenReturn(response);
        //cmekRME2.executeGetAccount("1234");
        Assert.assertEquals(0, context.getAdviceList().size());
    }

}
