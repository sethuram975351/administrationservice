package smartshare.administrationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import smartshare.administrationservice.constant.StatusConstants;
import smartshare.administrationservice.models.Bucket;
import smartshare.administrationservice.models.Status;
import smartshare.administrationservice.repository.AdminRoleRepository;
import smartshare.administrationservice.repository.BucketRepository;

@Slf4j
@Service
public class BucketAdministrationService {

    private BucketRepository bucketRepository;
    private AdminRoleRepository adminRoleRepository;
    private Status statusOfOperation;

    @Autowired
    BucketAdministrationService(BucketRepository bucketRepository, AdminRoleRepository adminRoleRepository, Status statusOfOperation) {
        this.bucketRepository = bucketRepository;
        this.adminRoleRepository = adminRoleRepository;
        this.statusOfOperation = statusOfOperation;
    }

    public Bucket createBucketInAccessManagementDb(String bucketName) {
        log.info( "Inside creatBucketInAccessManagementDb" );
        Bucket newBucket = new Bucket();
        newBucket.setName( bucketName );
        newBucket.setAdminRole( adminRoleRepository.getOne( Long.valueOf( "0000" ) ) ); // assuming update happens getting the current admin object
        Bucket savedBucket = bucketRepository.saveAndFlush( newBucket );
        System.out.println( "savedBucket-------->" + savedBucket );
        return savedBucket;
    }

    public Status deleteBucketInAccessManagementDb(String bucketName) {
        log.info( "Inside deleteBucketInAccessManagementDb" );
        Bucket bucketToBeDeleted = bucketRepository.findByName( bucketName );
        if (null == bucketToBeDeleted.getObjects()) {
            bucketRepository.delete( bucketToBeDeleted );
            statusOfOperation.setMessage( StatusConstants.SUCCESS.toString() );
        } else {
            statusOfOperation.setMessage( StatusConstants.FAILED.toString() );
            statusOfOperation.setReasonForFailure( "Please delete Bucket Objects before deleting the bucket" );
        }
        return statusOfOperation;
    }
}