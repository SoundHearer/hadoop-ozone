/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership.  The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.apache.hadoop.ozone.om.response.s3.bucket;

import org.apache.hadoop.ozone.om.OMMetadataManager;
import org.apache.hadoop.ozone.om.response.OMClientResponse;
import org.apache.hadoop.ozone.protocol.proto.OzoneManagerProtocolProtos.OMResponse;
import org.apache.hadoop.hdds.utils.db.BatchOperation;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Response for S3Bucket Delete request.
 */
public class S3BucketDeleteResponse extends OMClientResponse {

  private String s3BucketName;
  private String volumeName;

  public S3BucketDeleteResponse(@Nonnull OMResponse omResponse,
      @Nonnull String s3BucketName, @Nonnull String volumeName) {
    super(omResponse);
    this.s3BucketName = s3BucketName;
    this.volumeName = volumeName;
  }

  /**
   * For when the request is not successful or it is a replay transaction.
   * For a successful request, the other constructor should be used.
   */
  public S3BucketDeleteResponse(@Nonnull OMResponse omResponse) {
    super(omResponse);
    checkStatusNotOK();
  }

  @Override
  public void addToDBBatch(OMMetadataManager omMetadataManager,
      BatchOperation batchOperation) throws IOException {

    omMetadataManager.getBucketTable().deleteWithBatch(batchOperation,
        omMetadataManager.getBucketKey(volumeName, s3BucketName));
    omMetadataManager.getS3Table().deleteWithBatch(batchOperation,
        s3BucketName);
  }
}