package com.nice.incontact.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;
import org.apache.log4j.spi.LoggingEvent;
import org.testng.IAnnotationTransformer2;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.IConfigurationAnnotation;
import org.testng.annotations.IDataProviderAnnotation;
import org.testng.annotations.IFactoryAnnotation;
import org.testng.annotations.ITestAnnotation;


public class TestAppender extends AppenderSkeleton implements IAnnotationTransformer2 {
	
	private static InheritableThreadLocal<List<String>> tlsMessages = new InheritableThreadLocal<List<String>>();

    public TestAppender() {
    }

    public TestAppender(Layout layout) {
        setLayout(layout);
    }

    /**
     * Initialize this appender so it can record messages. This is best called
     * during a {@link BeforeClass} method.
     */
    public static void initMessages() {
        tlsMessages.set(new ArrayList<String>());
    }

    /**
     * Get the messages captured by this appender.
     * 
     * @return the messages captured by this appender
     */
    public static List<String> getMessages() {
        return tlsMessages.get();
    }

    /**
     * Clear the messages captured by this appender. This is best called during
     * a {@link BeforeMethod} method.
     */
    public static void clearMessages() {
        List<String> messages = tlsMessages.get();
        if (messages != null) {
            messages.clear();
        }
    }

    @Override
    protected void append(LoggingEvent event) {
        if (layout == null) {
            errorHandler.error("No layout for appender " + name, null, ErrorCode.MISSING_LAYOUT);
        }

        List<String> messages = tlsMessages.get();
        if (messages != null) {
            String message = layout.format(event);
            messages.add(message);
        }
    }


    @Override
    public void close() {
        tlsMessages.set(null);
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
    @Override
	public void transform(ITestAnnotation arg0, Class arg1, Constructor arg2, Method arg3) {
		// TODO Auto-generated method stub
		
	}
    @Override
	public void transform(IDataProviderAnnotation arg0, Method arg1) {
		// TODO Auto-generated method stub
		
	}
    @Override
	public void transform(IFactoryAnnotation arg0, Method arg1) {
		// TODO Auto-generated method stub
		
	}
    @Override
	public void transform(IConfigurationAnnotation arg0, Class arg1, Constructor arg2, Method arg3) {
		// TODO Auto-generated method stub
		
	}

}
