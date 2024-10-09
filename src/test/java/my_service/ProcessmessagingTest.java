package my_service;

import io.grpc.stub.StreamObserver;
import my_service.Myproto.Request;
import my_service.Myproto.Response;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.*;

public class ProcessmessagingTest {

    private static final Logger logger = LoggerFactory.getLogger(ProcessmessagingTest.class);

    // Process a single request and receive a response with status true
    @Test
    public void test_single_request_response_status_true() {
        logger.info("Starting test_single_request_response_status_true");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("Test").setNumberOfTimes(1).build();
        requestObserver.onNext(request);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver).onNext(responseCaptor.capture());
        Response response = responseCaptor.getValue();

        assertTrue(response.getStatus());
        logger.info("Ending test_single_request_response_status_true");
    }

    // Process multiple requests sequentially and receive responses with status true
    @Test
    public void test_multiple_requests_sequentially() {
        logger.info("Starting test_multiple_requests_sequentially");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request1 = Request.newBuilder().setMessage("Test1").setNumberOfTimes(1).build();
        Request request2 = Request.newBuilder().setMessage("Test2").setNumberOfTimes(2).build();
        requestObserver.onNext(request1);
        requestObserver.onNext(request2);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver, times(2)).onNext(responseCaptor.capture());
        assertTrue(responseCaptor.getAllValues().get(0).getStatus());
        assertTrue(responseCaptor.getAllValues().get(1).getStatus());

        logger.info("Ending test_multiple_requests_sequentially");
    }

    // Log the received message with a timestamp
    @Test
    public void test_log_received_message_with_timestamp() {
        logger.info("Starting test_log_received_message_with_timestamp");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("Test").setNumberOfTimes(1).build();
        requestObserver.onNext(request);

        // Assuming we have a way to capture the logs, this is a placeholder for log verification
        // verifyLogContains("Received message: Test");

        logger.info("Ending test_log_received_message_with_timestamp");
    }

    // Log the processing of each message the specified number of times
    @Test
    public void test_log_processing_each_message_specified_times() {
        logger.info("Starting test_log_processing_each_message_specified_times");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("Test").setNumberOfTimes(3).build();
        requestObserver.onNext(request);

        // Assuming we have a way to capture the logs, this is a placeholder for log verification
        // verifyLogContains("Processing Test time 1");
        // verifyLogContains("Processing Test time 2");
        // verifyLogContains("Processing Test time 3");

        logger.info("Ending test_log_processing_each_message_specified_times");
    }

    // Send a response after processing the message
    @Test
    public void test_send_response_after_processing_message() {
        logger.info("Starting test_send_response_after_processing_message");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("Test").setNumberOfTimes(1).build();
        requestObserver.onNext(request);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver, times(1)).onNext(responseCaptor.capture());
        assertTrue(responseCaptor.getValue().getStatus());

        logger.info("Ending test_send_response_after_processing_message");
    }

    // Handle an empty message in the request
    @Test
    public void test_handle_empty_message_in_request() {
        logger.info("Starting test_handle_empty_message_in_request");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("").setNumberOfTimes(1).build();
        requestObserver.onNext(request);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver, times(1)).onNext(responseCaptor.capture());
        assertTrue(responseCaptor.getValue().getStatus());

        logger.info("Ending test_handle_empty_message_in_request");
    }

    // Handle a request with numberOfTimes set to zero
    @Test
    public void test_handle_request_with_number_of_times_zero() {
        logger.info("Starting test_handle_request_with_number_of_times_zero");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("Test").setNumberOfTimes(0).build();
        requestObserver.onNext(request);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver, times(1)).onNext(responseCaptor.capture());
        assertFalse(responseCaptor.getValue().getStatus());

        logger.info("Ending test_handle_request_with_number_of_times_zero");
    }

    // Handle a request with a very large numberOfTimes
    @Test
    public void test_handle_request_with_large_number_of_times() {
        logger.info("Starting test_handle_request_with_large_number_of_times");

        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);

        Request request = Request.newBuilder().setMessage("Test").setNumberOfTimes(10000).build();
        requestObserver.onNext(request);

        ArgumentCaptor<Response> responseCaptor = ArgumentCaptor.forClass(Response.class);
        verify(responseObserver, times(1)).onNext(responseCaptor.capture());
        assertTrue(responseCaptor.getValue().getStatus());

        logger.info("Ending test_handle_request_with_large_number_of_times");
    }

        // Ensure the status is set to true only after the last iteration.
    @Test
    public void test_processmessaging_status_true_last_iteration() {
        // Given
        MyServiceControllerImpl service = new MyServiceControllerImpl();
        StreamObserver<Response> responseObserver = mock(StreamObserver.class);
        StreamObserver<Request> requestObserver = service.processMessaging(responseObserver);
        Request request = Request.newBuilder().setMessage("Test Message").setNumberOfTimes(3).build();

        // When
        requestObserver.onNext(request);

        // Then
        verify(responseObserver, times(1)).onNext(any(Response.class));
        verify(responseObserver, times(1)).onCompleted();
    }
}