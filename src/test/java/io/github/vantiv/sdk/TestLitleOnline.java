package io.github.vantiv.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Properties;

import javax.xml.bind.JAXBElement;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Before;
import org.junit.Test;

import io.github.vantiv.sdk.generate.ActionTypeEnum;
import io.github.vantiv.sdk.generate.Activate;
import io.github.vantiv.sdk.generate.ActivateResponse;
import io.github.vantiv.sdk.generate.ActivateReversal;
import io.github.vantiv.sdk.generate.ActivateReversalResponse;
import io.github.vantiv.sdk.generate.AdvancedFraudChecksType;
import io.github.vantiv.sdk.generate.AdvancedFraudResultsType;
import io.github.vantiv.sdk.generate.ApplepayHeaderType;
import io.github.vantiv.sdk.generate.ApplepayType;
import io.github.vantiv.sdk.generate.AuthInformation;
import io.github.vantiv.sdk.generate.AuthReversal;
import io.github.vantiv.sdk.generate.AuthReversalResponse;
import io.github.vantiv.sdk.generate.Authentication;
import io.github.vantiv.sdk.generate.Authorization;
import io.github.vantiv.sdk.generate.AuthorizationResponse;
import io.github.vantiv.sdk.generate.BalanceInquiry;
import io.github.vantiv.sdk.generate.BalanceInquiryResponse;
import io.github.vantiv.sdk.generate.CancelSubscription;
import io.github.vantiv.sdk.generate.CancelSubscriptionResponse;
import io.github.vantiv.sdk.generate.Capture;
import io.github.vantiv.sdk.generate.CaptureGivenAuth;
import io.github.vantiv.sdk.generate.CaptureGivenAuthResponse;
import io.github.vantiv.sdk.generate.CaptureResponse;
import io.github.vantiv.sdk.generate.CardType;
import io.github.vantiv.sdk.generate.Contact;
import io.github.vantiv.sdk.generate.CreateAddOnType;
import io.github.vantiv.sdk.generate.CreatePlan;
import io.github.vantiv.sdk.generate.CreatePlanResponse;
import io.github.vantiv.sdk.generate.Credit;
import io.github.vantiv.sdk.generate.CreditResponse;
import io.github.vantiv.sdk.generate.CustomerInfo;
import io.github.vantiv.sdk.generate.Deactivate;
import io.github.vantiv.sdk.generate.DeactivateResponse;
import io.github.vantiv.sdk.generate.DeactivateReversal;
import io.github.vantiv.sdk.generate.DeactivateReversalResponse;
import io.github.vantiv.sdk.generate.DepositReversal;
import io.github.vantiv.sdk.generate.DepositReversalResponse;
import io.github.vantiv.sdk.generate.EcheckAccountTypeEnum;
import io.github.vantiv.sdk.generate.EcheckCredit;
import io.github.vantiv.sdk.generate.EcheckCreditResponse;
import io.github.vantiv.sdk.generate.EcheckRedeposit;
import io.github.vantiv.sdk.generate.EcheckRedepositResponse;
import io.github.vantiv.sdk.generate.EcheckSale;
import io.github.vantiv.sdk.generate.EcheckSalesResponse;
import io.github.vantiv.sdk.generate.EcheckType;
import io.github.vantiv.sdk.generate.EcheckVerification;
import io.github.vantiv.sdk.generate.EcheckVerificationResponse;
import io.github.vantiv.sdk.generate.EcheckVoid;
import io.github.vantiv.sdk.generate.EcheckVoidResponse;
import io.github.vantiv.sdk.generate.ForceCapture;
import io.github.vantiv.sdk.generate.ForceCaptureResponse;
import io.github.vantiv.sdk.generate.FraudCheck;
import io.github.vantiv.sdk.generate.FraudCheckResponse;
import io.github.vantiv.sdk.generate.LitleOnlineRequest;
import io.github.vantiv.sdk.generate.Load;
import io.github.vantiv.sdk.generate.LoadResponse;
import io.github.vantiv.sdk.generate.LoadReversal;
import io.github.vantiv.sdk.generate.LoadReversalResponse;
import io.github.vantiv.sdk.generate.MethodOfPaymentTypeEnum;
import io.github.vantiv.sdk.generate.OrderSourceType;
import io.github.vantiv.sdk.generate.QueryTransaction;
import io.github.vantiv.sdk.generate.QueryTransactionResponse;
import io.github.vantiv.sdk.generate.QueryTransactionUnavailableResponse;
import io.github.vantiv.sdk.generate.RecurringRequestType;
import io.github.vantiv.sdk.generate.RecurringSubscriptionType;
import io.github.vantiv.sdk.generate.RefundReversal;
import io.github.vantiv.sdk.generate.RefundReversalResponse;
import io.github.vantiv.sdk.generate.RegisterTokenRequestType;
import io.github.vantiv.sdk.generate.RegisterTokenResponse;
import io.github.vantiv.sdk.generate.Sale;
import io.github.vantiv.sdk.generate.SaleResponse;
import io.github.vantiv.sdk.generate.TransactionTypeWithReportGroup;
import io.github.vantiv.sdk.generate.Unload;
import io.github.vantiv.sdk.generate.UnloadResponse;
import io.github.vantiv.sdk.generate.UnloadReversal;
import io.github.vantiv.sdk.generate.UnloadReversalResponse;
import io.github.vantiv.sdk.generate.UpdatePlan;
import io.github.vantiv.sdk.generate.UpdatePlanResponse;
import io.github.vantiv.sdk.generate.UpdateSubscription;
import io.github.vantiv.sdk.generate.UpdateSubscriptionResponse;
import io.github.vantiv.sdk.generate.Wallet;
import io.github.vantiv.sdk.generate.WalletSourceType;

public class TestLitleOnline {

	private LitleOnline litle;
	private BasicHttpContext context;

