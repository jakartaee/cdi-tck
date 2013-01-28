#Signature file v4.0
#Version 

CLSS public abstract interface java.io.Serializable

CLSS public abstract interface java.lang.Comparable<%0 extends java.lang.Object>
meth public abstract int compareTo({java.lang.Comparable%0})

CLSS public abstract java.lang.Enum<%0 extends java.lang.Enum<{java.lang.Enum%0}>>
cons protected Enum(java.lang.String,int)
intf java.io.Serializable
intf java.lang.Comparable<{java.lang.Enum%0}>
meth protected final java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected final void finalize()
meth public final boolean equals(java.lang.Object)
meth public final int compareTo({java.lang.Enum%0})
meth public final int hashCode()
meth public final int ordinal()
meth public final java.lang.Class<{java.lang.Enum%0}> getDeclaringClass()
meth public final java.lang.String name()
meth public java.lang.String toString()
meth public static <%0 extends java.lang.Enum<{%%0}>> {%%0} valueOf(java.lang.Class<{%%0}>,java.lang.String)
supr java.lang.Object
hfds name,ordinal

CLSS public java.lang.Exception
cons protected Exception(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public Exception()
cons public Exception(java.lang.String)
cons public Exception(java.lang.String,java.lang.Throwable)
cons public Exception(java.lang.Throwable)
supr java.lang.Throwable
hfds serialVersionUID

CLSS public abstract interface java.lang.Iterable<%0 extends java.lang.Object>
meth public abstract java.util.Iterator<{java.lang.Iterable%0}> iterator()

CLSS public java.lang.Object
cons public Object()
meth protected java.lang.Object clone() throws java.lang.CloneNotSupportedException
meth protected void finalize() throws java.lang.Throwable
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<?> getClass()
meth public final void notify()
meth public final void notifyAll()
meth public final void wait() throws java.lang.InterruptedException
meth public final void wait(long) throws java.lang.InterruptedException
meth public final void wait(long,int) throws java.lang.InterruptedException
meth public int hashCode()
meth public java.lang.String toString()

CLSS public java.lang.RuntimeException
cons protected RuntimeException(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public RuntimeException()
cons public RuntimeException(java.lang.String)
cons public RuntimeException(java.lang.String,java.lang.Throwable)
cons public RuntimeException(java.lang.Throwable)
supr java.lang.Exception
hfds serialVersionUID

CLSS public java.lang.Throwable
cons protected Throwable(java.lang.String,java.lang.Throwable,boolean,boolean)
cons public Throwable()
cons public Throwable(java.lang.String)
cons public Throwable(java.lang.String,java.lang.Throwable)
cons public Throwable(java.lang.Throwable)
intf java.io.Serializable
meth public final java.lang.Throwable[] getSuppressed()
meth public final void addSuppressed(java.lang.Throwable)
meth public java.lang.StackTraceElement[] getStackTrace()
meth public java.lang.String getLocalizedMessage()
meth public java.lang.String getMessage()
meth public java.lang.String toString()
meth public java.lang.Throwable fillInStackTrace()
meth public java.lang.Throwable getCause()
meth public java.lang.Throwable initCause(java.lang.Throwable)
meth public void printStackTrace()
meth public void printStackTrace(java.io.PrintStream)
meth public void printStackTrace(java.io.PrintWriter)
meth public void setStackTrace(java.lang.StackTraceElement[])
supr java.lang.Object
hfds CAUSE_CAPTION,EMPTY_THROWABLE_ARRAY,NULL_CAUSE_MESSAGE,SELF_SUPPRESSION_MESSAGE,SUPPRESSED_CAPTION,SUPPRESSED_SENTINEL,UNASSIGNED_STACK,backtrace,cause,detailMessage,serialVersionUID,stackTrace,suppressedExceptions
hcls PrintStreamOrWriter,SentinelHolder,WrappedPrintStream,WrappedPrintWriter

CLSS public abstract interface java.lang.annotation.Annotation
meth public abstract boolean equals(java.lang.Object)
meth public abstract int hashCode()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public abstract java.lang.String toString()

CLSS public abstract interface !annotation java.lang.annotation.Documented
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Inherited
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation java.lang.annotation.Retention
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.RetentionPolicy value()

CLSS public abstract interface !annotation java.lang.annotation.Target
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract java.lang.annotation.ElementType[] value()

CLSS public abstract interface !annotation javax.decorator.Decorator
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
 anno 0 javax.enterprise.inject.Stereotype()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.decorator.Delegate
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.ApplicationScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.NormalScope(boolean passivating=false)
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.context.BusyConversationException
cons public BusyConversationException()
cons public BusyConversationException(java.lang.String)
cons public BusyConversationException(java.lang.String,java.lang.Throwable)
cons public BusyConversationException(java.lang.Throwable)
supr javax.enterprise.context.ContextException
hfds serialVersionUID

CLSS public javax.enterprise.context.ContextException
cons public ContextException()
cons public ContextException(java.lang.String)
cons public ContextException(java.lang.String,java.lang.Throwable)
cons public ContextException(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public javax.enterprise.context.ContextNotActiveException
cons public ContextNotActiveException()
cons public ContextNotActiveException(java.lang.String)
cons public ContextNotActiveException(java.lang.String,java.lang.Throwable)
cons public ContextNotActiveException(java.lang.Throwable)
supr javax.enterprise.context.ContextException
hfds serialVersionUID

CLSS public abstract interface javax.enterprise.context.Conversation
meth public abstract boolean isTransient()
meth public abstract java.lang.String getId()
meth public abstract long getTimeout()
meth public abstract void begin()
meth public abstract void begin(java.lang.String)
meth public abstract void end()
meth public abstract void setTimeout(long)

CLSS public abstract interface !annotation javax.enterprise.context.ConversationScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.NormalScope(boolean passivating=true)
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.Dependent
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, TYPE, FIELD])
 anno 0 javax.inject.Scope()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.Destroyed
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> value()

CLSS public abstract interface !annotation javax.enterprise.context.Initialized
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> value()

CLSS public javax.enterprise.context.NonexistentConversationException
cons public NonexistentConversationException()
cons public NonexistentConversationException(java.lang.String)
cons public NonexistentConversationException(java.lang.String,java.lang.Throwable)
cons public NonexistentConversationException(java.lang.Throwable)
supr javax.enterprise.context.ContextException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.context.NormalScope
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean passivating()

CLSS public abstract interface !annotation javax.enterprise.context.RequestScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.NormalScope(boolean passivating=false)
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.SessionScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.NormalScope(boolean passivating=true)
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.enterprise.context.spi.AlterableContext
intf javax.enterprise.context.spi.Context
meth public abstract void destroy(javax.enterprise.context.spi.Contextual<?>)

CLSS public abstract interface javax.enterprise.context.spi.Context
meth public abstract <%0 extends java.lang.Object> {%%0} get(javax.enterprise.context.spi.Contextual<{%%0}>)
meth public abstract <%0 extends java.lang.Object> {%%0} get(javax.enterprise.context.spi.Contextual<{%%0}>,javax.enterprise.context.spi.CreationalContext<{%%0}>)
meth public abstract boolean isActive()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> getScope()

CLSS public abstract interface javax.enterprise.context.spi.Contextual<%0 extends java.lang.Object>
meth public abstract void destroy({javax.enterprise.context.spi.Contextual%0},javax.enterprise.context.spi.CreationalContext<{javax.enterprise.context.spi.Contextual%0}>)
meth public abstract {javax.enterprise.context.spi.Contextual%0} create(javax.enterprise.context.spi.CreationalContext<{javax.enterprise.context.spi.Contextual%0}>)

CLSS public abstract interface javax.enterprise.context.spi.CreationalContext<%0 extends java.lang.Object>
meth public abstract void push({javax.enterprise.context.spi.CreationalContext%0})
meth public abstract void release()

CLSS public abstract interface javax.enterprise.event.Event<%0 extends java.lang.Object>
meth public abstract !varargs <%0 extends {javax.enterprise.event.Event%0}> javax.enterprise.event.Event<{%%0}> select(java.lang.Class<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs <%0 extends {javax.enterprise.event.Event%0}> javax.enterprise.event.Event<{%%0}> select(javax.enterprise.util.TypeLiteral<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs javax.enterprise.event.Event<{javax.enterprise.event.Event%0}> select(java.lang.annotation.Annotation[])
meth public abstract void fire({javax.enterprise.event.Event%0})

CLSS public javax.enterprise.event.ObserverException
cons public ObserverException()
cons public ObserverException(java.lang.String)
cons public ObserverException(java.lang.String,java.lang.Throwable)
cons public ObserverException(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.event.Observes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault javax.enterprise.event.Reception notifyObserver()
meth public abstract !hasdefault javax.enterprise.event.TransactionPhase during()

CLSS public final !enum javax.enterprise.event.Reception
fld public final static javax.enterprise.event.Reception ALWAYS
fld public final static javax.enterprise.event.Reception IF_EXISTS
meth public static javax.enterprise.event.Reception valueOf(java.lang.String)
meth public static javax.enterprise.event.Reception[] values()
supr java.lang.Enum<javax.enterprise.event.Reception>

CLSS public final !enum javax.enterprise.event.TransactionPhase
fld public final static javax.enterprise.event.TransactionPhase AFTER_COMPLETION
fld public final static javax.enterprise.event.TransactionPhase AFTER_FAILURE
fld public final static javax.enterprise.event.TransactionPhase AFTER_SUCCESS
fld public final static javax.enterprise.event.TransactionPhase BEFORE_COMPLETION
fld public final static javax.enterprise.event.TransactionPhase IN_PROGRESS
meth public static javax.enterprise.event.TransactionPhase valueOf(java.lang.String)
meth public static javax.enterprise.event.TransactionPhase[] values()
supr java.lang.Enum<javax.enterprise.event.TransactionPhase>

CLSS public abstract interface !annotation javax.enterprise.inject.Alternative
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.inject.AmbiguousResolutionException
cons public AmbiguousResolutionException()
cons public AmbiguousResolutionException(java.lang.String)
cons public AmbiguousResolutionException(java.lang.String,java.lang.Throwable)
cons public AmbiguousResolutionException(java.lang.Throwable)
supr javax.enterprise.inject.ResolutionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.Any
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD, PARAMETER])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.inject.CreationException
cons public CreationException()
cons public CreationException(java.lang.String)
cons public CreationException(java.lang.String,java.lang.Throwable)
cons public CreationException(java.lang.Throwable)
supr javax.enterprise.inject.InjectionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.Decorated
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER, FIELD])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Default
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Disposes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.inject.IllegalProductException
cons public IllegalProductException()
cons public IllegalProductException(java.lang.String)
cons public IllegalProductException(java.lang.String,java.lang.Throwable)
cons public IllegalProductException(java.lang.Throwable)
supr javax.enterprise.inject.InjectionException
hfds serialVersionUID

