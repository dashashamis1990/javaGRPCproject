package my_service;

import io.grpc.stub.StreamObserver;
import my_service.Myproto.Request;
import my_service.Myproto.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MyServiceControllerImpl extends MyServiceControllerGrpc.MyServiceControllerImplBase {

    @Override
    public void sayHello(Request request, StreamObserver<Response> responseObserver) {
        System.out.println("Received single message");
        String message = request.getMessage();
        int count = request.getNumberOfTimes();
        for (int i = 0; i < count; i++) {
            System.out.println(message + " - " + (i + 1));
        }
        Response response = Response.newBuilder().setStatus(true).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Request> processMessaging(StreamObserver<Response> responseObserver) {
        return new StreamObserver<Request>() {
            @Override
            public void onNext(Request request) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
                String now = LocalDateTime.now().format(dtf);
                System.out.println(now + " Received message: " + request.getMessage());
                for (int i = 0; i < request.getNumberOfTimes(); i++) {
                    System.out.println("Processing " + request.getMessage() + " time " + (i + 1));
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // Email sending logic
                String to = "recipient@example.com"; // recipient email
                String from = "sender@example.com"; // sender email
                String host = "smtp.example.com"; // mail server host

                Properties properties = System.getProperties();
                properties.setProperty("mail.smtp.host", host);

                Session session = Session.getDefaultInstance(properties);

                try {
                    MimeMessage message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(from));
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message.setSubject("Notification");
                    message.setText("Message received: " + request.getMessage());

                    Transport.send(message);
                    System.out.println("Email sent successfully.");
                } catch (MessagingException mex) {
                    mex.printStackTrace();
                }

                now = LocalDateTime.now().format(dtf);
                System.out.println(now + " Sending response");
                Response response = Response.newBuilder().setStatus(true).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

}