	@Before
	public void before() throws Exception {
		litle = new LitleOnline();
	}

	@Test
	public void testAuth() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse><litleTxnId>123</litleTxnId></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = litle.authorize(authorization);
		assertEquals(123L, authorize.getLitleTxnId());
	}
	
	@Test
	public void testAuth_withErrorResponse_InvalidCredential() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version=\"1.0\" xmlns=\"http://www.litle.com/schema/online\" response=\"3\" message=\"Invalid credentials. Contact support@litle.com.\"></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		try {
			litle.authorize(authorization);
			fail("Should throw an exception!");
		} catch (VantivInvalidCredentialException vice) {
			assertEquals("Invalid credentials. Contact support@litle.com.", vice.getMessage());
		}
	}
	
	@Test
	public void testAuth_withErrorResponse_ConnectionLimitExceeded() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version=\"1.0\" xmlns=\"http://www.litle.com/schema/online\" response=\"4\" message=\"Connection limit exceeded. Contact support@litle.com.\"></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		try {
			litle.authorize(authorization);
			fail("Should throw an exception!");
		} catch (VantivConnectionLimitExceededException vcle) {
			assertEquals("Connection limit exceeded. Contact support@litle.com.", vcle.getMessage());
		}
	}
	
	@Test
	public void testAuth_withErrorResponse_ObjectionableContent() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version=\"1.0\" xmlns=\"http://www.litle.com/schema/online\" response=\"5\" message=\"Objectionable content detected. Contact support@litle.com.\"></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		try {
			litle.authorize(authorization);
			fail("Should throw an exception!");
		} catch (VantivObjectionableContentException voce) {
			assertEquals("Objectionable content detected. Contact support@litle.com.", voce.getMessage());
		}
	}
	
	@Test
    public void testAuthWithApplepayAndSecondaryAmountAndWallet() throws Exception {
        Authorization authorization = new Authorization();
        authorization.setReportGroup("Planets");
        authorization.setOrderId("12344");
        authorization.setAmount(106L);
        authorization.setSecondaryAmount(10L);
        authorization.setOrderSource(OrderSourceType.ECOMMERCE);
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        authorization.setApplepay(applepayType);
        
        Wallet wallet = new Wallet();
        wallet.setWalletSourceType(WalletSourceType.MASTER_PASS);
        wallet.setWalletSourceTypeId("123");
        authorization.setWallet(wallet);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                .requestToServer(
                        matches(".*?<litleOnlineRequest.*?<authorization.*?<secondaryAmount>10</secondaryAmount>.*?<applepay>.*?<data>user</data>.*?</applepay>.*?<wallet>.*?<walletSourceTypeId>123</walletSourceTypeId>.*?</wallet>.*?</authorization>.*?"),
                        any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse><litleTxnId>123</litleTxnId><applepayResponse><applicationPrimaryAccountNumber>123455</applicationPrimaryAccountNumber><transactionAmount>106</transactionAmount></applepayResponse></authorizationResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        AuthorizationResponse authorize = litle.authorize(authorization);
        assertEquals(123L, authorize.getLitleTxnId());
        assertEquals("123455", authorize.getApplepayResponse().getApplicationPrimaryAccountNumber());
        assertEquals(new Long(106), authorize.getApplepayResponse().getTransactionAmount());
    }

	@Test
	public void testAuthWithOverrides() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?merchantId=\"9001\".*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse><litleTxnId>123</litleTxnId></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		LitleOnlineRequest overrides = new LitleOnlineRequest();
		overrides.setMerchantId("9001");
		AuthorizationResponse authorize = litle.authorize(authorization, overrides);
		assertEquals(123L, authorize.getLitleTxnId());
	}

	@Test
	public void testAuthReversal() throws Exception {
		AuthReversal reversal = new AuthReversal();
		reversal.setLitleTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authReversal.*?<litleTxnId>12345678000</litleTxnId>.*?</authReversal>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authReversalResponse><litleTxnId>123</litleTxnId></authReversalResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		AuthReversalResponse authreversal = litle.authReversal(reversal);
		assertEquals(123L, authreversal.getLitleTxnId());
	}

	@Test
	public void testAuthReversalWithOverrides() throws Exception {
		AuthReversal reversal = new AuthReversal();
		reversal.setLitleTxnId(12345678000L);
		reversal.setAmount(106L);
		reversal.setPayPalNotes("Notes");


		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?merchantId=\"54321\".*?<authReversal.*?<litleTxnId>12345678000</litleTxnId>.*?</authReversal>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.11' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authReversalResponse><litleTxnId>123</litleTxnId></authReversalResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		LitleOnlineRequest overrides = new LitleOnlineRequest();
		overrides.setMerchantId("54321");
		AuthReversalResponse authreversal = litle.authReversal(reversal, overrides);
		assertEquals(123L, authreversal.getLitleTxnId());
	}

	@Test
	public void testCapture() throws Exception {
		Capture capture = new Capture();
		capture.setLitleTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<capture.*?<litleTxnId>123456000</litleTxnId>.*?</capture>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><captureResponse><litleTxnId>123</litleTxnId></captureResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		CaptureResponse captureresponse = litle.capture(capture);
		assertEquals(123L, captureresponse.getLitleTxnId());
	}

	@Test
	public void testCaptureWithOverrides() throws Exception {
		Capture capture = new Capture();
		capture.setLitleTxnId(123456000L);
		capture.setAmount(106L);
		capture.setPayPalNotes("Notes");
		
		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<password>supersecret</password>.*?<capture.*?<litleTxnId>123456000</litleTxnId>.*?</capture>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><captureResponse><litleTxnId>123</litleTxnId></captureResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		LitleOnlineRequest overrides = new LitleOnlineRequest();
		overrides.setAuthentication(new Authentication());
		overrides.getAuthentication().setPassword("supersecret");
		CaptureResponse captureresponse = litle.capture(capture, overrides);
		assertEquals(123L, captureresponse.getLitleTxnId());
	}

	@Test
	public void testCaptureGivenAuth() throws Exception {
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setSecondaryAmount(10L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
							matches(".*?<litleOnlineRequest.*?<captureGivenAuth.*?<secondaryAmount>10</secondaryAmount>.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</captureGivenAuth>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><captureGivenAuthResponse><litleTxnId>123</litleTxnId></captureGivenAuthResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		CaptureGivenAuthResponse capturegivenauthresponse = litle.captureGivenAuth(capturegivenauth);
		assertEquals(123L, capturegivenauthresponse.getLitleTxnId());
	}

	@Test
	public void testCaptureGivenAuthWithOverrides() throws Exception {
		CaptureGivenAuth capturegivenauth = new CaptureGivenAuth();
		capturegivenauth.setAmount(106L);
		capturegivenauth.setOrderId("12344");
		AuthInformation authInfo = new AuthInformation();
		Calendar authDate = Calendar.getInstance();
		authDate.set(2002, Calendar.OCTOBER, 9);
		authInfo.setAuthDate(authDate);
		authInfo.setAuthCode("543216");
		authInfo.setAuthAmount(12345L);
		capturegivenauth.setAuthInformation(authInfo);
		capturegivenauth.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		capturegivenauth.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
							matches(".*?<litleOnlineRequest.*?<user>neweruser</user>.*?<captureGivenAuth.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</captureGivenAuth>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><captureGivenAuthResponse><litleTxnId>123</litleTxnId></captureGivenAuthResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		LitleOnlineRequest overrides = new LitleOnlineRequest();
		overrides.setAuthentication(new Authentication());
		overrides.getAuthentication().setUser("neweruser");
		CaptureGivenAuthResponse capturegivenauthresponse = litle.captureGivenAuth(capturegivenauth, overrides);
		assertEquals(123L, capturegivenauthresponse.getLitleTxnId());
	}

	@Test
	public void testCredit() throws Exception {
		Credit credit = new Credit();
		credit.setAmount(106L);
        credit.setSecondaryAmount(10L);
		credit.setOrderId("12344");
		credit.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		credit.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<credit.*?<secondaryAmount>10</secondaryAmount>.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</credit>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><creditResponse><litleTxnId>123</litleTxnId></creditResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		CreditResponse creditresponse = litle.credit(credit);
		assertEquals(123L, creditresponse.getLitleTxnId());
	}

	@Test
	public void testEcheckCredit() throws Exception {
		EcheckCredit echeckcredit = new EcheckCredit();
		echeckcredit.setAmount(12L);
        echeckcredit.setSecondaryAmount(10L);
		echeckcredit.setLitleTxnId(123456789101112L);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<echeckCredit.*?<litleTxnId>123456789101112</litleTxnId>.*?<secondaryAmount>10</secondaryAmount>.*?</echeckCredit>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><echeckCreditResponse><litleTxnId>123</litleTxnId></echeckCreditResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		EcheckCreditResponse echeckcreditresponse = litle.echeckCredit(echeckcredit);
		assertEquals(123L, echeckcreditresponse.getLitleTxnId());
	}

	@Test
	public void testEcheckRedeposit() throws Exception {
		EcheckRedeposit echeckredeposit = new EcheckRedeposit();
		echeckredeposit.setLitleTxnId(123456L);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<echeckRedeposit.*?<litleTxnId>123456</litleTxnId>.*?</echeckRedeposit>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><echeckRedepositResponse><litleTxnId>123</litleTxnId></echeckRedepositResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		EcheckRedepositResponse echeckredepositresponse = litle.echeckRedeposit(echeckredeposit);
		assertEquals(123L, echeckredepositresponse.getLitleTxnId());
	}

	@Test
	public void testEcheckSale() throws Exception {
		EcheckSale echecksale = new EcheckSale();
		echecksale.setAmount(123456L);
        echecksale.setSecondaryAmount(10L);
		echecksale.setOrderId("12345");
		echecksale.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeck.setCcdPaymentInformation("1234567890");
		echecksale.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echecksale.setBillToAddress(contact);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<echeckSale.*?<secondaryAmount>10</secondaryAmount>.*?<echeck>.*?<accNum>12345657890</accNum>.*?</echeck>.*?</echeckSale>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><echeckSalesResponse><litleTxnId>123</litleTxnId></echeckSalesResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		EcheckSalesResponse echecksaleresponse = litle.echeckSale(echecksale);
		assertEquals(123L, echecksaleresponse.getLitleTxnId());
	}

	@Test
	public void testEcheckVerification() throws Exception {
		EcheckVerification echeckverification = new EcheckVerification();
		echeckverification.setAmount(123456L);
		echeckverification.setOrderId("12345");
		echeckverification.setOrderSource(OrderSourceType.ECOMMERCE);
		EcheckType echeck = new EcheckType();
		echeck.setAccType(EcheckAccountTypeEnum.CHECKING);
		echeck.setAccNum("12345657890");
		echeck.setRoutingNum("123456789");
		echeck.setCheckNum("123455");
		echeckverification.setEcheck(echeck);
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setCity("lowell");
		contact.setState("MA");
		contact.setEmail("litle.com");
		echeckverification.setBillToAddress(contact);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<echeckVerification.*?<echeck>.*?<accNum>12345657890</accNum>.*?</echeck>.*?</echeckVerification>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><echeckVerificationResponse><litleTxnId>123</litleTxnId></echeckVerificationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		EcheckVerificationResponse echeckverificationresponse = litle.echeckVerification(echeckverification);
		assertEquals(123L, echeckverificationresponse.getLitleTxnId());
	}

	@Test
	public void testForceCapture() throws Exception {
		ForceCapture forcecapture = new ForceCapture();
		forcecapture.setAmount(106L);
		forcecapture.setSecondaryAmount(10L);
		forcecapture.setOrderId("12344");
		forcecapture.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000001");
		card.setExpDate("1210");
		forcecapture.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<forceCapture.*?<secondaryAmount>10</secondaryAmount>.*?<card>.*?<number>4100000000000001</number>.*?</card>.*?</forceCapture>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><forceCaptureResponse><litleTxnId>123</litleTxnId></forceCaptureResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		ForceCaptureResponse forcecaptureresponse = litle.forceCapture(forcecapture);
		assertEquals(123L, forcecaptureresponse.getLitleTxnId());
	}

	@Test
	public void testSale() throws Exception {
		Sale sale = new Sale();
		sale.setAmount(106L);
		sale.setLitleTxnId(123456L);
		sale.setOrderId("12344");
		sale.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		sale.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<sale.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</sale>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><saleResponse><litleTxnId>123</litleTxnId></saleResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		SaleResponse saleresponse = litle.sale(sale);
		assertEquals(123L, saleresponse.getLitleTxnId());
	}
	
	@Test
    public void testSaleWithApplepayAndSecondaryAmountAndWallet() throws Exception {
        Sale sale = new Sale();
        sale.setAmount(106L);
        sale.setSecondaryAmount(10L);
        sale.setLitleTxnId(123456L);
        sale.setOrderId("12344");
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        sale.setApplepay(applepayType);

        Wallet wallet = new Wallet();
        wallet.setWalletSourceType(WalletSourceType.MASTER_PASS);
        wallet.setWalletSourceTypeId("123");
        sale.setWallet(wallet);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<sale.*?<secondaryAmount>10</secondaryAmount>.*?<applepay>.*?<data>user</data>.*?</applepay>.*?<wallet>.*?<walletSourceTypeId>123</walletSourceTypeId>.*?</wallet>.*?</sale>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><saleResponse><litleTxnId>123</litleTxnId><applepayResponse><applicationPrimaryAccountNumber>123455</applicationPrimaryAccountNumber></applepayResponse></saleResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        SaleResponse saleresponse = litle.sale(sale);
        assertEquals(123L, saleresponse.getLitleTxnId());
        assertEquals("123455", saleresponse.getApplepayResponse().getApplicationPrimaryAccountNumber());
    }

	@Test
	public void testToken() throws Exception {
		RegisterTokenRequestType token = new RegisterTokenRequestType();
		token.setOrderId("12344");
		token.setAccountNumber("1233456789103801");

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<registerTokenRequest.*?<accountNumber>1233456789103801</accountNumber>.*?</registerTokenRequest>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><registerTokenResponse><litleTxnId>123</litleTxnId></registerTokenResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		RegisterTokenResponse registertokenresponse = litle.registerToken(token);
		assertEquals(123L, registertokenresponse.getLitleTxnId());
	}
	
	@Test
    public void testTokenWithApplepay() throws Exception {
        RegisterTokenRequestType token = new RegisterTokenRequestType();
        token.setOrderId("12344");
        ApplepayType applepayType = new ApplepayType();
        ApplepayHeaderType applepayHeaderType = new ApplepayHeaderType();
        applepayHeaderType.setApplicationData("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setEphemeralPublicKey("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setPublicKeyHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
        applepayHeaderType.setTransactionId("1234");
        applepayType.setHeader(applepayHeaderType);
        applepayType.setData("user");
        applepayType.setSignature("sign");
        applepayType.setVersion("1");
        token.setApplepay(applepayType);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<registerTokenRequest.*?<applepay>.*?<data>user</data>.*?</applepay>.*?</registerTokenRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><registerTokenResponse><litleTxnId>123</litleTxnId><applepayResponse><applicationPrimaryAccountNumber>123455</applicationPrimaryAccountNumber></applepayResponse></registerTokenResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        RegisterTokenResponse registertokenresponse = litle.registerToken(token);
        assertEquals(123L, registertokenresponse.getLitleTxnId());
        assertEquals("123455", registertokenresponse.getApplepayResponse().getApplicationPrimaryAccountNumber());
    }

	@Test
	public void testLitleOnlineException() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='1' message='Error validating xml data against the schema' xmlns='http://www.litle.com/schema'><authorizationResponse><litleTxnId>123</litleTxnId></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		try{
			litle.authorize(authorization);
			fail("Expected Exception");
		} catch (LitleOnlineException e){
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}

	@Test
	public void testJAXBException() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"no xml");
		litle.setCommunication(mockedCommunication);
		try{
			litle.authorize(authorization);
			fail("Expected Exception");
		} catch(LitleOnlineException e){
			assertEquals("Error validating xml data against the schema", e.getMessage());
		}
	}

	@Test
	public void testDefaultReportGroup() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*? reportGroup=\"Default Report Group\">.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse reportGroup='Default Report Group'></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = litle.authorize(authorization);
		assertEquals("Default Report Group", authorize.getReportGroup());
	}

	@Test
	public void testOverrideLoggedInUser() throws Exception {
		Properties config = new Properties();
		config.setProperty("loggedInUser", "avig");
		litle = new LitleOnline(config);
		Authorization authorization = new Authorization();
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?loggedInUser=\"avig\".*?<authorization.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse reportGroup='Default Report Group'></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = litle.authorize(authorization);
		assertEquals("Default Report Group", authorize.getReportGroup());
	}

	@Test
	public void testEcheckVoid() throws Exception {
		EcheckVoid echeckvoid = new EcheckVoid();
		echeckvoid.setLitleTxnId(12345L);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<echeckVoid.*?<litleTxnId>12345</litleTxnId>.*?</echeckVoid>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><echeckVoidResponse><litleTxnId>123</litleTxnId></echeckVoidResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		EcheckVoidResponse echeckvoidresponse = litle.echeckVoid(echeckvoid);
		assertEquals(123L, echeckvoidresponse.getLitleTxnId());
	}

	@Test
	public void test_CustomerInfo_dob() throws Exception {
		Authorization authorization = new Authorization();
		authorization.setReportGroup("Planets");
		authorization.setOrderId("12344");
		authorization.setAmount(106L);
		authorization.setOrderSource(OrderSourceType.ECOMMERCE);
		CardType card = new CardType();
		card.setType(MethodOfPaymentTypeEnum.VI);
		card.setNumber("4100000000000002");
		card.setExpDate("1210");
		authorization.setCard(card);
		CustomerInfo customerInfo = new CustomerInfo();
		Calendar c = Calendar.getInstance();
		c.set(1980, Calendar.APRIL, 14);
		customerInfo.setDob(c);
		authorization.setCustomerInfo(customerInfo);

		Communication mockedCommunication = mock(Communication.class);
		when(
				mockedCommunication
						.requestToServer(
								matches(".*?<litleOnlineRequest.*?<authorization.*?<dob>1980-04-14</dob>.*?</authorization>.*?"),
								any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
				.thenReturn(
						"<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><authorizationResponse><litleTxnId>123</litleTxnId></authorizationResponse></litleOnlineResponse>");
		litle.setCommunication(mockedCommunication);
		AuthorizationResponse authorize = litle.authorize(authorization);
		assertEquals(123L, authorize.getLitleTxnId());
	}

	@Test(expected=LitleOnlineException.class)
	public void testSendToLitleNamespaceHotswap() {
	    Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                any(String.class),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version=\"1.0\" xmlns=\"http://www.litle.com/schema/online\" response=\"1\" message=\"System Error - Call Litle &amp; Co.\"></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);


        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1210");
        card.setType(MethodOfPaymentTypeEnum.VI);

        Sale sale = new Sale();
        sale.setReportGroup("Planets");
        sale.setOrderId("12344");
        sale.setAmount(6000L);
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        sale.setCard(card);

        litle.sale(sale);
	}

    @Test
    public void testCancelSubscription() throws Exception {
        CancelSubscription cancel = new CancelSubscription();
        cancel.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<cancelSubscription><subscriptionId>12345</subscriptionId></cancelSubscription></litleOnlineRequest>*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><cancelSubscriptionResponse><subscriptionId>12345</subscriptionId></cancelSubscriptionResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        CancelSubscriptionResponse cancelResponse = litle.cancelSubscription(cancel);
        assertEquals(12345L, cancelResponse.getSubscriptionId());
    }

    @Test
    public void testCancelSubscriptionWithOverrides() throws Exception {
        CancelSubscription cancel = new CancelSubscription();
        cancel.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"9001\".*?<cancelSubscription><subscriptionId>12345</subscriptionId></cancelSubscription></litleOnlineRequest>*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><cancelSubscriptionResponse><subscriptionId>12345</subscriptionId></cancelSubscriptionResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("9001");
        CancelSubscriptionResponse cancelResponse = litle.cancelSubscription(cancel, overrides);
        assertEquals(12345L, cancelResponse.getSubscriptionId());
    }

    @Test
    public void testUpdateSubscription() throws Exception {
        UpdateSubscription update = new UpdateSubscription();
        Calendar c = Calendar.getInstance();
        c.set(2013, Calendar.AUGUST, 7);
        update.setBillingDate(c);
        Contact billToAddress = new Contact();
        billToAddress.setName("Greg Dake");
        billToAddress.setCity("Lowell");
        billToAddress.setState("MA");
        billToAddress.setEmail("sdksupport@litle.com");
        update.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1215");
        card.setType(MethodOfPaymentTypeEnum.VI);
        update.setCard(card);
        update.setPlanCode("abcdefg");
        update.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<updateSubscription><subscriptionId>12345</subscriptionId><planCode>abcdefg</planCode><billToAddress><name>Greg Dake</name><city>Lowell</city><state>MA</state><email>sdksupport@litle.com</email></billToAddress><card><type>VI</type><number>4100000000000001</number><expDate>1215</expDate></card><billingDate>2013-08-07</billingDate></updateSubscription></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><updateSubscriptionResponse><subscriptionId>12345</subscriptionId></updateSubscriptionResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        UpdateSubscriptionResponse updateResponse = litle.updateSubscription(update);
        assertEquals(12345L, updateResponse.getSubscriptionId());
    }

    @Test
    public void testUpdateSubscriptionWithOverrides() throws Exception {
        UpdateSubscription update = new UpdateSubscription();
        Calendar c = Calendar.getInstance();
        c.set(2013, Calendar.AUGUST, 7);
        update.setBillingDate(c);
        Contact billToAddress = new Contact();
        billToAddress.setName("Greg Dake");
        billToAddress.setCity("Lowell");
        billToAddress.setState("MA");
        billToAddress.setEmail("sdksupport@litle.com");
        update.setBillToAddress(billToAddress);
        CardType card = new CardType();
        card.setNumber("4100000000000001");
        card.setExpDate("1215");
        card.setType(MethodOfPaymentTypeEnum.VI);
        update.setCard(card);
        update.setPlanCode("abcdefg");
        update.setSubscriptionId(12345L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<updateSubscription><subscriptionId>12345</subscriptionId><planCode>abcdefg</planCode><billToAddress><name>Greg Dake</name><city>Lowell</city><state>MA</state><email>sdksupport@litle.com</email></billToAddress><card><type>VI</type><number>4100000000000001</number><expDate>1215</expDate></card><billingDate>2013-08-07</billingDate></updateSubscription></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><updateSubscriptionResponse><subscriptionId>12345</subscriptionId></updateSubscriptionResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        UpdateSubscriptionResponse updateResponse = litle.updateSubscription(update, overrides);
        assertEquals(12345L, updateResponse.getSubscriptionId());
    }

    @Test
    public void testUpdatePlan() throws Exception {
        UpdatePlan update = new UpdatePlan();
        update.setActive(true);
        update.setPlanCode("abc");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<updatePlan><planCode>abc</planCode><active>true</active></updatePlan></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><updatePlanResponse><planCode>abc</planCode></updatePlanResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        UpdatePlanResponse updateResponse = litle.updatePlan(update);
        assertEquals("abc", updateResponse.getPlanCode());
    }

    @Test
    public void testUpdatePlanWithOverrides() throws Exception {
        UpdatePlan update = new UpdatePlan();
        update.setActive(true);
        update.setPlanCode("abc");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<updatePlan><planCode>abc</planCode><active>true</active></updatePlan></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.20' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><updatePlanResponse><planCode>abc</planCode></updatePlanResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        UpdatePlanResponse updateResponse = litle.updatePlan(update, overrides);
        assertEquals("abc", updateResponse.getPlanCode());
    }

    @Test
    public void testCreatePlan() throws Exception {
        CreatePlan create = new CreatePlan();
        create.setPlanCode("abc");
        create.setActive(true);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<createPlan><planCode>abc</planCode><active>true</active></createPlan></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><createPlanResponse><planCode>abc</planCode></createPlanResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        CreatePlanResponse createResponse = litle.createPlan(create);
        assertEquals("abc", createResponse.getPlanCode());
    }

    @Test
    public void testCreatePlanWithOverrides() throws Exception {
        CreatePlan create = new CreatePlan();
        create.setPlanCode("abc");
        create.setActive(true);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<createPlan><planCode>abc</planCode><active>true</active></createPlan></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><createPlanResponse><planCode>abc</planCode></createPlanResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        CreatePlanResponse createResponse = litle.createPlan(create, overrides);
        assertEquals("abc", createResponse.getPlanCode());
    }

    @Test
    public void testActivate() throws Exception {
        Activate activate = new Activate();
        activate.setAmount(100L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<activate><amount>100</amount></activate></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><activateResponse><litleTxnId>123456</litleTxnId></activateResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        ActivateResponse response = litle.activate(activate);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testActivateWithOverrides() throws Exception {
        Activate activate = new Activate();
        activate.setAmount(100L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<activate><amount>100</amount></activate></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><activateResponse><litleTxnId>123456</litleTxnId></activateResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        ActivateResponse response = litle.activate(activate, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testDectivate() throws Exception {
        Deactivate deactivate = new Deactivate();
        deactivate.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<deactivate><orderId>123</orderId></deactivate></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><deactivateResponse><litleTxnId>123456</litleTxnId></deactivateResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        DeactivateResponse response = litle.deactivate(deactivate);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testDeactivateWithOverrides() throws Exception {
        Deactivate deactivate = new Deactivate();
        deactivate.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<deactivate><orderId>123</orderId></deactivate></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><deactivateResponse><litleTxnId>123456</litleTxnId></deactivateResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        DeactivateResponse response = litle.deactivate(deactivate, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testLoad() throws Exception {
        Load load = new Load();
        load.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<load><orderId>123</orderId></load></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><loadResponse><litleTxnId>123456</litleTxnId></loadResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LoadResponse response = litle.load(load);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testLoadWithOverrides() throws Exception {
        Load load = new Load();
        load.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<load><orderId>123</orderId></load></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><loadResponse><litleTxnId>123456</litleTxnId></loadResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        LoadResponse response = litle.load(load, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testUnload() throws Exception {
        Unload unload = new Unload();
        unload.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<unload><orderId>123</orderId></unload></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><unloadResponse><litleTxnId>123456</litleTxnId></unloadResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        UnloadResponse response = litle.unload(unload);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testUnloadWithOverrides() throws Exception {
        Unload unload = new Unload();
        unload.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<unload><orderId>123</orderId></unload></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><unloadResponse><litleTxnId>123456</litleTxnId></unloadResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        UnloadResponse response = litle.unload(unload, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testBalanceInquiry() throws Exception {
        BalanceInquiry balanceInquiry = new BalanceInquiry();
        balanceInquiry.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<balanceInquiry><orderId>123</orderId></balanceInquiry></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><balanceInquiryResponse><litleTxnId>123456</litleTxnId></balanceInquiryResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        BalanceInquiryResponse response = litle.balanceInquiry(balanceInquiry);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testBalanceInquiryWithOverrides() throws Exception {
        BalanceInquiry balanceInquiry = new BalanceInquiry();
        balanceInquiry.setOrderId("123");

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<balanceInquiry><orderId>123</orderId></balanceInquiry></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><balanceInquiryResponse><litleTxnId>123456</litleTxnId></balanceInquiryResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        BalanceInquiryResponse response = litle.balanceInquiry(balanceInquiry, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testActivateReversal() throws Exception {
        ActivateReversal activateReversal = new ActivateReversal();
        activateReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<activateReversal><litleTxnId>123</litleTxnId></activateReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><activateReversalResponse><litleTxnId>123456</litleTxnId></activateReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        ActivateReversalResponse response = litle.activateReversal(activateReversal);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testActivateReversalWithOverrides() throws Exception {
        ActivateReversal activateReversal = new ActivateReversal();
        activateReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<activateReversal><litleTxnId>123</litleTxnId></activateReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><activateReversalResponse><litleTxnId>123456</litleTxnId></activateReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        ActivateReversalResponse response = litle.activateReversal(activateReversal, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testDeactivateReversal() throws Exception {
        DeactivateReversal deactivateReversal = new DeactivateReversal();
        deactivateReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<deactivateReversal><litleTxnId>123</litleTxnId></deactivateReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><deactivateReversalResponse><litleTxnId>123456</litleTxnId></deactivateReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        DeactivateReversalResponse response = litle.deactivateReversal(deactivateReversal);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testDeActivateReversalWithOverrides() throws Exception {
        DeactivateReversal deactivateReversal = new DeactivateReversal();
        deactivateReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<deactivateReversal><litleTxnId>123</litleTxnId></deactivateReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><deactivateReversalResponse><litleTxnId>123456</litleTxnId></deactivateReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        DeactivateReversalResponse response = litle.deactivateReversal(deactivateReversal, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testLoadReversal() throws Exception {
        LoadReversal loadReversal = new LoadReversal();
        loadReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<loadReversal><litleTxnId>123</litleTxnId></loadReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><loadReversalResponse><litleTxnId>123456</litleTxnId></loadReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LoadReversalResponse response = litle.loadReversal(loadReversal);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testLoadReversalWithOverrides() throws Exception {
        LoadReversal loadReversal = new LoadReversal();
        loadReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<loadReversal><litleTxnId>123</litleTxnId></loadReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><loadReversalResponse><litleTxnId>123456</litleTxnId></loadReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        LoadReversalResponse response = litle.loadReversal(loadReversal, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testUnLoadReversal() throws Exception {
        UnloadReversal unloadReversal = new UnloadReversal();
        unloadReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<unloadReversal><litleTxnId>123</litleTxnId></unloadReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><unloadReversalResponse><litleTxnId>123456</litleTxnId></unloadReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        UnloadReversalResponse response = litle.unloadReversal(unloadReversal);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testUnLoadReversalWithOverrides() throws Exception {
        UnloadReversal unloadReversal = new UnloadReversal();
        unloadReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<unloadReversal><litleTxnId>123</litleTxnId></unloadReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><unloadReversalResponse><litleTxnId>123456</litleTxnId></unloadReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        UnloadReversalResponse response = litle.unloadReversal(unloadReversal, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testRefundReversal() throws Exception {
        RefundReversal refundReversal = new RefundReversal();
        refundReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<refundReversal><litleTxnId>123</litleTxnId></refundReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><refundReversalResponse><litleTxnId>123456</litleTxnId></refundReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        RefundReversalResponse response = litle.refundReversal(refundReversal);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testRefundReversalWithOverrides() throws Exception {
        RefundReversal refundReversal = new RefundReversal();
        refundReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<refundReversal><litleTxnId>123</litleTxnId></refundReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><refundReversalResponse><litleTxnId>123456</litleTxnId></refundReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        RefundReversalResponse response = litle.refundReversal(refundReversal, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testDepositReversal() throws Exception {
        DepositReversal depositReversal = new DepositReversal();
        depositReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<depositReversal><litleTxnId>123</litleTxnId></depositReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><depositReversalResponse><litleTxnId>123456</litleTxnId></depositReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        DepositReversalResponse response = litle.depositReversal(depositReversal);
        assertEquals(123456L, response.getLitleTxnId());
    }

    @Test
    public void testDepositReversalWithOverrides() throws Exception {
        DepositReversal depositReversal = new DepositReversal();
        depositReversal.setLitleTxnId(123L);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?merchantId=\"905\".*?<depositReversal><litleTxnId>123</litleTxnId></depositReversal></litleOnlineRequest>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.21' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><depositReversalResponse><litleTxnId>123456</litleTxnId></depositReversalResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        LitleOnlineRequest overrides = new LitleOnlineRequest();
        overrides.setMerchantId("905");
        DepositReversalResponse response = litle.depositReversal(depositReversal, overrides);
        assertEquals(123456L, response.getLitleTxnId());
    }
    
    @Test
    public void testRecurring() throws Exception{
        Sale sale = new Sale();
        sale.setAmount(106L);
        sale.setLitleTxnId(123456L);
        sale.setOrderId("12344");
        sale.setOrderSource(OrderSourceType.ECOMMERCE);
        CardType card = new CardType();
        card.setType(MethodOfPaymentTypeEnum.VI);
        card.setNumber("4100000000000002");
        card.setExpDate("1210");
        sale.setCard(card);
        RecurringRequestType recuring = new RecurringRequestType();
        RecurringSubscriptionType sub = new RecurringSubscriptionType();
        sub.setPlanCode("12345");
        sub.setNumberOfPayments(12);
        sub.setStartDate(Calendar.getInstance());
        sub.setAmount(1000L);
        CreateAddOnType cat = new CreateAddOnType();
        cat.setAddOnCode("1234");
        cat.setAmount(500L);
        cat.setEndDate(Calendar.getInstance());
        cat.setName("name");
        cat.setEndDate(Calendar.getInstance());
        sub.getCreateAddOns().add(cat);
        recuring.setSubscription(sub);
        sale.setRecurringRequest(recuring);

        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<sale.*?<card>.*?<number>4100000000000002</number>.*?</card>.*?<createAddOn>.*?</createAddOn>.*?</sale>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='8.10' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><saleResponse><litleTxnId>123</litleTxnId></saleResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);
        SaleResponse saleresponse = litle.sale(sale);
        assertEquals(123L, saleresponse.getLitleTxnId());
    }
    
    @Test
    public void testQueryTransactionResponse_notFound() {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("1234");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("org1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        
        Communication mockedComm = mock (Communication.class);
        when(mockedComm.requestToServer(matches(".*?<litleOnlineRequest.*?<queryTransaction.*id=\"1234\".*?customerId=\"customerId\".*?<origId>org1</origId>.*?<origActionType>A</origActionType>.*?"),
                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class))).thenReturn("<litleOnlineResponse version='10.1' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><queryTransactionResponse id='1234' customerId='customerId'> <response>150</response> <responseTime>2015-04-14T12:37:26</responseTime> <message>Original transaction not found</message><matchCount>0</matchCount></queryTransactionResponse></litleOnlineResponse>");
        
        litle.setCommunication(mockedComm);
        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("1234", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
    }
    
    @Test
    public void testQueryTransactionResponse_transactionFound() {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("findId");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("org1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        
        Communication mockedComm = mock (Communication.class); 
        when(mockedComm.requestToServer(matches(".*?<litleOnlineRequest.*?<queryTransaction.*id=\"findId\".*?customerId=\"customerId\".*?<origId>org1</origId>.*?<origActionType>A</origActionType>.*?"),
                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class))).thenReturn("<litleOnlineResponse version='10.1' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><queryTransactionResponse id='findId' customerId='customerId'> <response>150</response> <responseTime>2015-04-14T12:37:26</responseTime> " +
                		"<message>Original transaction found</message><matchCount>1</matchCount>" +
                		"<results_max10> <authorizationResponse id=\"findId\" > <litleTxnId>1111111</litleTxnId> <orderId>150306_auth</orderId> <response>000</response><responseTime>2015-04-14T12:37:23</responseTime><postDate>2015-04-14</postDate><message>Approved</message></authorizationResponse></results_max10></queryTransactionResponse></litleOnlineResponse>");
        
        litle.setCommunication(mockedComm);
        TransactionTypeWithReportGroup response = litle.queryTransaction(queryTransaction);
        QueryTransactionResponse queryTransactionResponse = (QueryTransactionResponse)response;
        assertEquals("findId", queryTransactionResponse.getId());
        assertEquals("customerId", queryTransactionResponse.getCustomerId());
        assertEquals("150", queryTransactionResponse.getResponse());
        assertEquals(1, queryTransactionResponse.getResultsMax10().getTransactionResponses().size());
        JAXBElement<? extends TransactionTypeWithReportGroup> authorization = queryTransactionResponse.getResultsMax10().getTransactionResponses().get(0);
        AuthorizationResponse authResponse = (AuthorizationResponse)authorization.getValue();
        assertEquals(1111111L,authResponse.getLitleTxnId());
    }
    
    @Test
    public void testQueryTransactionResponse_unavailable() {
        QueryTransaction queryTransaction = new QueryTransaction();
        queryTransaction.setId("1234");
        queryTransaction.setCustomerId("customerId");
        queryTransaction.setOrigId("org1");
        queryTransaction.setOrigActionType(ActionTypeEnum.A);
        
        Communication mockedComm = mock (Communication.class);
        when(mockedComm.requestToServer(matches(".*?<litleOnlineRequest.*?<queryTransaction.*id=\"1234\".*?customerId=\"customerId\".*?<origId>org1</origId>.*?<origActionType>A</origActionType>.*?"),
                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class))).thenReturn("<litleOnlineResponse version='10.1' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><queryTransactionUnavailableResponse id='1234' customerId='customerId'><litleTxnId>123456</litleTxnId> <response>123</response> <message>Sample message</message></queryTransactionUnavailableResponse></litleOnlineResponse>");
        
        litle.setCommunication(mockedComm);
        TransactionTypeWithReportGroup response =litle.queryTransaction(queryTransaction);
        QueryTransactionUnavailableResponse unavailableResponse = (QueryTransactionUnavailableResponse)response;
        assertEquals("1234", unavailableResponse.getId());
        assertEquals(123456L,unavailableResponse.getLitleTxnId());
        assertEquals("Sample message", unavailableResponse.getMessage());
    }

    @Test
    public void testFraudCheck() throws Exception{
        FraudCheck fraudCheck = new FraudCheck();
        AdvancedFraudChecksType advancedFraudChecks = new AdvancedFraudChecksType();
        advancedFraudChecks.setThreatMetrixSessionId("123");
        advancedFraudChecks.setCustomAttribute1("pass");
        advancedFraudChecks.setCustomAttribute2("42");
        advancedFraudChecks.setCustomAttribute3("5");
        fraudCheck.setAdvancedFraudChecks(advancedFraudChecks);
        
        Communication mockedCommunication = mock(Communication.class);
        when(
                mockedCommunication
                        .requestToServer(
                                matches(".*?<litleOnlineRequest.*?<fraudCheck.*?<advancedFraudChecks>.*?</advancedFraudChecks>.*?</fraudCheck>.*?"),
                                any(Properties.class), any(BasicHttpContext.class), any(RequestConfig.class)))
                .thenReturn(
                        "<litleOnlineResponse version='10.1' response='0' message='Valid Format' xmlns='http://www.litle.com/schema'><fraudCheckResponse id='' reportGroup='Default Report Group' customerId=''><litleTxnId>602413782865196123</litleTxnId><response>123</response><responseTime>2016-07-11T15:12:34</responseTime><message>Call Discover</message><advancedFraudResults><deviceReviewStatus>pass</deviceReviewStatus><deviceReputationScore>42</deviceReputationScore><triggeredRule>triggered_rule_1</triggeredRule><triggeredRule>triggered_rule_2</triggeredRule><triggeredRule>triggered_rule_3</triggeredRule><triggeredRule>triggered_rule_4</triggeredRule><triggeredRule>triggered_rule_5</triggeredRule></advancedFraudResults></fraudCheckResponse></litleOnlineResponse>");
        litle.setCommunication(mockedCommunication);        
        
        FraudCheckResponse fraudCheckResponse = litle.fraudCheck(fraudCheck);
        AdvancedFraudResultsType advancedFraudResultsType = fraudCheckResponse.getAdvancedFraudResults();
        assertEquals("pass", advancedFraudResultsType.getDeviceReviewStatus());
        assertEquals(new Integer(42), advancedFraudResultsType.getDeviceReputationScore());
        assertEquals(5, advancedFraudResultsType.getTriggeredRules().size());
    }

}