CLSS public javax.enterprise.inject.InjectionException
cons public InjectionException()
cons public InjectionException(java.lang.String)
cons public InjectionException(java.lang.String,java.lang.Throwable)
cons public InjectionException(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public abstract interface javax.enterprise.inject.Instance<%0 extends java.lang.Object>
intf java.lang.Iterable<{javax.enterprise.inject.Instance%0}>
intf javax.inject.Provider<{javax.enterprise.inject.Instance%0}>
meth public abstract !varargs <%0 extends {javax.enterprise.inject.Instance%0}> javax.enterprise.inject.Instance<{%%0}> select(java.lang.Class<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs <%0 extends {javax.enterprise.inject.Instance%0}> javax.enterprise.inject.Instance<{%%0}> select(javax.enterprise.util.TypeLiteral<{%%0}>,java.lang.annotation.Annotation[])
meth public abstract !varargs javax.enterprise.inject.Instance<{javax.enterprise.inject.Instance%0}> select(java.lang.annotation.Annotation[])
meth public abstract boolean isAmbiguous()
meth public abstract boolean isUnsatisfied()
meth public abstract void destroy({javax.enterprise.inject.Instance%0})

CLSS public abstract interface !annotation javax.enterprise.inject.Intercepted
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER, FIELD])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Model
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.RequestScoped()
 anno 0 javax.enterprise.inject.Stereotype()
 anno 0 javax.inject.Named(java.lang.String value="")
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.New
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, PARAMETER, METHOD, TYPE])
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<?> value()

