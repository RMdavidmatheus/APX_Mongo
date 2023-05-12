package com.bbva.cmek.lib.rme1.impl;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.bbva.cmek.dto.payments.BillDTO;
import com.bbva.cmek.dto.payments.PaymentDTO;
import com.bbva.cmek.lib.rme2.CMEKRME2;
import com.datiobd.daas.DaasMongoConnector;
import com.datiobd.daas.Parameters;
import com.datiobd.daas.conf.EnumOperation;
import com.datiobd.daas.conf.InsertOneOptionsWrapper;
import com.datiobd.daas.model.DocumentWrapper;
import com.datiobd.daas.model.json.FiltersWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The CMEKRME1Impl class...
 */
public class CMEKRME1Impl extends CMEKRME1Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(CMEKRME1Impl.class);

	private static final String putString = "{'DATABASE_NAME' : 'BMG_CMEK_CO_PAYM','table' : 'c_cmek_payment_reg_operation', 'detail' : {'payment_id' : '%s', 'value' : %s, 'date' : '%s', 'provider' : '%s'}}";

	@Override
	public PaymentDTO executeDoPayment(PaymentDTO paymentDTO) {
		/**
		 *  METHOD EXECUTE THE PAYMENT
		 */
		AccountDTO accountDTO = cmekRME2.executeGetAccount(paymentDTO.getOriginAccount().getId());
		BillDTO billDTO = executeGetBill(paymentDTO.getBill().getNumber());
		if (billDTO.getValue() > accountDTO.getBalance()) {
			// VALIDATING THE ACCOUNT BALANCE
			LOGGER.error("Account not have balance");
			return paymentDTO;
		}else if (!billDTO.getState().equals("A")){
			// VALIDATE THE STATE OF BILL
			LOGGER.error("Bill not have a pending");
			return paymentDTO;
		}else if (Objects.isNull(billDTO)){
			// VALIDATE BILL NOT NULL
			return paymentDTO;
		}else if (!cmekRME2.executeVerifyAccount(accountDTO)) {
			// EXECUTING THE ACCOUNT VALIDATE
			return paymentDTO;
		}else {
			/**
			 *  Executing transaction
			 */
			// SET THE PAYMENT DATA..
			accountDTO.setBalance(accountDTO.getBalance() - billDTO.getValue());
			paymentDTO.setId(String.valueOf(UUID.randomUUID()));
			paymentDTO.setOperationDateTime(LocalDateTime.now().toString());
			billDTO.setState("P");
			paymentDTO.setBill(billDTO);
			paymentDTO.setOriginAccount(accountDTO);
			// END SETING DATA

			// EXECUTING THE INSERT ON MONGO
			executePutPayment(paymentDTO);
			//
			// EXECUTING THE UPDATE ON STATE OF BILL
			executeUpdateBill(paymentDTO.getBill());
			//
			return paymentDTO;
		}
	}

	@Override
	public BillDTO executeGetBill(String billId) {
		/**
		 *  METHOD DO GET THE BILL
		 */
		LOGGER.debug("Executing consult SELECT");
		BillDTO billDTO = new BillDTO();
		List<Map<String,Object>> result = jdbcUtils.queryForList("cmek.selectBillById", billId);
		if (result.isEmpty()){
			LOGGER.error("Bill id "+ billId +" not exist");
			return null;
		}
		// BILL SET DATA
		billDTO.setNumber(result.get(0).get("BILL_ID").toString());
		BigDecimal value = (BigDecimal) result.get(0).get("BILL_AMOUNT");
		billDTO.setValue(value.longValue());
		billDTO.setState(result.get(0).get("BILL_STATE_NAME").toString());
		billDTO.setSupplier(result.get(0).get("BILL_OWNER_NAME").toString());
		// END SET DATA OF BILL
		LOGGER.debug("bill result: "+ billDTO.toString());
		//
		return billDTO;
	}

	@Override
	public void executePutPayment(PaymentDTO paymentDTO) {
		/**
		 *  EXECUTE THE MONGO INSERT
		 */
		LOGGER.debug("Executing Mongo insertion");
		Map<String, Object> params = new HashMap<>();
		params.put(Parameters.DATABASE_PROPERTY_NAME, "BMG_CMEK_CO_PAYM");
		params.put(Parameters.COLLECTION, "c_cmek_payment_reg_operation");
		params.put(Parameters.DOCUMENT, DocumentWrapper.parse(String.format(putString,paymentDTO.getId(),paymentDTO.getBill().getValue(),paymentDTO.getOperationDateTime(),paymentDTO.getBill().getSupplier())));
		params.put(Parameters.API_OR_REST, Parameters.API);
		daasMongoConnector.executeWithNoReturn(EnumOperation.INSERT_ONE, params);
		LOGGER.debug("Document inserted");
	}

	@Override
	public void executeUpdateBill(BillDTO billDTO) {
		/**
		 *  EXECUTE THE UPDATE METHOD
		 */
		LOGGER.debug("Executing sql update updateBillStateById");
		jdbcUtils.update("cmek.updateBillStateById", billDTO.getState(),billDTO.getNumber());
		LOGGER.debug("State updated");
	}


}
