/*
 * Copyright 2024, AutoMQ HK Limited.
 *
 * Use of this software is governed by the Business Source License
 * included in the file BSL.md
 *
 * As of the Change Date specified in that file, in accordance with
 * the Business Source License, use of this software will be governed
 * by the Apache License, Version 2.0
 */

package com.automq.stream.s3.trace;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import java.lang.reflect.Method;

public final class SpanAttributesExtractor {

    private final MethodCache<AttributeBindings> cache;

    SpanAttributesExtractor(MethodCache<AttributeBindings> cache) {
        this.cache = cache;
    }

    public static SpanAttributesExtractor create() {
        return new SpanAttributesExtractor(new MethodCache<>());
    }

    public Attributes extract(Method method, String[] parametersNames, Object[] args) {
        AttributesBuilder attributes = Attributes.builder();
        AttributeBindings bindings =
            cache.computeIfAbsent(method, (Method m) -> AttributeBindings.bind(m, parametersNames));
        if (!bindings.isEmpty()) {
            bindings.apply(attributes, args);
        }
        return attributes.build();
    }
}