CLSS public abstract interface !annotation javax.enterprise.inject.Produces
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD])
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.inject.ResolutionException
cons public ResolutionException()
cons public ResolutionException(java.lang.String)
cons public ResolutionException(java.lang.String,java.lang.Throwable)
cons public ResolutionException(java.lang.Throwable)
supr javax.enterprise.inject.InjectionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.Specializes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Stereotype
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Typed
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, METHOD, TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<?>[] value()

CLSS public javax.enterprise.inject.UnproxyableResolutionException
cons public UnproxyableResolutionException()
cons public UnproxyableResolutionException(java.lang.String)
cons public UnproxyableResolutionException(java.lang.String,java.lang.Throwable)
cons public UnproxyableResolutionException(java.lang.Throwable)
supr javax.enterprise.inject.ResolutionException
hfds serialVersionUID

CLSS public javax.enterprise.inject.UnsatisfiedResolutionException
cons public UnsatisfiedResolutionException()
cons public UnsatisfiedResolutionException(java.lang.String)
cons public UnsatisfiedResolutionException(java.lang.String,java.lang.Throwable)
cons public UnsatisfiedResolutionException(java.lang.Throwable)
supr javax.enterprise.inject.ResolutionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.Vetoed
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, PACKAGE])
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.enterprise.inject.spi.AfterBeanDiscovery
meth public abstract void addBean(javax.enterprise.inject.spi.Bean<?>)
meth public abstract void addContext(javax.enterprise.context.spi.Context)
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void addObserverMethod(javax.enterprise.inject.spi.ObserverMethod<?>)

