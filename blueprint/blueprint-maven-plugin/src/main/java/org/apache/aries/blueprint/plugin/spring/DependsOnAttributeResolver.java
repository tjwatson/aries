/**
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
package org.apache.aries.blueprint.plugin.spring;

import org.apache.aries.blueprint.plugin.spi.BeanAttributesResolver;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.DependsOn;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.Map;

public class DependsOnAttributeResolver implements BeanAttributesResolver<DependsOn> {
    @Override
    public Class<DependsOn> getAnnotation() {
        return DependsOn.class;
    }

    @Override
    public Map<String, String> resolveAttributes(Class<?> clazz, AnnotatedElement annotatedElement) {
        DependsOn annotation = annotatedElement.getAnnotation(DependsOn.class);
        if (annotation == null || annotation.value().length == 0) {
            return new HashMap<>();
        }
        String[] value = annotation.value();
        String dependsOnValue = StringUtils.join(value, " ");
        Map<String, String> map = new HashMap<>();
        map.put("depends-on", dependsOnValue);
        return map;
    }
}
