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
cons public RuntimeException()
cons public RuntimeException(java.lang.String)
cons public RuntimeException(java.lang.String,java.lang.Throwable)
cons public RuntimeException(java.lang.Throwable)
supr java.lang.Exception
hfds serialVersionUID

CLSS public java.lang.Throwable
cons public Throwable()
cons public Throwable(java.lang.String)
cons public Throwable(java.lang.String,java.lang.Throwable)
cons public Throwable(java.lang.Throwable)
intf java.io.Serializable
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
hfds backtrace,cause,detailMessage,serialVersionUID,stackTrace

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

CLSS public abstract interface !annotation javax.decorator.Decorates
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.decorator.Decorator
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
 anno 0 javax.enterprise.inject.Stereotype(java.lang.Class<? extends java.lang.annotation.Annotation>[] supportedScopes=[], java.lang.Class<?>[] requiredTypes=[])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.ApplicationScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.ScopeType(boolean normal=true, boolean passivating=false)
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.context.ContextNotActiveException
cons public ContextNotActiveException()
cons public ContextNotActiveException(java.lang.String)
cons public ContextNotActiveException(java.lang.String,java.lang.Throwable)
cons public ContextNotActiveException(java.lang.Throwable)
supr javax.inject.ExecutionException
hfds serialVersionUID

CLSS public abstract interface javax.enterprise.context.Conversation
meth public abstract boolean isLongRunning()
meth public abstract java.lang.String getId()
meth public abstract long getTimeout()
meth public abstract void begin()
meth public abstract void begin(java.lang.String)
meth public abstract void end()
meth public abstract void setTimeout(long)

CLSS public abstract interface !annotation javax.enterprise.context.ConversationScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.ScopeType(boolean normal=true, boolean passivating=true)
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.Dependent
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, TYPE, FIELD])
 anno 0 javax.enterprise.context.ScopeType(boolean normal=false, boolean passivating=false)
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.RequestScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.ScopeType(boolean normal=true, boolean passivating=false)
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.context.ScopeType
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault boolean normal()
meth public abstract !hasdefault boolean passivating()

CLSS public abstract interface !annotation javax.enterprise.context.SessionScoped
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Inherited()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.ScopeType(boolean normal=true, boolean passivating=true)
intf java.lang.annotation.Annotation

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

CLSS public javax.enterprise.inject.AmbiguousResolutionException
cons public AmbiguousResolutionException()
cons public AmbiguousResolutionException(java.lang.String)
cons public AmbiguousResolutionException(java.lang.String,java.lang.Throwable)
cons public AmbiguousResolutionException(java.lang.Throwable)
supr javax.inject.DeploymentException
hfds serialVersionUID

CLSS public abstract javax.enterprise.inject.AnnotationLiteral<%0 extends java.lang.annotation.Annotation>
cons protected AnnotationLiteral()
intf java.lang.annotation.Annotation
meth public boolean equals(java.lang.Object)
meth public int hashCode()
meth public java.lang.Class<? extends java.lang.annotation.Annotation> annotationType()
meth public java.lang.String toString()
supr java.lang.Object
hfds annotationType,members

CLSS public abstract interface !annotation javax.enterprise.inject.Any
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD, PARAMETER])
 anno 0 javax.enterprise.inject.BindingType()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.BindingType
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public javax.enterprise.inject.CreationException
cons public CreationException()
cons public CreationException(java.lang.String)
cons public CreationException(java.lang.String,java.lang.Throwable)
cons public CreationException(java.lang.Throwable)
supr javax.inject.ExecutionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.Current
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, PARAMETER, FIELD])
 anno 0 javax.enterprise.inject.BindingType()
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
supr javax.inject.ExecutionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.Initializer
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, CONSTRUCTOR])
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.enterprise.inject.Instance<%0 extends java.lang.Object>
intf java.lang.Iterable<{javax.enterprise.inject.Instance%0}>
meth public abstract !varargs {javax.enterprise.inject.Instance%0} get(java.lang.annotation.Annotation[])