CLSS public abstract interface javax.enterprise.inject.spi.AfterDeploymentValidation
meth public abstract void addDeploymentProblem(java.lang.Throwable)

CLSS public abstract interface javax.enterprise.inject.spi.Annotated
meth public abstract <%0 extends java.lang.annotation.Annotation> {%%0} getAnnotation(java.lang.Class<{%%0}>)
meth public abstract boolean isAnnotationPresent(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.lang.reflect.Type getBaseType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getAnnotations()
meth public abstract java.util.Set<java.lang.reflect.Type> getTypeClosure()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedCallable<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.AnnotatedMember<{javax.enterprise.inject.spi.AnnotatedCallable%0}>
meth public abstract java.util.List<javax.enterprise.inject.spi.AnnotatedParameter<{javax.enterprise.inject.spi.AnnotatedCallable%0}>> getParameters()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedConstructor<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.AnnotatedCallable<{javax.enterprise.inject.spi.AnnotatedConstructor%0}>
meth public abstract java.lang.reflect.Constructor<{javax.enterprise.inject.spi.AnnotatedConstructor%0}> getJavaMember()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedField<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.AnnotatedMember<{javax.enterprise.inject.spi.AnnotatedField%0}>
meth public abstract java.lang.reflect.Field getJavaMember()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedMember<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Annotated
meth public abstract boolean isStatic()
meth public abstract java.lang.reflect.Member getJavaMember()
meth public abstract javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.AnnotatedMember%0}> getDeclaringType()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedMethod<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.AnnotatedCallable<{javax.enterprise.inject.spi.AnnotatedMethod%0}>
meth public abstract java.lang.reflect.Method getJavaMember()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedParameter<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Annotated
meth public abstract int getPosition()
meth public abstract javax.enterprise.inject.spi.AnnotatedCallable<{javax.enterprise.inject.spi.AnnotatedParameter%0}> getDeclaringCallable()

CLSS public abstract interface javax.enterprise.inject.spi.AnnotatedType<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Annotated
meth public abstract java.lang.Class<{javax.enterprise.inject.spi.AnnotatedType%0}> getJavaClass()
meth public abstract java.util.Set<javax.enterprise.inject.spi.AnnotatedConstructor<{javax.enterprise.inject.spi.AnnotatedType%0}>> getConstructors()
meth public abstract java.util.Set<javax.enterprise.inject.spi.AnnotatedField<? super {javax.enterprise.inject.spi.AnnotatedType%0}>> getFields()
meth public abstract java.util.Set<javax.enterprise.inject.spi.AnnotatedMethod<? super {javax.enterprise.inject.spi.AnnotatedType%0}>> getMethods()

CLSS public abstract interface javax.enterprise.inject.spi.Bean<%0 extends java.lang.Object>
intf javax.enterprise.context.spi.Contextual<{javax.enterprise.inject.spi.Bean%0}>
intf javax.enterprise.inject.spi.BeanAttributes<{javax.enterprise.inject.spi.Bean%0}>
meth public abstract java.lang.Class<?> getBeanClass()
meth public abstract java.util.Set<javax.enterprise.inject.spi.InjectionPoint> getInjectionPoints()

