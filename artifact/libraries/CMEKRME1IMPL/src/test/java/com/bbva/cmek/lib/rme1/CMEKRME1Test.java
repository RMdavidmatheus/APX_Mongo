package com.bbva.cmek.lib.rme1;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.bbva.cmek.dto.payments.BillDTO;
import com.bbva.cmek.dto.payments.PaymentDTO;
import com.bbva.cmek.lib.rme1.impl.CMEKRME1Impl;
import com.bbva.cmek.lib.rme2.CMEKRME2;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import javax.annotation.Resource;

import com.bbva.elara.utility.jdbc.connector.JdbcUtils;
import com.datiobd.daas.DaasMongoConnector;
import com.datiobd.daas.conf.EnumOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.aop.framework.Advised;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.*;
import java.util.jar.JarEntry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/CMEKRME1-app.xml",
		"classpath:/META-INF/spring/CMEKRME1-app-test.xml",
		"classpath:/META-INF/spring/CMEKRME1-arc.xml",
		"classpath:/META-INF/spring/CMEKRME1-arc-test.xml" })
public class CMEKRME1Test {

	@Spy
	private Context context;

	@InjectMocks
	private CMEKRME1Impl cmekRME1;
	@Mock
	private JdbcUtils jdbcUtils;
	@Mock
	private DaasMongoConnector daasMongoConnector;
	@Mock
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
	
	private Object getObjectIntrospection() throws Exception{
		Object result = this.cmekRME1;
		if(this.cmekRME1 instanceof Advised){
			Advised advised = (Advised) this.cmekRME1;
			result = advised.getTargetSource().getTarget();
		}
		return result;
	}
	
	@Test
	public void executeTest() {
		/**
		 *  TESTING METHOD BILL METHOD
		 */
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setOwner("Alejandro");
		accountDTO.setAccountType("Ahorros");
		accountDTO.setBalance(1000000);
		accountDTO.setState("active");
		accountDTO.setId(UUID.randomUUID().toString());
		Mockito.when(cmekRME2.executeGetAccount(Mockito.anyString())).thenReturn(accountDTO);
		BillDTO billDTO = new BillDTO();
		billDTO.setNumber("a1");
		List<Map<String,Object>> billsInfo = new ArrayList<Map<String,Object>>();
		Map<String,Object> billInfo = new HashMap<>();
		billInfo.put("BILL_ID","a2");
		billInfo.put("BILL_AMOUNT",new BigDecimal(4211));
		billInfo.put("BILL_STATE_NAME","A");
		billInfo.put("BILL_OWNER_NAME","BBVA");
		billsInfo.add(billInfo);
		Mockito.when(cmekRME2.executeVerifyAccount(accountDTO)).thenReturn(true);
		Mockito.when(jdbcUtils.queryForList("cmek.selectBillById", "a1")).thenReturn(billsInfo);
		Mockito.when(jdbcUtils.update("cmek.updateBillStateById", "P","a2")).thenReturn(1);
		Mockito.when(daasMongoConnector.executeWithReturn(EnumOperation.INSERT_ONE, new HashMap<>())).thenReturn(1);
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setOriginAccount(accountDTO);
		paymentDTO.setBill(billDTO);
		cmekRME1.executeDoPayment(paymentDTO);
		Assert.assertEquals(0, context.getAdviceList().size());
	}

	@Test
	public void executeGetNullBillTest() {
		/**
		 *  EXECUTING MOCKITO
		 */
		Mockito.when(jdbcUtils.queryForList("cmek.selectBillById", "al")).thenReturn(new ArrayList<>());
		Assert.assertEquals(null,cmekRME1.executeGetBill("al"));
	}

	@Test
	public void executeDoPaymentWithoutEnoughBalanceTest(){
		/**
		 * SET THE DATA
		 */
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setOwner("Alejandro Mateus");
		accountDTO.setAccountType("Ahorros");
		accountDTO.setBalance(0);
		accountDTO.setState("active");
		accountDTO.setId(UUID.randomUUID().toString());
		/**
		 *  END SET DATA
		 */

		/**
		 *  EXECUTING THE MOCKITO
		 */
		Mockito.when(cmekRME2.executeGetAccount(Mockito.anyString())).thenReturn(accountDTO);
		/**
		 *  END EXECUTING MOCKITO
		 */

		/**
		 *  SET THE BILL
		 */
		BillDTO billDTO = new BillDTO();
		billDTO.setNumber("al");
		List<Map<String,Object>> billsInfo = new ArrayList<Map<String,Object>>();
		Map<String,Object> billInfo = new HashMap<>();
		billInfo.put("BILL_ID","al");
		billInfo.put("BILL_AMOUNT",new BigDecimal(4211));
		billInfo.put("BILL_STATE_NAME","A");
		billInfo.put("BILL_OWNER_NAME","claro");
		billsInfo.add(billInfo);
		/**
		 *  END SET BILL
		 */

		/**
		 *  PAYMENT SET
		 */
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setOriginAccount(accountDTO);
		paymentDTO.setBill(billDTO);
		/**
		 *  END PAYMENT SET
		 */

		/**
		 *  EXECUTING MOCKITO
		 */
		Mockito.when(jdbcUtils.queryForList("cmek.selectBillById", "al")).thenReturn(billsInfo);
		Assert.assertEquals(paymentDTO,cmekRME1.executeDoPayment(paymentDTO));
	}

}