CLSS public abstract interface !annotation javax.enterprise.inject.Model
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.context.RequestScoped()
 anno 0 javax.enterprise.inject.Named(java.lang.String value="")
 anno 0 javax.enterprise.inject.Stereotype(java.lang.Class<? extends java.lang.annotation.Annotation>[] supportedScopes=[], java.lang.Class<?>[] requiredTypes=[])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Named
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.String value()

CLSS public abstract interface !annotation javax.enterprise.inject.New
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[FIELD, PARAMETER])
 anno 0 javax.enterprise.inject.BindingType()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.NonBinding
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Produces
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[METHOD, FIELD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.Stereotype
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation
meth public abstract !hasdefault java.lang.Class<? extends java.lang.annotation.Annotation>[] supportedScopes()
meth public abstract !hasdefault java.lang.Class<?>[] requiredTypes()

CLSS public abstract javax.enterprise.inject.TypeLiteral<%0 extends java.lang.Object>
cons protected TypeLiteral()
meth public final java.lang.Class<{javax.enterprise.inject.TypeLiteral%0}> getRawType()
meth public final java.lang.reflect.Type getType()
supr java.lang.Object
hfds actualType

CLSS public javax.enterprise.inject.UnproxyableResolutionException
cons public UnproxyableResolutionException()
cons public UnproxyableResolutionException(java.lang.String)
cons public UnproxyableResolutionException(java.lang.String,java.lang.Throwable)
cons public UnproxyableResolutionException(java.lang.Throwable)
supr javax.inject.DeploymentException
hfds serialVersionUID

CLSS public javax.enterprise.inject.UnsatisfiedResolutionException
cons public UnsatisfiedResolutionException()
cons public UnsatisfiedResolutionException(java.lang.String)
cons public UnsatisfiedResolutionException(java.lang.String,java.lang.Throwable)
cons public UnsatisfiedResolutionException(java.lang.Throwable)
supr javax.inject.DeploymentException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.enterprise.inject.deployment.DeploymentType
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.deployment.Production
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.inject.deployment.DeploymentType()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.deployment.Specializes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.enterprise.inject.deployment.Standard
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD])
 anno 0 javax.enterprise.inject.deployment.DeploymentType()
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.enterprise.inject.spi.AfterBeanDiscovery
meth public abstract void addDefinitionError(java.lang.Throwable)

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
meth public abstract boolean isNullable()
meth public abstract boolean isSerializable()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> getDeploymentType()
meth public abstract java.lang.Class<? extends java.lang.annotation.Annotation> getScope()
meth public abstract java.lang.String getName()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()
meth public abstract java.util.Set<java.lang.reflect.Type> getTypes()
meth public abstract java.util.Set<javax.enterprise.inject.spi.InjectionPoint> getInjectionPoints()

