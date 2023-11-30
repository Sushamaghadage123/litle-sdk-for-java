package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import io.github.vantiv.sdk.generate.*;

public class TestAuthReversal {

	private static LitleOnline litle;

	@BeforeClass
	public static void beforeClass() throws Exception {
		litle = new LitleOnline();
	}
	
	@Test
	public void simpleAuthReversal() throws Exception{
		AuthReversal reversal = new AuthReversal();
		reversal.setLitleTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");
		reversal.setId("id");
		
		AuthReversalResponse response = litle.authReversal(reversal);
		assertEquals("Transaction Received", response.getMessage());
	}

}