package com.bbva.cmek.lib.rme1;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.bbva.cmek.dto.payments.BillDTO;
import com.bbva.cmek.dto.payments.PaymentDTO;

/**
 * The  interface CMEKRME1 class...
 */
public interface CMEKRME1 {

	/**
	 * The execute method...
	 */
	PaymentDTO executeDoPayment(PaymentDTO paymentDTO);

	BillDTO executeGetBill(String billId);

	void executePutPayment(PaymentDTO paymentDTO);

	void executeUpdateBill(BillDTO billDTO);

}