CLSS public abstract interface javax.enterprise.inject.spi.BeanAttributes<%0 extends java.lang.Object>
meth public abstract boolean isAlternative()
meth public abstract boolean isNullable()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> getScope()
meth public abstract java.lang.String getName()
meth public abstract java.util.Set<java.lang.Class<? extends java.lang.annotation.Annotation>> getStereotypes()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()
meth public abstract java.util.Set<java.lang.reflect.Type> getTypes()

CLSS public abstract interface javax.enterprise.inject.spi.BeanManager
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.enterprise.inject.spi.ObserverMethod<? super {%%0}>> resolveObserverMethods({%%0},java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.List<javax.enterprise.inject.spi.Decorator<?>> resolveDecorators(java.util.Set<java.lang.reflect.Type>,java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.List<javax.enterprise.inject.spi.Interceptor<?>> resolveInterceptors(javax.enterprise.inject.spi.InterceptionType,java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.Set<javax.enterprise.inject.spi.Bean<?>> getBeans(java.lang.reflect.Type,java.lang.annotation.Annotation[])
meth public abstract !varargs void fireEvent(java.lang.Object,java.lang.annotation.Annotation[])
meth public abstract <%0 extends java.lang.Object> java.lang.Iterable<javax.enterprise.inject.spi.AnnotatedType<{%%0}>> getAnnotatedTypes(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.context.spi.CreationalContext<{%%0}> createCreationalContext(javax.enterprise.context.spi.Contextual<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.AnnotatedType<{%%0}> createAnnotatedType(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.AnnotatedType<{%%0}> getAnnotatedType(java.lang.Class<{%%0}>,java.lang.String)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Bean<? extends {%%0}> resolve(java.util.Set<javax.enterprise.inject.spi.Bean<? extends {%%0}>>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Bean<{%%0}> createBean(javax.enterprise.inject.spi.BeanAttributes<{%%0}>,java.lang.Class<?>,javax.enterprise.inject.spi.Producer<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Bean<{%%0}> createBean(javax.enterprise.inject.spi.BeanAttributes<{%%0}>,java.lang.Class<{%%0}>,javax.enterprise.inject.spi.InjectionTarget<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.BeanAttributes<{%%0}> createBeanAttributes(javax.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.InjectionTarget<{%%0}> createInjectionTarget(javax.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Producer<?> createProducer(javax.enterprise.inject.spi.AnnotatedField<? super {%%0}>,javax.enterprise.inject.spi.Bean<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Producer<?> createProducer(javax.enterprise.inject.spi.AnnotatedMethod<? super {%%0}>,javax.enterprise.inject.spi.Bean<{%%0}>)
meth public abstract <%0 extends javax.enterprise.inject.spi.Extension> {%%0} getExtension(java.lang.Class<{%%0}>)
meth public abstract boolean areInterceptorBindingsEquivalent(java.lang.annotation.Annotation,java.lang.annotation.Annotation)
meth public abstract boolean areQualifiersEquivalent(java.lang.annotation.Annotation,java.lang.annotation.Annotation)
meth public abstract boolean isInterceptorBinding(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isNormalScope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isPassivatingScope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isQualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isScope(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract int getInterceptorBindingHashCode(java.lang.annotation.Annotation)
meth public abstract int getQualifierHashCode(java.lang.annotation.Annotation)
meth public abstract java.lang.Object getInjectableReference(javax.enterprise.inject.spi.InjectionPoint,javax.enterprise.context.spi.CreationalContext<?>)
meth public abstract java.lang.Object getReference(javax.enterprise.inject.spi.Bean<?>,java.lang.reflect.Type,javax.enterprise.context.spi.CreationalContext<?>)
meth public abstract java.util.Set<java.lang.annotation.Annotation> getInterceptorBindingDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.util.Set<java.lang.annotation.Annotation> getStereotypeDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.util.Set<javax.enterprise.inject.spi.Bean<?>> getBeans(java.lang.String)
meth public abstract javax.el.ELResolver getELResolver()
meth public abstract javax.el.ExpressionFactory wrapExpressionFactory(javax.el.ExpressionFactory)
meth public abstract javax.enterprise.context.spi.Context getContext(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract javax.enterprise.inject.spi.Bean<?> getPassivationCapableBean(java.lang.String)
meth public abstract javax.enterprise.inject.spi.BeanAttributes<?> createBeanAttributes(javax.enterprise.inject.spi.AnnotatedMember<?>)
meth public abstract javax.enterprise.inject.spi.InjectionPoint createInjectionPoint(javax.enterprise.inject.spi.AnnotatedField<?>)
meth public abstract javax.enterprise.inject.spi.InjectionPoint createInjectionPoint(javax.enterprise.inject.spi.AnnotatedParameter<?>)
meth public abstract void validate(javax.enterprise.inject.spi.InjectionPoint)

CLSS public abstract interface javax.enterprise.inject.spi.BeforeBeanDiscovery
meth public abstract !varargs void addInterceptorBinding(java.lang.Class<? extends java.lang.annotation.Annotation>,java.lang.annotation.Annotation[])
meth public abstract !varargs void addStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>,java.lang.annotation.Annotation[])
meth public abstract void addAnnotatedType(javax.enterprise.inject.spi.AnnotatedType<?>)
meth public abstract void addInterceptorBinding(javax.enterprise.inject.spi.AnnotatedType<? extends java.lang.annotation.Annotation>)
meth public abstract void addQualifier(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void addQualifier(javax.enterprise.inject.spi.AnnotatedType<? extends java.lang.annotation.Annotation>)
meth public abstract void addScope(java.lang.Class<? extends java.lang.annotation.Annotation>,boolean,boolean)

CLSS public abstract interface javax.enterprise.inject.spi.BeforeShutdown

CLSS public abstract javax.enterprise.inject.spi.CDI<%0 extends java.lang.Object>
cons public CDI()
fld protected static volatile java.util.Set<javax.enterprise.inject.spi.CDIProvider> discoveredProviders
fld protected static volatile javax.enterprise.inject.spi.CDIProvider configuredProvider
intf javax.enterprise.inject.Instance<{javax.enterprise.inject.spi.CDI%0}>
meth public abstract javax.enterprise.inject.spi.BeanManager getBeanManager()
meth public static javax.enterprise.inject.spi.CDI<java.lang.Object> current()
meth public static void setCDIProvider(javax.enterprise.inject.spi.CDIProvider)
supr java.lang.Object
hfds lock,nonCommentPattern

CLSS public abstract interface javax.enterprise.inject.spi.CDIProvider
meth public abstract javax.enterprise.inject.spi.CDI<java.lang.Object> getCDI()

CLSS public abstract interface javax.enterprise.inject.spi.Decorator<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.Decorator%0}>
meth public abstract java.lang.reflect.Type getDelegateType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getDelegateQualifiers()
meth public abstract java.util.Set<java.lang.reflect.Type> getDecoratedTypes()

CLSS public javax.enterprise.inject.spi.DefinitionException
cons public DefinitionException(java.lang.String)
cons public DefinitionException(java.lang.String,java.lang.Throwable)
cons public DefinitionException(java.lang.Throwable)
supr java.lang.RuntimeException

CLSS public javax.enterprise.inject.spi.DeploymentException
cons public DeploymentException(java.lang.String)
cons public DeploymentException(java.lang.String,java.lang.Throwable)
cons public DeploymentException(java.lang.Throwable)
supr java.lang.RuntimeException

CLSS public abstract interface javax.enterprise.inject.spi.Extension

CLSS public abstract interface javax.enterprise.inject.spi.IdentifiedAnnotatedType<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.IdentifiedAnnotatedType%0}>
meth public abstract java.lang.String getId()

CLSS public abstract interface javax.enterprise.inject.spi.InjectionPoint
meth public abstract boolean isDelegate()
meth public abstract boolean isTransient()
meth public abstract java.lang.reflect.Member getMember()
meth public abstract java.lang.reflect.Type getType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()
meth public abstract javax.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract javax.enterprise.inject.spi.Bean<?> getBean()

CLSS public abstract interface javax.enterprise.inject.spi.InjectionTarget<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Producer<{javax.enterprise.inject.spi.InjectionTarget%0}>
meth public abstract void inject({javax.enterprise.inject.spi.InjectionTarget%0},javax.enterprise.context.spi.CreationalContext<{javax.enterprise.inject.spi.InjectionTarget%0}>)
meth public abstract void postConstruct({javax.enterprise.inject.spi.InjectionTarget%0})
meth public abstract void preDestroy({javax.enterprise.inject.spi.InjectionTarget%0})

CLSS public final !enum javax.enterprise.inject.spi.InterceptionType
fld public final static javax.enterprise.inject.spi.InterceptionType AROUND_INVOKE
fld public final static javax.enterprise.inject.spi.InterceptionType AROUND_TIMEOUT
fld public final static javax.enterprise.inject.spi.InterceptionType POST_ACTIVATE
fld public final static javax.enterprise.inject.spi.InterceptionType POST_CONSTRUCT
fld public final static javax.enterprise.inject.spi.InterceptionType PRE_DESTROY
fld public final static javax.enterprise.inject.spi.InterceptionType PRE_PASSIVATE
meth public static javax.enterprise.inject.spi.InterceptionType valueOf(java.lang.String)
meth public static javax.enterprise.inject.spi.InterceptionType[] values()
supr java.lang.Enum<javax.enterprise.inject.spi.InterceptionType>

CLSS public abstract interface javax.enterprise.inject.spi.Interceptor<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.Interceptor%0}>
meth public abstract boolean intercepts(javax.enterprise.inject.spi.InterceptionType)
meth public abstract java.lang.Object intercept(javax.enterprise.inject.spi.InterceptionType,{javax.enterprise.inject.spi.Interceptor%0},javax.interceptor.InvocationContext) throws java.lang.Exception
meth public abstract java.util.Set<java.lang.annotation.Annotation> getInterceptorBindings()

CLSS public abstract interface javax.enterprise.inject.spi.ObserverMethod<%0 extends java.lang.Object>
meth public abstract java.lang.Class<?> getBeanClass()
meth public abstract java.lang.reflect.Type getObservedType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getObservedQualifiers()
meth public abstract javax.enterprise.event.Reception getReception()
meth public abstract javax.enterprise.event.TransactionPhase getTransactionPhase()
meth public abstract void notify({javax.enterprise.inject.spi.ObserverMethod%0})

CLSS public abstract interface javax.enterprise.inject.spi.PassivationCapable
meth public abstract java.lang.String getId()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessAnnotatedType<%0 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.ProcessAnnotatedType%0}> getAnnotatedType()
meth public abstract void setAnnotatedType(javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.ProcessAnnotatedType%0}>)
meth public abstract void veto()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessBean<%0 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ProcessBean%0}> getBean()
meth public abstract void addDefinitionError(java.lang.Throwable)

CLSS public abstract interface javax.enterprise.inject.spi.ProcessBeanAttributes<%0 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract javax.enterprise.inject.spi.BeanAttributes<{javax.enterprise.inject.spi.ProcessBeanAttributes%0}> getBeanAttributes()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setBeanAttributes(javax.enterprise.inject.spi.BeanAttributes<{javax.enterprise.inject.spi.ProcessBeanAttributes%0}>)
meth public abstract void veto()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessInjectionPoint<%0 extends java.lang.Object, %1 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.InjectionPoint getInjectionPoint()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setInjectionPoint(javax.enterprise.inject.spi.InjectionPoint)

CLSS public abstract interface javax.enterprise.inject.spi.ProcessInjectionTarget<%0 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.ProcessInjectionTarget%0}> getAnnotatedType()
meth public abstract javax.enterprise.inject.spi.InjectionTarget<{javax.enterprise.inject.spi.ProcessInjectionTarget%0}> getInjectionTarget()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setInjectionTarget(javax.enterprise.inject.spi.InjectionTarget<{javax.enterprise.inject.spi.ProcessInjectionTarget%0}>)

CLSS public abstract interface javax.enterprise.inject.spi.ProcessManagedBean<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.ProcessBean<{javax.enterprise.inject.spi.ProcessManagedBean%0}>
meth public abstract javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.ProcessManagedBean%0}> getAnnotatedBeanClass()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessModule
meth public abstract java.io.InputStream getBeansXml()
meth public abstract java.util.List<java.lang.Class<?>> getAlternatives()
meth public abstract java.util.List<java.lang.Class<?>> getDecorators()
meth public abstract java.util.List<java.lang.Class<?>> getInterceptors()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessObserverMethod<%0 extends java.lang.Object, %1 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.AnnotatedMethod<{javax.enterprise.inject.spi.ProcessObserverMethod%1}> getAnnotatedMethod()
meth public abstract javax.enterprise.inject.spi.ObserverMethod<{javax.enterprise.inject.spi.ProcessObserverMethod%0}> getObserverMethod()
meth public abstract void addDefinitionError(java.lang.Throwable)

CLSS public abstract interface javax.enterprise.inject.spi.ProcessProducer<%0 extends java.lang.Object, %1 extends java.lang.Object>
meth public abstract javax.enterprise.inject.spi.AnnotatedMember<{javax.enterprise.inject.spi.ProcessProducer%0}> getAnnotatedMember()
meth public abstract javax.enterprise.inject.spi.Producer<{javax.enterprise.inject.spi.ProcessProducer%1}> getProducer()
meth public abstract void addDefinitionError(java.lang.Throwable)
meth public abstract void setProducer(javax.enterprise.inject.spi.Producer<{javax.enterprise.inject.spi.ProcessProducer%1}>)

CLSS public abstract interface javax.enterprise.inject.spi.ProcessProducerField<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf javax.enterprise.inject.spi.ProcessBean<{javax.enterprise.inject.spi.ProcessProducerField%1}>
meth public abstract javax.enterprise.inject.spi.AnnotatedField<{javax.enterprise.inject.spi.ProcessProducerField%0}> getAnnotatedProducerField()
meth public abstract javax.enterprise.inject.spi.AnnotatedParameter<{javax.enterprise.inject.spi.ProcessProducerField%0}> getAnnotatedDisposedParameter()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessProducerMethod<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf javax.enterprise.inject.spi.ProcessBean<{javax.enterprise.inject.spi.ProcessProducerMethod%1}>
meth public abstract javax.enterprise.inject.spi.AnnotatedMethod<{javax.enterprise.inject.spi.ProcessProducerMethod%0}> getAnnotatedProducerMethod()
meth public abstract javax.enterprise.inject.spi.AnnotatedParameter<{javax.enterprise.inject.spi.ProcessProducerMethod%0}> getAnnotatedDisposedParameter()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessSessionBean<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.ProcessManagedBean<java.lang.Object>
meth public abstract java.lang.String getEjbName()
meth public abstract javax.enterprise.inject.spi.SessionBeanType getSessionBeanType()

CLSS public abstract interface javax.enterprise.inject.spi.ProcessSyntheticAnnotatedType<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.ProcessAnnotatedType<{javax.enterprise.inject.spi.ProcessSyntheticAnnotatedType%0}>
meth public abstract javax.enterprise.inject.spi.Extension getSource()

CLSS public abstract interface javax.enterprise.inject.spi.Producer<%0 extends java.lang.Object>
meth public abstract java.util.Set<javax.enterprise.inject.spi.InjectionPoint> getInjectionPoints()
meth public abstract void dispose({javax.enterprise.inject.spi.Producer%0})
meth public abstract {javax.enterprise.inject.spi.Producer%0} produce(javax.enterprise.context.spi.CreationalContext<{javax.enterprise.inject.spi.Producer%0}>)

CLSS public final !enum javax.enterprise.inject.spi.SessionBeanType
fld public final static javax.enterprise.inject.spi.SessionBeanType SINGLETON
fld public final static javax.enterprise.inject.spi.SessionBeanType STATEFUL
fld public final static javax.enterprise.inject.spi.SessionBeanType STATELESS
meth public static javax.enterprise.inject.spi.SessionBeanType valueOf(java.lang.String)
meth public static javax.enterprise.inject.spi.SessionBeanType[] values()
supr java.lang.Enum<javax.enterprise.inject.spi.SessionBeanType>

CLSS public abstract interface !annotation javax.enterprise.inject.spi.WithAnnotations
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation>[] value()

CLSS public abstract javax.enterprise.util.AnnotationLiteral<%0 extends java.lang.annotation.Annotation>
cons protected AnnotationLiteral()
intf java.io.Serializable
intf java.lang.annotation.Annotation
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public java.lang.String toString()
supr java.lang.Object
hfds annotationType,cachedHashCode,members,serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.util.Nonbinding
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract javax.enterprise.util.TypeLiteral<%0 extends java.lang.Object>
cons protected TypeLiteral()
intf java.io.Serializable
meth public boolean equals(java.lang.Object)
meth public final java.lang.Class<{javax.enterprise.util.TypeLiteral%0}> getRawType()
meth public final java.lang.reflect.Type getType()
meth public int hashCode()
meth public java.lang.String toString()
supr java.lang.Object
hfds actualType,serialVersionUID

CLSS public abstract interface !annotation javax.inject.Named
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 javax.inject.Qualifier()
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String value()

CLSS public abstract interface javax.inject.Provider<%0 extends java.lang.Object>
meth public abstract {javax.inject.Provider%0} get()

CLSS public abstract interface !annotation javax.inject.Qualifier
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.inject.Scope
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

