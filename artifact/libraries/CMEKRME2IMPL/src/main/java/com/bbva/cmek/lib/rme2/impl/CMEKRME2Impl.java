package com.bbva.cmek.lib.rme2.impl;

import com.bbva.cmek.dto.accounts.AccountDTO;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * The CMEKRME2Impl class...
 */
public class CMEKRME2Impl extends CMEKRME2Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(CMEKRME2Impl.class);

	@Override
	public AccountDTO executeGetAccount(String accountId) {
		LOGGER.debug("Executing api conection for get account");
		Map<String,String> params = new HashMap<>();
		params.put("urlContext", "account="+accountId);
		ResponseEntity<String> response = this.internalApiConnector.getForEntity("getAccount",String.class, params);
		String responseBody = response.getBody();
		Gson gson = new Gson();
		Map jsonMap = gson.fromJson(responseBody, Map.class);
		LOGGER.debug("Result from api consult: "+ jsonMap);
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(accountId);
		Double balance = (Double) jsonMap.get("balance");
		accountDTO.setBalance(balance.longValue());
		accountDTO.setAccountType((String) jsonMap.get("type"));
		accountDTO.setState((String) jsonMap.get("state"));
		accountDTO.setOwner((String) jsonMap.get("owner"));
		LOGGER.debug("Account created: "+ accountDTO.toString());
		return accountDTO;
	}

	@Override
	public Boolean executeVerifyAccount(AccountDTO accountDTO) {
		if (accountDTO.getBalance()<0){
			LOGGER.error("Account cant have negative balance");
			return false;
		}
		if (!Objects.equals(accountDTO.getAccountType(), "AHORROS")){
			LOGGER.error("Account type is not valid");
			return false;
		}
		if (!Objects.equals(accountDTO.getState(), "ACTIVE")){
			LOGGER.error("Account is not active");
			return false;
		}
		return true;
	}
}
