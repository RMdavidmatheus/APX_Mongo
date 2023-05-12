package com.bbva.cmek.lib.rme2;

import com.bbva.cmek.dto.accounts.AccountDTO;

/**
 * The  interface CMEKRME2 class...
 */
public interface CMEKRME2 {

	// METHOD TO GET ACCOUNT
	AccountDTO executeGetAccount(String accountId);

	// METHOD TO VALIDATE ACCOUNT
	Boolean executeVerifyAccount(AccountDTO accountDTO);

}
