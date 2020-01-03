package org.jmqtt.persistent.utils;

import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

/**
 * @author Alex Liu
 * @date 2020/01/03
 */
@Component
public class PropertiesUtils implements EmbeddedValueResolverAware {
    private static StringValueResolver valueResolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
        this.valueResolver = stringValueResolver;
    }

    public static String getPropertiesValue(String name) {
        return valueResolver.resolveStringValue(name);
    }

}
