package com.scenario.common;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

public class FaceWallet extends SetUp {
	@FindBy(xpath = "//h2[text()='Coin Transaction']")
	private WebElement CoinTransaction;
	
	
	public FaceWallet() {
		PageFactory.initElements(driver, this);
	}
	
	@Test
	public void CASE_01_네트워크_선택() throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException, CipherException {
		
		// Mumbai 네트워크 선택
		WebElement network = driver.findElement(By.xpath("//div[text() = 'Mumbai']"));
		network.click();
		
		// Connected 문구 노출되는지 확인
		WebElement success = driver.findElement(By.xpath("//div[text() = 'Connected']"));
		Assert.assertNotNull(success);
				
	}
	@Test
	public void CASE_02_소셜로그인() {
		
		//Log in with Face wallet  버튼 클릭
		WebElement login = driver.findElement(By.xpath("//div[text() = 'Log in with Face wallet']"));
		login.click();
		
		//Frame 전환 후 Continue with Google 버튼 클릭
		WebElement iframe = driver.findElement(By.xpath("//iframe"));
		driver.switchTo().frame(iframe);
		WebElement googleLogin = driver.findElement(By.xpath("//div[text() = 'Continue with Google']"));
		googleLogin.click();
		
		// google 창 이동
		this.switchToWindow();
		
		// 아이디 입력
		WebElement id = driver.findElement(By.xpath("//input[@type=\"email\"]"));
		id.sendKeys("ethanjin2020");
		this.nextBtn().click();
		
		// 자동화로 구글 비밀번호 입력 (보안상 문제로 비번 입력 불가)
//		WebElement pw = driver.findElement(By.xpath("//input[@type=\"password\"]"));
//		pw.sendKeys("");
		

	}
	
	// amounts() 메서드에서 관리하는 코인수 데이터를 불러와서 반복 전송
	//@Test(description = "코인 전송", dataProvider = "amounts")
	public void CASE_03_Coin_전송(String amount) {
		
		// 전송할 코인 갯수 입력
		WebElement amountBox = CoinTransaction.findElement(By.xpath("input[1]"));
		amountBox.sendKeys(amount);
		
		// transfer 버튼 클릭
		WebElement transferBtn = CoinTransaction.findElement(By.xpath("div[@class='button']"));
		transferBtn.click();
		
		// iframe 전환
		WebElement iframe = driver.findElement(By.xpath("//iframe"));
		driver.switchTo().frame(iframe);
		
		// Send Now 버튼 클릭
		WebElement Send = driver.findElement(By.xpath("//div[text()='Send Now']"));
		Send.click();
		
		// 전송 성공 메시지 확인
		WebElement Success = driver.findElement(By.xpath("//div[text()='Sent successfully!']"));
		Assert.assertNotNull(Success);
		
		// Explorer Link 확인
		WebElement ExplorerLink = driver.findElement(By.xpath("//a[text()='Explorer Link']"));
		Assert.assertNotNull(ExplorerLink);
		
	}
	// CASE_03_Coin_전송 메서드에 사용할 데이터
	@DataProvider(name = "amounts")
	public Object [][] amounts() {
		return new Object [][] {
			{ "0.001"}, 
			{ "-1"}, 
			{ "0"}, 
			{ "9999999"}, 
		};
	}
	//@Test
	public void CASE_04_메시지_서명() throws SignatureException {
		// 서명 입력 박스에 메시지 입력
		String originalMessage = "서명 메시지입니다. ";
		WebElement messageBox = driver.findElement(By.xpath("//textarea"));
		messageBox.sendKeys(originalMessage);
		
		// Sign Message 버튼 클릭
		WebElement signMessage = driver.findElement(By.xpath("//div[text()='Sign Message']"));
		signMessage.click();
		
		WebElement iframe = driver.findElement(By.xpath("//iframe"));
		driver.switchTo().frame(iframe);
		
		// Sign 버튼 클릭
		WebElement sign = driver.findElement(By.xpath("//div[text()='Sign']"));
		sign.click();
		
		// 서명 성공 문구 확인
		WebElement success = driver.findElement(By.xpath("//div[text()='Signature success!']"));
		assertNotNull(success);
		
		// 닫기 클릭
		WebElement close = driver.findElement(By.xpath("//button[contains(@class,'closeButton')]"));
		close.click();
		
		// 서명된 메시지 확인
		WebElement signedMsgBtn = driver.findElement(By.xpath("//h4[text()='Signed message']/following-sibling::div"));
		String signedHash = signedMsgBtn.getText();
		
		// 메시지 서명 검증
		WebElement myAddress = driver.findElement(By.xpath("//div[@class='AccountInformation']/descendant::div[contains(text(),'Address:')]"));
		String address = myAddress.getText().toLowerCase();
		
		String r = signedHash.substring(0, 66);
		String s = "0x" + signedHash.substring(66, 130);
		String v = "0x" + signedHash.substring(130, 132);

		String pubkey = Sign.signedPrefixedMessageToKey(originalMessage.getBytes(), new Sign.SignatureData(
				Numeric.hexStringToByteArray(v)[0], Numeric.hexStringToByteArray(r), Numeric.hexStringToByteArray(s)))
				.toString(16);
		
		String decryptedAddress = "0x" + Keys.getAddress(pubkey);
		System.out.println("Address :          " + address);
		System.out.println("decryptedAddress : " + decryptedAddress);
		assertEquals(decryptedAddress, address);
	
		
	}
	
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		

	
	WebElement nextBtn () {
		return driver.findElement(By.xpath("//span[text()='다음']"));
	}
	
	public void switchToWindow() {
		Set<String> windows = driver.getWindowHandles(); 
		for (String window : windows) { 
		driver.switchTo().window(window); 
		}
	}

}
