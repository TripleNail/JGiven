package com.tngtech.jgiven.impl.intercept;

import com.tngtech.jgiven.impl.intercept.StepInterceptor.Invoker;

import java.lang.reflect.Method;

import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.BindingPriority;
import net.bytebuddy.implementation.bind.annotation.DefaultCall;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * StepInterceptorImpl that uses ByteBuddy Method interceptor with annotations for intercepting JGiven methods
 *
 */
public class ByteBuddyMethodInterceptor {

    private final StepInterceptor interceptor;

    public ByteBuddyMethodInterceptor(StepInterceptor interceptor ) {
        this.interceptor = interceptor;
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 3)
    public Object interceptSuper( @SuperCall final Callable<?> zuper,@This final Object receiver,@Origin
        Method method, @AllArguments final Object[] parameters)
        throws Throwable {
        Invoker invoker = new Invoker() {
            @Override
            public Object proceed() throws Throwable {
                return zuper.call();
            }

        };
        return interceptor.intercept( receiver  , method, parameters, invoker );
    }

    @RuntimeType
    @BindingPriority(BindingPriority.DEFAULT * 2)
    public Object interceptDefault(@DefaultCall final Callable<?> zuper,@This final Object receiver,@Origin
        Method method, @AllArguments final Object[] parameters)
        throws Throwable {
        Invoker invoker = new Invoker() {

            @Override
            public Object proceed() throws Throwable {
                return zuper.call();
            }

        };
        return interceptor.intercept( receiver  , method, parameters, invoker );
    }
    @RuntimeType
    public Object intercept( @This final Object receiver,@Origin final Method method, @AllArguments final Object[] parameters) throws Throwable {
        // this intercepted method does not have a non-abstract super method
        Invoker invoker = new Invoker() {

            @Override
            public Object proceed() throws Throwable {
                return null;
            }

        };
        return interceptor.intercept( receiver  , method, parameters, invoker );
    }


}
