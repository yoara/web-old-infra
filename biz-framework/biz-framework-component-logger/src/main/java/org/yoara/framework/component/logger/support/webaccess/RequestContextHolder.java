package org.yoara.framework.component.logger.support.webaccess;

public abstract class  RequestContextHolder {

    private static final ThreadLocal<RequestContext> digestContextHolder = new ThreadLocal<>();

    private static ThreadLocal<ExtendedParameter> extendedParameterHolder = new ThreadLocal();

    public static void setExtendedParameter(ExtendedParameter extendedParameter){
        extendedParameterHolder.set(extendedParameter);
    }

    public static RequestContext init() {
        return init(false);
    }

    public static RequestContext init(boolean debug) {
        RequestContext ctx = digestContextHolder.get();
        ExtendedParameter extendedParameter = extendedParameterHolder.get();
        if (ctx == null) {
            ctx = new RequestContext();
            ctx.setDebug(debug);
            ctx.setLoginAccount(extendedParameter !=null ? extendedParameter.getAccessAccount() : null);
            digestContextHolder.set(ctx);
        }
        return ctx;
    }

    public static RequestContext getDigestContext() {
        return digestContextHolder.get();
    }

    public static void clear() {
        extendedParameterHolder.remove();
        digestContextHolder.remove();
    }
}