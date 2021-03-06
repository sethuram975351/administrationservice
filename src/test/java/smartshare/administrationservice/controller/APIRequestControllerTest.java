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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import smartshare.administrationservice.dto.*;
import smartshare.administrationservice.dto.response.UserLoginStatus;
import smartshare.administrationservice.models.BucketAccessEntity;
import smartshare.administrationservice.models.ObjectAccessEntity;
import smartshare.administrationservice.models.UserAggregate;
import smartshare.administrationservice.service.APIRequestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class APIRequestControllerTest {

    @MockBean
    private APIRequestService apiRequestService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("GET objects/accessInfo")
    void fetchMetaDataForObjectsInGivenBucketForSpecificUser() throws Exception {

        // set up the mock service
        List<BucketObjectMetadata> usersMetadataForPermittedBucketObjects = new ArrayList<>();
        BucketObjectMetadata bucketObjectMetadata = new BucketObjectMetadata();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setOwnerName( "Owner" );
        ObjectAccessEntity objectAccess = new ObjectAccessEntity( 2, false, true, false );
        AccessingUserInfoForApi accessingUserInfo = new AccessingUserInfoForApi( "sethuram", objectAccess );
        objectMetadata.setAccessingUserInfo( accessingUserInfo );
        bucketObjectMetadata.setObjectName( "Sample1.txt" );
        bucketObjectMetadata.setObjectMetadata( objectMetadata );
        usersMetadataForPermittedBucketObjects.add( bucketObjectMetadata );

        when( apiRequestService.fetchBucketObjectsMetaDataByBucketNameAndUserId( "file.server.1", 1 ) )
                .thenReturn( usersMetadataForPermittedBucketObjects );

        // execute the get request

        mockMvc.perform( get( "/objects/accessInfo" )
                .param( "bucketName", "file.server.1" )
                .param( "userName", "sethuram" )
        )
                .andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( jsonPath( "$.bucketObjectsMetadata[0].objectName" ).value( "Sample1.txt" ) )
                .andExpect( jsonPath( "$.bucketObjectsMetadata[0].objectMetadata.ownerName" ).value( "Owner" ) );

    }

    @Test
    @DisplayName("GET buckets/accessInfo")
    void fetchMetaDataForBucketsInS3() throws Exception {

        // set up the mock service
        BucketAccessEntity bucketAccess = new BucketAccessEntity();
        bucketAccess.setBucketAccessId( 1 );
        bucketAccess.setRead( true );
        bucketAccess.setWrite( false );
        BucketMetadata bucketMetadata = new BucketMetadata( "file.server.1", bucketAccess.getRead(), bucketAccess.getWrite() );

        when( apiRequestService.fetchBucketsMetaDataByUserId( 1 ) )
                .thenReturn( Collections.singletonList( bucketMetadata ) );
        // execute the get request

        mockMvc.perform( get( "/buckets/accessInfo" )
                .param( "userName", "sethuram" )
        )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.bucketsMetadata[0].read" ).value( true ) )
                .andExpect( jsonPath( "$.bucketsMetadata[0].write" ).value( false ) );

    }

    @Test
    @DisplayName("POST register")
    void registerUser() throws Exception {
        // set up the mock service

        UserDto userDto = new UserDto();
        userDto.setUserName( "sethuram" );
        userDto.setEmail( "sethuram@gmail.com" );
        UserAggregate user = new UserAggregate();
        user.setUserId( 1 );
        user.setUserName( "sethuram" );
        user.setEmail( "sethuram@gmail.com" );

        UserLoginStatus userLoginStatus = new UserLoginStatus( user, false, false );

        when( apiRequestService.registerUserAndCheckIsAdmin( userDto ) )
                .thenReturn( userLoginStatus );
        // execute the post request
        mockMvc.perform( post( "/register" )
                .contentType( MediaType.APPLICATION_JSON )
                .content( "{\"userId\":1,\"userName\":\"sethuram\",\"email\":\"sethuram@gmail.com\",\"picture\":\"http:url\"}" )
        )
                .andExpect( status().isOk() );

    }

    private String asJsonString(Object object) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString( object );
    }
}