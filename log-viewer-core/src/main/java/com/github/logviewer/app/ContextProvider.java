package com.github.logviewer.app;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by rusakovich on 16.10.2017.
 */


public class ContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;

    public static final String PROFILE_NONE_QA = "NONE_QA";

    /**
     * @return the context
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public static void setContext(ApplicationContext context) {
        ContextProvider.context = context;
    }

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        setContext(context);
    }

}
