package my_service;

import io.grpc.stub.StreamObserver;
import my_service.Myproto.Request;
import my_service.Myproto.Response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;


public class MyServiceControllerImpl extends MyServiceControllerGrpc.MyServiceControllerImplBase {

    private static final Logger logger = Logger.getLogger(MyServiceControllerImpl.class.getName());

    @Override
    public void sayHello(Request request, StreamObserver<Response> responseObserver) {
        logger.log(Level.INFO, "Received single message");
        String message = request.getMessage();
        int count = request.getNumberOfTimes();
        for (int i = 0; i < count; i++) {
            logger.log(Level.INFO, "{0} - {1}", new Object[]{message, (i + 1)});
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
                logger.log(Level.INFO, "{0} Received message: {1}", new Object[]{now, request.getMessage()});
                for (int i = 0; i < request.getNumberOfTimes(); i++) {
                    logger.log(Level.INFO, "Processing {0} time {1}", new Object[]{request.getMessage(), (i + 1)});
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                now = LocalDateTime.now().format(dtf);
                logger.log(Level.INFO, "{0} Sending response", now);
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

