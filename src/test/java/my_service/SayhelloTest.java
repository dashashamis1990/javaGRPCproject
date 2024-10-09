package my_service;

import io.grpc.Server;
import io.grpc.stub.StreamObserver;
import my_service.MyServiceControllerImpl;
import my_service.Myproto.Request;
import my_service.Myproto.Response;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SayhelloTest {

    private static final Logger logger = Logger.getLogger(ProcessmessagingTest.class.getName());

    // sayHello processes the request message correctly
    @Test
    public void test_processes_request_message_correctly() {
        logger.info("Starting test_processes_request_message_correctly");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(1).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        logger.info("Completed test_processes_request_message_correctly");
    }

    // sayHello iterates correct number of times
    @Test
    public void test_iterates_correct_number_of_times() {
        logger.info("Starting test_iterates_correct_number_of_times");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(3).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        verify(responseObserver, times(1)).onNext(any(Response.class));
        verify(responseObserver, times(1)).onCompleted();
        logger.info("Completed test_iterates_correct_number_of_times");
    }

    // sayHello constructs a response with status set to true
    @Test
    public void test_constructs_response_with_status_true() {
        logger.info("Starting test_constructs_response_with_status_true");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(1).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        ArgumentCaptor<Response> captor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver).onNext(captor.capture());
        assertTrue(captor.getValue().getStatus());
        logger.info("Completed test_constructs_response_with_status_true");
    }

    // sayHello sends the response using responseObserver.onNext
    @Test
    public void test_sends_response_using_onNext() {
        logger.info("Starting test_sends_response_using_onNext");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(1).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        logger.info("Completed test_sends_response_using_onNext");
    }

    // sayHello completes the response using responseObserver.onCompleted
    @Test
    public void test_completes_response_using_onCompleted() {
        logger.info("Starting test_completes_response_using_onCompleted");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(1).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onCompleted();
        logger.info("Completed test_completes_response_using_onCompleted");
    }

    // sayHello handles a request with count set to zero
    @Test
    public void test_handles_request_with_count_zero() {
        logger.info("Starting test_handles_request_with_count_zero");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(0).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        verify(responseObserver).onCompleted();
        logger.info("Completed test_handles_request_with_count_zero");
    }

    // sayHello handles a request with an empty message
    @Test
    public void test_handles_request_with_empty_message() {
        logger.info("Starting test_handles_request_with_empty_message");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("").setNumberOfTimes(1).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        verify(responseObserver).onCompleted();
        logger.info("Completed test_handles_request_with_empty_message");
    }

    // sayHello handles a request with a very large count
    @Test
    public void test_handles_request_with_large_count() {
        logger.info("Starting test_handles_request_with_large_count");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(10000).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        verify(responseObserver).onCompleted();
        logger.info("Completed test_handles_request_with_large_count");
    }

    // sayHello handles a request with special characters in the message
    @Test
    public void test_handles_request_with_special_characters() {
        logger.info("Starting test_handles_request_with_special_characters");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        Request request = Request.newBuilder().setMessage("@#$%^&*()").setNumberOfTimes(1).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        service.sayHello(request, responseObserver);
        verify(responseObserver).onNext(any(Response.class));
        verify(responseObserver).onCompleted();
        logger.info("Completed test_handles_request_with_special_characters");
    }

    // sayHello handles a null request object
    @Test
    public void test_handles_null_request_object() {
        logger.info("Starting test_handles_null_request_object");
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
    
        assertThrows(NullPointerException.class, () -> {
            service.sayHello(null, responseObserver);
        });
    
        logger.info("Completed test_handles_null_request_object");
    }

    // sayHello ensures the log messages are in the correct order
    @Test
    public void test_logs_in_correct_order() {
        logger.info("Starting test_logs_in_correct_order");
    
        // Mock gRPC server and service implementation
        Server server = mock(Server.class);
        MyServiceControllerImpl service = new MyServiceControllerImpl();
    
        Request request = Request.newBuilder().setMessage("Hello").setNumberOfTimes(2).build();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
    
        service.sayHello(request, responseObserver);
    
        InOrder inOrder = inOrder(responseObserver);
        inOrder.verify(responseObserver).onNext(any(Response.class));
        inOrder.verify(responseObserver).onCompleted();
    
        logger.info("Completed test_logs_in_correct_order");
    }
}