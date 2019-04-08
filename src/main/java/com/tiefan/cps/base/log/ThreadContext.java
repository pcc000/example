package com.tiefan.cps.base.log;

import java.util.UUID;

public class ThreadContext {
	
	public String uuid;

	
	private static final InheritableThreadLocal<ThreadContext> CONTEXT = new InheritableThreadLocal<ThreadContext>() {
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };
	
    public static ThreadContext get() {
    	return CONTEXT.get();
    }
    
    public static void set(ThreadContext context) {
    	CONTEXT.set(context);
    }
    /**
     * 获得uuid
     * @return uuid
     */
    public static String getUUID() {
        return CONTEXT.get().uuid;
    }

    /**
     * 刷新流程uuid
     */
    public static void refreshUUID(){
        String uuid = UUID.randomUUID().toString();
        if (uuid.contains("-")) {
            uuid = uuid.substring(uuid.lastIndexOf("-") + 1);
        }
        CONTEXT.get().uuid = uuid;
    }

    /**
     * 清空
     */
    public static void empty() {
    	CONTEXT.set(new ThreadContext());
    }
}