CLSS public abstract interface javax.enterprise.inject.spi.BeanManager
meth public abstract !varargs <%0 extends java.lang.Object> java.util.Set<javax.event.Observer<{%%0}>> resolveObservers({%%0},java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.List<javax.enterprise.inject.spi.Decorator<?>> resolveDecorators(java.util.Set<java.lang.reflect.Type>,java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.List<javax.enterprise.inject.spi.Interceptor<?>> resolveInterceptors(javax.enterprise.inject.spi.InterceptionType,java.lang.annotation.Annotation[])
meth public abstract !varargs java.util.Set<javax.enterprise.inject.spi.Bean<?>> getBeans(java.lang.reflect.Type,java.lang.annotation.Annotation[])
meth public abstract !varargs void addObserver(javax.event.Observer<?>,java.lang.annotation.Annotation[])
meth public abstract !varargs void fireEvent(java.lang.Object,java.lang.annotation.Annotation[])
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Bean<? extends {%%0}> getHighestPrecedenceBean(java.util.Set<javax.enterprise.inject.spi.Bean<? extends {%%0}>>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.Bean<? extends {%%0}> getMostSpecializedBean(javax.enterprise.inject.spi.Bean<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.InjectionTarget<{%%0}> createInjectionTarget(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.InjectionTarget<{%%0}> createInjectionTarget(javax.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.ManagedBean<{%%0}> createManagedBean(java.lang.Class<{%%0}>)
meth public abstract <%0 extends java.lang.Object> javax.enterprise.inject.spi.ManagedBean<{%%0}> createManagedBean(javax.enterprise.inject.spi.AnnotatedType<{%%0}>)
meth public abstract boolean isBindingType(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isInterceptorBindingType(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isScopeType(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract boolean isStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.lang.Object getInjectableReference(javax.enterprise.inject.spi.InjectionPoint,javax.enterprise.context.spi.CreationalContext<?>)
meth public abstract java.lang.Object getReference(javax.enterprise.inject.spi.Bean<?>,java.lang.reflect.Type)
meth public abstract java.util.List<java.lang.Class<? extends java.lang.annotation.Annotation>> getEnabledDeploymentTypes()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getInterceptorBindingTypeDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.util.Set<java.lang.annotation.Annotation> getStereotypeDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract java.util.Set<javax.enterprise.inject.spi.Bean<?>> getBeans(java.lang.String)
meth public abstract javax.el.ELResolver getELResolver()
meth public abstract javax.enterprise.context.ScopeType getScopeDefinition(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract javax.enterprise.context.spi.Context getContext(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract javax.enterprise.inject.spi.Bean<?> getPassivationCapableBean(java.lang.String)
meth public abstract javax.enterprise.inject.spi.BeanManager createActivity()
meth public abstract javax.enterprise.inject.spi.BeanManager setCurrent(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void addBean(javax.enterprise.inject.spi.Bean<?>)
meth public abstract void addContext(javax.enterprise.context.spi.Context)
meth public abstract void addObserver(javax.enterprise.inject.spi.ObserverMethod<?,?>)
meth public abstract void removeObserver(javax.event.Observer<?>)
meth public abstract void validate(javax.enterprise.inject.spi.InjectionPoint)

CLSS public abstract interface javax.enterprise.inject.spi.BeforeBeanDiscovery
meth public abstract !varargs void addStereotype(java.lang.Class<? extends java.lang.annotation.Annotation>,java.lang.annotation.Annotation[])
meth public abstract void addBindingType(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void addInterceptorBindingType(java.lang.Class<? extends java.lang.annotation.Annotation>)
meth public abstract void addScopeType(java.lang.Class<? extends java.lang.annotation.Annotation>,boolean,boolean)

CLSS public abstract interface javax.enterprise.inject.spi.Decorator<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.Decorator%0}>
meth public abstract java.lang.reflect.Type getDelegateType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getDelegateBindings()
meth public abstract java.util.Set<java.lang.reflect.Type> getDecoratedTypes()

CLSS public abstract interface javax.enterprise.inject.spi.Extension

CLSS public abstract interface javax.enterprise.inject.spi.InjectionPoint
meth public abstract boolean isDelegate()
meth public abstract boolean isTransient()
meth public abstract java.lang.reflect.Member getMember()
meth public abstract java.lang.reflect.Type getType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getQualifiers()
meth public abstract javax.enterprise.inject.spi.Annotated getAnnotated()
meth public abstract javax.enterprise.inject.spi.Bean<?> getBean()

CLSS public abstract interface javax.enterprise.inject.spi.InjectionTarget<%0 extends java.lang.Object>
meth public abstract java.util.Set<javax.enterprise.inject.spi.InjectionPoint> getInjectionPoints()
meth public abstract void destroy({javax.enterprise.inject.spi.InjectionTarget%0})
meth public abstract void dispose({javax.enterprise.inject.spi.InjectionTarget%0})
meth public abstract void inject({javax.enterprise.inject.spi.InjectionTarget%0},javax.enterprise.context.spi.CreationalContext<{javax.enterprise.inject.spi.InjectionTarget%0}>)
meth public abstract {javax.enterprise.inject.spi.InjectionTarget%0} produce(javax.enterprise.context.spi.CreationalContext<{javax.enterprise.inject.spi.InjectionTarget%0}>)

CLSS public final !enum javax.enterprise.inject.spi.InterceptionType
fld public final static javax.enterprise.inject.spi.InterceptionType AROUND_INVOKE
fld public final static javax.enterprise.inject.spi.InterceptionType POST_ACTIVATE
fld public final static javax.enterprise.inject.spi.InterceptionType POST_CONSTRUCT
fld public final static javax.enterprise.inject.spi.InterceptionType PRE_DESTROY
fld public final static javax.enterprise.inject.spi.InterceptionType PRE_PASSIVATE
meth public static javax.enterprise.inject.spi.InterceptionType valueOf(java.lang.String)
meth public static javax.enterprise.inject.spi.InterceptionType[] values()
supr java.lang.Enum<javax.enterprise.inject.spi.InterceptionType>

CLSS public abstract interface javax.enterprise.inject.spi.Interceptor<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.Interceptor%0}>
meth public abstract java.lang.reflect.Method getMethod(javax.enterprise.inject.spi.InterceptionType)
meth public abstract java.util.Set<java.lang.annotation.Annotation> getInterceptorBindingTypes()

CLSS public abstract interface javax.enterprise.inject.spi.ManagedBean<%0 extends java.lang.Object>
intf javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ManagedBean%0}>
meth public abstract java.util.Set<javax.enterprise.inject.spi.ObserverMethod<{javax.enterprise.inject.spi.ManagedBean%0},?>> getObserverMethods()
meth public abstract java.util.Set<javax.enterprise.inject.spi.ProducerBean<{javax.enterprise.inject.spi.ManagedBean%0},?>> getProducerBeans()
meth public abstract javax.enterprise.inject.spi.AnnotatedType<{javax.enterprise.inject.spi.ManagedBean%0}> getAnnotatedType()
meth public abstract javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ManagedBean%0}> getBeanClass()
meth public abstract javax.enterprise.inject.spi.InjectionTarget<{javax.enterprise.inject.spi.ManagedBean%0}> getInjectionTarget()

CLSS public abstract interface javax.enterprise.inject.spi.ObserverMethod<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf javax.event.Observer<{javax.enterprise.inject.spi.ObserverMethod%1}>
meth public abstract java.lang.reflect.Type getObservedEventType()
meth public abstract java.util.Set<java.lang.annotation.Annotation> getObservedEventBindings()
meth public abstract java.util.Set<javax.enterprise.inject.spi.InjectionPoint> getInjectionPoints()
meth public abstract javax.enterprise.inject.spi.AnnotatedMethod<? super {javax.enterprise.inject.spi.ObserverMethod%0}> getAnnotatedMethod()
meth public abstract javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ObserverMethod%0}> getBean()
meth public abstract void notify({javax.enterprise.inject.spi.ObserverMethod%0},{javax.enterprise.inject.spi.ObserverMethod%1})

CLSS public abstract interface javax.enterprise.inject.spi.ProducerBean<%0 extends java.lang.Object, %1 extends java.lang.Object>
intf javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ProducerBean%1}>
meth public abstract javax.enterprise.inject.spi.AnnotatedMember<? super {javax.enterprise.inject.spi.ProducerBean%0}> getAnnotatedProducer()
meth public abstract javax.enterprise.inject.spi.AnnotatedMethod<? super {javax.enterprise.inject.spi.ProducerBean%0}> getAnnotatedDisposer()
meth public abstract javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ProducerBean%0}> getBean()
meth public abstract javax.enterprise.inject.spi.Bean<{javax.enterprise.inject.spi.ProducerBean%0}> getBeanClass()
meth public abstract javax.enterprise.inject.spi.InjectionTarget<{javax.enterprise.inject.spi.ProducerBean%1}> getInjectionTarget()

CLSS public abstract interface !annotation javax.event.AfterTransactionCompletion
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.event.AfterTransactionFailure
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.event.AfterTransactionSuccess
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.event.Asynchronously
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.event.BeforeTransactionCompletion
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.event.Event<%0 extends java.lang.Object>
meth public abstract !varargs void fire({javax.event.Event%0},java.lang.annotation.Annotation[])
meth public abstract !varargs void observe(javax.event.Observer<{javax.event.Event%0}>,java.lang.annotation.Annotation[])

CLSS public abstract interface !annotation javax.event.IfExists
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public abstract interface javax.event.Observer<%0 extends java.lang.Object>
meth public abstract boolean notify({javax.event.Observer%0})

CLSS public javax.event.ObserverException
cons public ObserverException()
cons public ObserverException(java.lang.String)
cons public ObserverException(java.lang.String,java.lang.Throwable)
cons public ObserverException(java.lang.Throwable)
supr javax.inject.ExecutionException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.event.Observes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[PARAMETER])
intf java.lang.annotation.Annotation

CLSS public javax.inject.DefinitionException
cons public DefinitionException()
cons public DefinitionException(java.lang.String)
cons public DefinitionException(java.lang.String,java.lang.Throwable)
cons public DefinitionException(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public javax.inject.DeploymentException
cons public DeploymentException()
cons public DeploymentException(java.lang.String)
cons public DeploymentException(java.lang.String,java.lang.Throwable)
cons public DeploymentException(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public javax.inject.DuplicateBindingTypeException
cons public DuplicateBindingTypeException()
cons public DuplicateBindingTypeException(java.lang.String)
cons public DuplicateBindingTypeException(java.lang.String,java.lang.Throwable)
cons public DuplicateBindingTypeException(java.lang.Throwable)
supr javax.inject.ExecutionException
hfds serialVersionUID

CLSS public javax.inject.ExecutionException
cons public ExecutionException()
cons public ExecutionException(java.lang.String)
cons public ExecutionException(java.lang.String,java.lang.Throwable)
cons public ExecutionException(java.lang.Throwable)
supr java.lang.RuntimeException
hfds serialVersionUID

CLSS public javax.inject.InconsistentSpecializationException
cons public InconsistentSpecializationException()
cons public InconsistentSpecializationException(java.lang.String)
cons public InconsistentSpecializationException(java.lang.String,java.lang.Throwable)
cons public InconsistentSpecializationException(java.lang.Throwable)
supr javax.inject.DeploymentException
hfds serialVersionUID

CLSS public javax.inject.NullableDependencyException
cons public NullableDependencyException()
cons public NullableDependencyException(java.lang.String)
cons public NullableDependencyException(java.lang.String,java.lang.Throwable)
cons public NullableDependencyException(java.lang.Throwable)
supr javax.inject.DeploymentException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.inject.Obtains
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE, METHOD, FIELD, PARAMETER])
 anno 0 javax.enterprise.inject.BindingType()
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.inject.Realizes
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
intf java.lang.annotation.Annotation

CLSS public javax.inject.UnserializableDependencyException
cons public UnserializableDependencyException()
cons public UnserializableDependencyException(java.lang.String)
cons public UnserializableDependencyException(java.lang.String,java.lang.Throwable)
cons public UnserializableDependencyException(java.lang.Throwable)
supr javax.inject.DeploymentException
hfds serialVersionUID

CLSS public abstract interface !annotation javax.interceptor.Interceptor
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[TYPE])
 anno 0 javax.enterprise.inject.Stereotype(java.lang.Class<? extends java.lang.annotation.Annotation>[] supportedScopes=[], java.lang.Class<?>[] requiredTypes=[])
intf java.lang.annotation.Annotation

CLSS public abstract interface !annotation javax.interceptor.InterceptorBindingType
 anno 0 java.lang.annotation.Documented()
 anno 0 java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy value=RUNTIME)
 anno 0 java.lang.annotation.Target(java.lang.annotation.ElementType[] value=[ANNOTATION_TYPE])
intf java.lang.annotation.Annotation

