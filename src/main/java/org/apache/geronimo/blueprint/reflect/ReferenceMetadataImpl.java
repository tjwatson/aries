/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.geronimo.blueprint.reflect;

import org.osgi.service.blueprint.reflect.ReferenceMetadata;
import org.apache.geronimo.blueprint.mutable.MutableReferenceMetadata;

/**
 * Implementation of ReferenceMetadata
 *
 * @author <a href="mailto:dev@geronimo.apache.org">Apache Geronimo Project</a>
 * @version $Rev: 760378 $, $Date: 2009-03-31 11:31:38 +0200 (Tue, 31 Mar 2009) $
 */
public class ReferenceMetadataImpl extends ServiceReferenceMetadataImpl implements MutableReferenceMetadata {

    private long timeout;

    public ReferenceMetadataImpl() {
    }
    
    public ReferenceMetadataImpl(ReferenceMetadata source) {
        super(source);
        timeout = source.getTimeout();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "ReferenceMetadata[" +
                "availability=" + availability +
                ", interfaceNames=" + interfaceNames +
                ", componentName='" + componentName + '\'' +
                ", filter='" + filter + '\'' +
                ", serviceListeners=" + serviceListeners +
                ", timeout=" + timeout +
                ']';
    }
}
