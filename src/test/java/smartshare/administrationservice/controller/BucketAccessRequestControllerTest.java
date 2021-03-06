package smartshare.administrationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import smartshare.administrationservice.dto.BucketAccessRequestFromUi;
import smartshare.administrationservice.dto.Status;
import smartshare.administrationservice.dto.response.BucketAccessRequestDto;
import smartshare.administrationservice.models.BucketAccessRequestEntity;
import smartshare.administrationservice.service.BucketAccessRequestService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BucketAccessRequestControllerTest {

    @MockBean
    private BucketAccessRequestService bucketAccessRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET  /buckets/accessRequests - SUCCESS")
    void getBucketAccessRequestForAdmin() throws Exception {

        // set up the mock service
        BucketAccessRequestDto bucketAccessRequest1 = new BucketAccessRequestDto();
        BucketAccessRequestDto bucketAccessRequest2 = new BucketAccessRequestDto();
        BucketAccessRequestDto bucketAccessRequest3 = new BucketAccessRequestDto();
        List<BucketAccessRequestDto> bucketAccessRequestList = new ArrayList<>();
        bucketAccessRequestList.add( bucketAccessRequest1 );
        bucketAccessRequestList.add( bucketAccessRequest2 );
        bucketAccessRequestList.add( bucketAccessRequest3 );


        when( bucketAccessRequestService.getBucketAccessRequestForAdmin() ).thenReturn( bucketAccessRequestList );

        // execute the get request

        mockMvc.perform( get( "/bucket/bucketAccessRequests" )
        )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) );
        // not sure of how return value is

//                .andExpect( jsonPath( "$[0].objectMetadata.ownerName" ).value( "Owner" ) );


    }

    @Test
    @DisplayName("POST /bucket/createAccessRequest - SUCCESS")
    void createBucketAccessRequest_success() throws Exception {

        // set up the mock service
        BucketAccessRequestFromUi bucketAccessRequestFromUi = new BucketAccessRequestFromUi( 1, "file.server.1", "read" );
        when( bucketAccessRequestService.createBucketAccessRequest( any() ) )
                .thenReturn( true );

        // execute the post request
        mockMvc.perform( post( "/bucket/createAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequestFromUi ) )

        )
                .andExpect( status().isCreated() );
    }

    @Test
    @DisplayName("POST /bucket/createAccessRequest - FAILURE")
    void createBucketAccessRequest_failure() throws Exception {

        // set up the mock service
        BucketAccessRequestFromUi bucketAccessRequestFromUi = new BucketAccessRequestFromUi( 1, "file.server.1", "read" );
        when( bucketAccessRequestService.createBucketAccessRequest( any() ) )
                .thenReturn( false );

        // execute the post request
        mockMvc.perform( post( "/bucket/createAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequestFromUi ) )
        )
                .andExpect( status().isBadRequest() );
    }


    @Test
    @DisplayName("POST /bucket/approveAccessRequest - SUCCESS")
    void approveBucketAccessRequest() throws Exception {
        // set up the mock service

        // difficulty in creating mocks can change this stub for integration testing

        BucketAccessRequestEntity bucketAccessRequest = new BucketAccessRequestEntity();
//        bucketAccessRequest.setStatus( StatusConstants.APPROVED.toString() );
//        bucketAccessRequest.setAdminRole( new AdminRole() );
//        bucketAccessRequest.setAdminAccess( new AdminAccess() );
//        bucketAccessRequest.setAccess( new BucketAccess("read") );
//        bucketAccessRequest.setUser( new User( "sethuram" ) );
//        Bucket bucket = new Bucket();
//        bucket.setName("file.server.1");
//        bucketAccessRequest.setBucket( bucket );

        when( bucketAccessRequestService.approveBucketAccessRequest( any() ) ).thenReturn( Boolean.TRUE );

        // execute the post request
        mockMvc.perform( post( "/bucket/approveAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequest ) )
        )
                .andExpect( status().isCreated() );
    }

    @Test
    @DisplayName("PUT /bucket/rejectAccessRequest - SUCCESS")
    void rejectBucketAccessRequest() throws Exception {
        // set up the mock service

        // difficulty in creating mocks can change this stub for integration testing

        BucketAccessRequestEntity bucketAccessRequest = new BucketAccessRequestEntity();

        when( bucketAccessRequestService.rejectBucketAccessRequest( any() ) ).thenReturn( Boolean.TRUE );

        // execute the post request
        mockMvc.perform( put( "/bucket/rejectAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequest ) )
        )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName("DELETE  /bucket/deleteAccessRequest - SUCCESS")
    void deleteBucketAccessRequest_success() throws Exception {
        // set up the mock service

        // difficulty in creating mocks can change this stub for integration testing

        BucketAccessRequestEntity bucketAccessRequest = new BucketAccessRequestEntity();
        Status status = new Status();
        status.setValue( Boolean.TRUE );
        when( bucketAccessRequestService.deleteBucketAccessRequests( any() ) ).thenReturn( status );

        // execute the post request
        mockMvc.perform( delete( "/bucket/deleteAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequest ) )
        )
                .andExpect( status().isOk() );
    }

    @Test
    @DisplayName("DELETE  /bucket/deleteAccessRequest - NOT FOUND")
    void deleteBucketAccessRequest_not_found() throws Exception {
        // set up the mock service

        // difficulty in creating mocks can change this stub for integration testing

        BucketAccessRequestEntity bucketAccessRequest = new BucketAccessRequestEntity();
        Status status = new Status();
        status.setValue( Boolean.FALSE );
        status.setReasonForFailure( HttpStatus.NOT_FOUND.getReasonPhrase() );
        when( bucketAccessRequestService.deleteBucketAccessRequests( any() ) ).thenReturn( status );

        // execute the post request
        mockMvc.perform( delete( "/bucket/deleteAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequest ) )
        )
                .andExpect( status().isNotFound() );
    }

    @Test
    @DisplayName("DELETE  /bucket/deleteAccessRequest - FAILURE")
    void deleteBucketAccessRequest_failure() throws Exception {
        // set up the mock service

        // difficulty in creating mocks can change this stub for integration testing

        BucketAccessRequestEntity bucketAccessRequest = new BucketAccessRequestEntity();
        Status status = new Status();
        status.setValue( Boolean.FALSE );
        status.setReasonForFailure( HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() );
        when( bucketAccessRequestService.deleteBucketAccessRequests( any() ) ).thenReturn( status );

        // execute the post request
        mockMvc.perform( delete( "/bucket/deleteAccessRequest" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( bucketAccessRequest ) )
        )
                .andExpect( status().isInternalServerError() );
    }

    private String asJsonString(Object object) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString( object );
    }

}