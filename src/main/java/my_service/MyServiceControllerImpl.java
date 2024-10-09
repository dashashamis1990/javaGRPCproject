package my_service;

import io.grpc.stub.StreamObserver;
import my_service.Myproto.Request;
import my_service.Myproto.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class MyServiceControllerImpl extends MyServiceControllerGrpc.MyServiceControllerImplBase {

    private boolean status = false;

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
                    if (i == request.getNumberOfTimes() - 1) {
                        status = true;
                    }
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                now = LocalDateTime.now().format(dtf);
                System.out.println(now + " Sending response");
                Response response = Response.newBuilder().setStatus(status).build();
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
