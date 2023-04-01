package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.io.*;

import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class EmailTest {

	private static final String[] TEST_EMAILS = {"ab@bc.com", "ab@c.org", "abcdefghijklmnopqrst@abcdef.com"};
	private EmailConcrete email;
	
	
	//Set up a test ConcreteEmail object to run the tests with
	@Before
	public void setUpEmailTest() throws Exception{	
		email = new EmailConcrete();
	}
	
	
	//No resources need to be released
	@After
	public void tearDownEmailTest() throws Exception{
		
	}
	
	//-----------------------------------Tests the addBcc method-----------------------------------
	
	//Sends a list of email addresses to the addBcc method and makes sure that the Email class successfully adds them
	//to its BCC list
	@Test
	public void testAddBcc() throws Exception{
		email.addBcc(TEST_EMAILS);
		assertEquals(3, email.getBccAddresses().size());
		assertEquals(TEST_EMAILS[0], email.getBccAddresses().get(0).toString());
		assertEquals(TEST_EMAILS[1], email.getBccAddresses().get(1).toString());
		assertEquals(TEST_EMAILS[2], email.getBccAddresses().get(2).toString());
	}
	
	
	//-----------------------------------Tests the addCc method-----------------------------------

	//Sends an email address to the addCc method and makes sure that the correct email address
	//is added to the email object's CC list
	@Test
	public void testAddCc() throws Exception{
		email.addCc(TEST_EMAILS[0]);
		assertEquals(TEST_EMAILS[0], email.getCcAddresses().get(0).toString());
	}
	
	
	//-----------------------------------Tests the addHeader method-----------------------------------

	//Makes sure that the addHeader method returns an IllegalArgumentException
	//if the method is called with an empty value
	@Test
	public void testAddHeaderEmptyValue() throws Exception{
		try {
		email.addHeader("Name", "");
		assertEquals(0,1);
		}
		catch(IllegalArgumentException e) {
			assertEquals(1,1);
		}
		catch(Exception e) {
			assertEquals(1,1);
		}
		
	}
	
	//Makes sure that the addHeader method returns an IllegalArgumentException
	//if the method is called with an empty name/key
	@Test
	public void testAddHeaderEmptyName() throws Exception{
		try {
		email.addHeader("", "Value");
		assertEquals(0,1);
		}
		catch(IllegalArgumentException e) {
			assertEquals(1,1);
		}
		catch(Exception e) {
			assertEquals(1,1);
		}
		
	}
	
	
	//Makes sure that the addHeader method successfully adds the another entry
	//to the email Header map with the appropriate key and value
	@Test 
	public void testAddHeader() throws Exception{
		
		try {
			email.addHeader("Name", "Value");
			Map<String, String> headerMap = email.getHeaders();
			boolean containsValue = headerMap.containsKey("Name");
			if (containsValue) {
				assertEquals("Value", headerMap.get("Name").toString());
			}
			else {
				assertEquals(0,1);
			}
		}
		catch(Exception e) {
			assertEquals(0,1);
		}
		
	}
	
	
	//-----------------------------------Tests the addReplyTo method-----------------------------------

	//Tests that the addReplyTo method successfully adds the correct email and personal name
	//to the email's replyToAddresses list
	@Test
	public void testAddReplyTo() throws Exception{
		email.addReplyTo(TEST_EMAILS[0], "Joseph");
		assertEquals(1, email.getReplyToAddresses().size());
		assertEquals(TEST_EMAILS[0], email.getReplyToAddresses().get(0).getAddress());
		assertEquals("Joseph", email.getReplyToAddresses().get(0).getPersonal());
	}
	
	
	//-----------------------------------Tests the buildMimeMessage method-----------------------------------
	
	//checks that an exception with the message "From address required" is sent
	//if a mime message build is attempted with no "from" address set
	@Test
	public void testBuildMimeMessageNoFromAddress() throws Exception{
		try {
		email.setHostName("Host");
		email.buildMimeMessage();
		}
		
		catch (Exception e) {
			assertEquals("From address required", e.getMessage());
		}
	}
	
	//checks that an exception with the message "At least one receiver address required" is sent
	//if a mime message build is attempted with no "receiver" address set
	@Test
	public void testBuildMimeMessageNoRecieverAddress() throws Exception{
		try {
		email.setHostName("Host");
		email.setFrom(TEST_EMAILS[0]);
		email.buildMimeMessage();
		}
		catch (Exception e) {
			assertEquals("At least one receiver address required", e.getMessage());
		}
	}
	
	//Makes sure that the correct recipient and sender is added to the mime message
	@Test
	public void testBuildMimeMessageReciever() throws Exception{
		try {
		email.setHostName("Host");
		email.setFrom(TEST_EMAILS[0]);
		email.addTo(TEST_EMAILS[1]);
		email.buildMimeMessage();
		
		assertEquals(TEST_EMAILS[0], email.getMimeMessage().getFrom()[0].toString());
		assertEquals(TEST_EMAILS[1], email.getMimeMessage().getRecipients(RecipientType.TO)[0].toString());

		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	//Makes sure that the correct recipients, both directly to and CC'd, and sender is added to the mime message
	@Test
	public void testBuildMimeMessageRecieverCC() throws Exception{
		try {
		email.setHostName("Host");
		email.setFrom(TEST_EMAILS[0]);
		email.addTo(TEST_EMAILS[1]);
		email.addCc(TEST_EMAILS[2]);
		email.buildMimeMessage();
		
		
		assertEquals(TEST_EMAILS[0], email.getMimeMessage().getFrom()[0].toString());
		assertEquals(TEST_EMAILS[1], email.getMimeMessage().getRecipients(RecipientType.TO)[0].toString());
		assertEquals(TEST_EMAILS[2], email.getMimeMessage().getRecipients(RecipientType.CC)[0].toString());

		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	//Makes sure that the correct recipients, both directly to and BCC'd, and sender is added to the mime message
	@Test
	public void testBuildMimeMessageRecieverBcc() throws Exception{
		try {
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.addBcc(TEST_EMAILS[2]);
			email.buildMimeMessage();
			
			
			assertEquals(TEST_EMAILS[0], email.getMimeMessage().getFrom()[0].toString());
			assertEquals(TEST_EMAILS[1], email.getMimeMessage().getRecipients(RecipientType.TO)[0].toString());
			assertEquals(TEST_EMAILS[2], email.getMimeMessage().getRecipients(RecipientType.BCC)[0].toString());
		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	//Makes sure that the correct content is added to the mime message when content is marked as a String
	@Test
	public void testBuildMimeMessageSetContent() throws Exception{
		try {
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.setContent("Hello World", "");
			email.buildMimeMessage();
			
			
			assertEquals(TEST_EMAILS[0], email.getMimeMessage().getFrom()[0].toString());
			assertEquals(TEST_EMAILS[1], email.getMimeMessage().getRecipients(RecipientType.TO)[0].toString());
			assertEquals("Hello World", email.getMimeMessage().getContent().toString());
		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	
	//Makes sure that the correct content is added to the mime message when content is marked as a "text/plain"
	//and the correct charset (us-ascii) is appended to the DataHandler content type
	@Test
	public void testBuildMimeMessageSetContentTextPlain() throws Exception{
		try {
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.setContent("Hello World", "text/plain");
			email.buildMimeMessage();
			
			System.out.println(email.getMimeMessage().getDataHandler().getContentType());
			assertEquals(TEST_EMAILS[0], email.getMimeMessage().getFrom()[0].toString());
			assertEquals(TEST_EMAILS[1], email.getMimeMessage().getRecipients(RecipientType.TO)[0].toString());
			assertEquals(true, email.getMimeMessage().isMimeType("text/plain"));
			assertEquals("text/plain; charset=us-ascii", email.getMimeMessage().getDataHandler().getContentType());

		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	//Makes sure that the correct subject is added to the message with a plain String
	@Test
	public void testBuildMimeMessageSetSubject() throws Exception{
		try {
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.setSubject("Subject");
			email.buildMimeMessage();
			
			
			assertEquals(TEST_EMAILS[0], email.getMimeMessage().getFrom()[0].toString());
			assertEquals(TEST_EMAILS[1], email.getMimeMessage().getRecipients(RecipientType.TO)[0].toString());
			assertEquals("Subject", email.getMimeMessage().getSubject());
		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	//Checks that the correct email address is added to the message from the replyto list
	@Test
	public void testBuildMimeMessageSetReplyTo() throws Exception{
		try {
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.addReplyTo(TEST_EMAILS[2]);
			email.buildMimeMessage();
			
			
			assertEquals(TEST_EMAILS[2], email.getMimeMessage().getReplyTo()[0].toString());
		}
		catch (Exception e) {
			assertEquals(0,1);
		}
	}
	
	//Checks that the correct content is added to the message when using a MimeMultipart to set content
	@Test
	public void testBuildMimeMessageSetEmailBody() throws Exception{
		try {
			BodyPart body = new MimeBodyPart();
			body.setContent("Hello World", "text/plain");
			MimeMultipart email_body = new MimeMultipart();
			email_body.addBodyPart(body);
			
			
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.setContent(email_body);
			email.buildMimeMessage();
						
			
			MimeMultipart result = (MimeMultipart) email.getMimeMessage().getContent();
			BodyPart result_body = result.getBodyPart(0);
						
			assertEquals("Hello World", result_body.getContent());
		}
		catch (Exception e) {
			PrintWriter writer = new PrintWriter(System.out);
			e.printStackTrace(writer);
			writer.flush();
			assertEquals(0,1);
		}
	}
	
	
	//Checks that an exception with the message "The MimeMessage is already built" is thrown when
	//two mime message builds are attempted
	@Test
	public void testBuildMimeMessageSecondBuild() throws Exception{
		try {
			MimeMultipart email_body = new MimeMultipart();
			
			email.setHostName("Host");
			email.setFrom(TEST_EMAILS[0]);
			email.addTo(TEST_EMAILS[1]);
			email.buildMimeMessage();
			email.buildMimeMessage();
			
			
			assertEquals(0,1);
		}
		catch (Exception e) {
			assertEquals("The MimeMessage is already built.", e.getMessage());
		}
	}
	
	

	//-----------------------------------Tests the getHostName method-----------------------------------

	//Makes sure the host name starts as null when it hasn't been set
	@Test
	public void testGetHostName() throws Exception{
		assertEquals(null, email.getHostName());
	}

	//Makes sure that when the hostname has been set, the correct value is retrieved
	@Test
	public void testGetHostNameSetName() throws Exception{
		email.setHostName("hostName");
		assertEquals("hostName", email.getHostName());
	}
	
	
	//-----------------------------------Tests the getMailSession method-----------------------------------

	//makes sure that an exception with the message "Cannot find valid hostname for mail session" is
	//thrown when no hostname is set for the mail session
	@Test
	public void testGetMailSession() throws Exception{
		try {
		email.getMailSession();
		assertEquals(0,1);
		}
		catch(EmailException e) {
			assertEquals("Cannot find valid hostname for mail session",e.getMessage());	
		}
		
	}
	
	//checks that the correct information is created and retrieved if a host name name is set
	//for getMailSession
	@Test
	public void testGetMailSessionSetHostname() throws Exception{
		try {
		
		String send_partial = String.valueOf(email.isSendPartial());
		String socket_connection_timeout = Integer.toString(email.getSocketConnectionTimeout());
		String socket_timeout = Integer.toString(email.getSocketTimeout());
		
		email.setHostName("Host");
		
		Session session= email.getMailSession();
		Properties session_props = session.getProperties();	 
		
		assertEquals("Host", session_props.get("mail.smtp.host"));
		assertEquals(send_partial, session_props.get("mail.smtp.sendpartial"));
		assertEquals(send_partial, session_props.get("mail.smtps.sendpartial"));
		assertEquals(socket_timeout, session_props.get("mail.smtp.timeout"));
		assertEquals(socket_connection_timeout, session_props.get("mail.smtp.connectiontimeout"));

		}
		catch(EmailException e) {
			assertEquals(0,1);	
		}
		
	}
	
	//-----------------------------------Tests the getSentDate method-----------------------------------

	//makes sure that the default sent date is the current date
	@Test
	public void testGetSentDateNull() throws Exception{
		assertEquals(new Date(), email.getSentDate());
	}
	
	//makes sure that if the sent date is set, the correct value is retrieved
	@Test
	public void testGetSentDate() throws Exception{
		Date date = new Date(1000);
		email.setSentDate(date);
		assertEquals(date, email.getSentDate());
	}
	
	//-----------------------------------Tests the getSocketConnectionTimeout method-----------------------------------

	//makes sure that the correct value is retrieved with getSocketConnectionTimeout is called
	@Test
	public void testGetSocketConnectionTimeout() throws Exception{
		email.setSocketConnectionTimeout(10);
		assertEquals(10, email.getSocketConnectionTimeout());
	}
	
	
	//-----------------------------------Tests the setFrom method-----------------------------------

	//makes sure that the correct value is recorded when setFrom is called
	@Test
	public void testSetFrom() throws Exception{
		email.setFrom(TEST_EMAILS[0]);
		assertEquals(TEST_EMAILS[0], email.getFromAddress().getAddress());
	}
	
	
}
