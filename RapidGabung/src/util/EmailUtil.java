package util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPMessage;

import entity.TrPelanggan;
import service.GenericService;

public class EmailUtil {

	public void setEmail(String strFrom, String strPassFrom, String strTo, String body, String strPDFPath,
			String nmAkun, String created) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		try {
			Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(strFrom, strPassFrom);
				}
			});

			// Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			Multipart multipart = new MimeMultipart();

			MimeBodyPart attachment1 = new MimeBodyPart();

			DataSource source = new FileDataSource(strPDFPath);
			attachment1.setDataHandler(new DataHandler(source));
			attachment1.setFileName(nmAkun.toUpperCase() + "-" + created + ".PDF");
			// attachment1.setHeader("Content-Type", "multipart/mixed");
			// attachment1.setHeader("Content-Transfer-Encoding", "base64");

			MimeBodyPart bodypart = new MimeBodyPart();
			bodypart.setText(body, "utf-8");

			multipart.addBodyPart(bodypart);
			multipart.addBodyPart(attachment1);

			message.setSubject("Rapid Express - Billing Tagihan " + "( " + nmAkun + " ) " + created);
			message.setContent(multipart);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(strTo));

			// Required magic (violates principle of least astonishment).
			message.saveChanges();

			Transport.send(message);
			System.out.println("--> kirim email ke : " + strTo + ", sukses!");
		} catch (MessagingException e) {
			e.printStackTrace();
			MessageBox.alert("Pelanggan : " + nmAkun + ", kesalahan pada saat pengiriman email : " + e.getMessage());
		}
	}

	public static void kirimManifestWithEmail(String username, String password, String toUser, String file,
			String path) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		try {
			Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			MimeMessage message = new MimeMessage(mailSession);
			Multipart multipart = new MimeMultipart();

			MimeBodyPart attachment1 = new MimeBodyPart();

			DataSource source = new FileDataSource(path);
			attachment1.setDataHandler(new DataHandler(source));
			attachment1.setFileName(file);
			// attachment1.setHeader("Content-Type", "multipart/mixed");
			// attachment1.setHeader("Content-Transfer-Encoding", "base64");

			MimeBodyPart bodypart = new MimeBodyPart();
			bodypart.setText("Berikut adalah data transaksi manifest cabang", "utf-8");

			multipart.addBodyPart(bodypart);
			multipart.addBodyPart(attachment1);

			message.setSubject("Manifest Cabang tanggal " + DateUtil.dateToStdDateLiteral(DateUtil.getNow()));
			message.setContent(multipart);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toUser));

			// Required magic (violates principle of least astonishment).
			message.saveChanges();

			Transport.send(message);
			System.out.println("--> kirim email ke : " + toUser + ", sukses!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public static void sendJadwalPickup(String emailBody, String judul, String email) {
		Map<String, Object> config = GenericService.getConfig();

		String username = config.get("email_username") == null ? "" : (String) config.get("email_username");
		String password = config.get("email_password") == null ? "" : (String) config.get("email_password");

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		try {
			Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			Multipart multipart = new MimeMultipart();

			MimeBodyPart bodypart = new MimeBodyPart();
			bodypart.setText(emailBody, "utf-8");

			multipart.addBodyPart(bodypart);

			message.setSubject(judul);
			message.setContent(multipart);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

			// Required magic (violates principle of least astonishment).
			message.saveChanges();

			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendNewsLetter(String emailBody, String judul, String email) {
		Map<String, Object> config = GenericService.getConfig();

		String username = config.get("email_username") == null ? "" : (String) config.get("email_username");
		String password = config.get("email_password") == null ? "" : (String) config.get("email_password");

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		try {
			Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			MimeMessage message = new MimeMessage(mailSession);
			Multipart multipart = new MimeMultipart();

			MimeBodyPart bodypart = new MimeBodyPart();
			bodypart.setText(emailBody, "utf-8");

			multipart.addBodyPart(bodypart);

			message.setSubject(judul);
			message.setContent(multipart);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

			message.saveChanges();

			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendEmailPenandaLunas(
			String username, 
			String password, 
			String subject, 
			String bodyEmail,
			TrPelanggan pel, 
			String path) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		try {
			Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			// Transport transport = mailSession.getTransport();

			MimeMessage message = new MimeMessage(mailSession);
			Multipart multipart = new MimeMultipart();

			MimeBodyPart attachment1 = new MimeBodyPart();

			DataSource source = new FileDataSource(path);
			attachment1.setDataHandler(new DataHandler(source));
			attachment1.setFileName(pel.getNamaAkun()+"-piutang.pdf");

			MimeBodyPart bodypart = new MimeBodyPart();
			bodypart.setText(bodyEmail, "utf-8");

			multipart.addBodyPart(bodypart);
			multipart.addBodyPart(attachment1);

			message.setSubject(subject);
			message.setContent(multipart);
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(pel.getEmail()));
//			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("puryanto.mis@gmail.com"));

			// Required magic (violates principle of least astonishment).
			message.saveChanges();

			Transport.send(message);
			System.out.println("--> kirim email ke : " + pel.getEmail() + ", sukses!");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageBox.alert("Pelanggan : " + pel.getEmail() + ", kesalahan pada saat pengiriman email : " + e.getMessage());
		}
		
	}
	
	public static void sendNewsLetter(String emailBody, String judul, String email, String pathFile) {
		Map<String, Object> config = GenericService.getConfig();

		String username = config.get("email_username") == null ? "" : (String) config.get("email_username");
		String password = config.get("email_password") == null ? "" : (String) config.get("email_password");

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session mailSession;
		try {
			mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});

			SMTPMessage m = new SMTPMessage(mailSession);
			MimeMultipart content = new MimeMultipart("related");

			String cid = ContentIdGenerator.getContentId();

			// body
			MimeBodyPart textPart = new MimeBodyPart();
			if (pathFile != null && !pathFile.trim().equals("")) {
				textPart.setText("<html><head><title>Title</title></head>\n<body><div><img src=\"cid:" + cid
						+ "\" /></div>\n<div>" + emailBody + "</div>\n" + "</body></html>", "US-ASCII", "html");
			} else {
				textPart.setText("<html><head>" + "<title>Title</title>" + "</head>\n" + "<body><div>" + emailBody
						+ "</div></body></html>", "US-ASCII", "html");
			}
			content.addBodyPart(textPart);

			// image
			MimeBodyPart imagePart = new MimeBodyPart();
			if (pathFile != null && !pathFile.trim().equals("")) {
				imagePart.attachFile(pathFile);
				imagePart.setContentID("<" + cid + ">");
				imagePart.setDisposition(MimeBodyPart.INLINE);
				content.addBodyPart(imagePart);
			}

			m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			m.setContent(content);
			m.setSubject(judul);

			Transport.send(m);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
