package org.jboss.jsr299.tck.tests.extensions.alternative.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.jsr299.tck.literals.InjectLiteral;

public class GroceryWrapper extends AnnotatedTypeWrapper<Grocery>
{
   private final Set<Type> typeClosure = new HashSet<Type>();
   private static boolean getBaseTypeOfFruitFieldUsed = false;
   private static boolean getTypeClosureUsed = false;

   public GroceryWrapper(AnnotatedType<Grocery> delegate)
   {
      super(delegate, false, new AnnotationLiteral<RequestScoped>()
      {
      }, new CheapLiteral(), new AnnotationLiteral<NamedStereotype>()
      {
      }, new AnnotationLiteral<GroceryInterceptorBinding>(){});
      typeClosure.add(Grocery.class);
      typeClosure.add(Object.class);
   }

   @Override
   public Set<Type> getTypeClosure()
   {
      getTypeClosureUsed = true;
      return typeClosure;
   }

   @Override
   public Set<AnnotatedConstructor<Grocery>> getConstructors()
   {
      Set<AnnotatedConstructor<Grocery>> constructors = new HashSet<AnnotatedConstructor<Grocery>>();
      for (AnnotatedConstructor<Grocery> constructor : super.getConstructors())
      {
         if (constructor.getParameters().size() == 1)
         {
            constructors.add(wrapConstructor(constructor, new InjectLiteral()));
         }
         else
         {
            constructors.add(constructor);
         }
      }
      return constructors;
   }

   @Override
   public Set<AnnotatedField<? super Grocery>> getFields()
   {
      Set<AnnotatedField<? super Grocery>> fields = new HashSet<AnnotatedField<? super Grocery>>();
      for (AnnotatedField<? super Grocery> field : super.getFields())
      {
         if (field.getBaseType().equals(Vegetables.class))
         {
            fields.add(wrapField(field, new InjectLiteral()));
         }
         else
            if (field.getJavaMember().getName().equals("fruit"))
         {
            fields.add(wrapFruitField(field, new CheapLiteral()));
         }
         else if (field.getBaseType().equals(Bread.class))
         {
            fields.add(wrapField(field, new AnnotationLiteral<Produces>()
            {
            }));
         }
         else
         {
            fields.add(field);
         }
      }
      return fields;
   }

   @Override
   public Set<AnnotatedMethod<? super Grocery>> getMethods()
   {
      Set<AnnotatedMethod<? super Grocery>> methods = new HashSet<AnnotatedMethod<? super Grocery>>();
      for (AnnotatedMethod<? super Grocery> method : super.getMethods())
      {
         if (method.getJavaMember().getName().equals("getMilk"))
         {
            methods.add(wrapMethod(method, false, new AnnotationLiteral<Produces>()
            {
            }));
         }
         else if (method.getJavaMember().getName().equals("getYogurt"))
         {
            // wrap the method and its parameters
            AnnotatedMethod<? super Grocery> wrappedMethod = wrapMethod(method, false, new ExpensiveLiteral(), new AnnotationLiteral<Produces>()
            {
            });
            methods.add(wrapMethodParameters(wrappedMethod, false, new Annotation[] {new CheapLiteral()}));
         }
         else if (method.getJavaMember().getName().equals("nonInjectAnnotatedInitializer"))
         {
            methods.add(wrapMethod(method, false, new InjectLiteral()));
         }
         else if (method.getJavaMember().getName().equals("initializer"))
         {
            methods.add(wrapMethodParameters(method, false, new Annotation[] { new CheapLiteral() }));
         }
         else if (method.getJavaMember().getName().equals("observer1"))
         {
            Annotation[] firstParameterAnnotations = new Annotation[] { new Observes()
            {

               public TransactionPhase during()
               {
                  return TransactionPhase.IN_PROGRESS;
               }

               public Reception notifyObserver()
               {
                  return Reception.ALWAYS;
               }

               public Class<? extends Annotation> annotationType()
               {
                  return Observes.class;
               }

            } };
            Annotation[] secondParameterAnnotations = new Annotation[] { new CheapLiteral() };
            methods.add(wrapMethodParameters(method, false, firstParameterAnnotations, secondParameterAnnotations));
         }
         else if (method.getJavaMember().getName().equals("observer2"))
         {
            methods.add(wrapMethodParameters(method, true, new Annotation[] { new ExpensiveLiteral() }));
         }
         else
         {
            methods.add(method);
         }
      }
      return methods;
   }

   private <Y> AnnotatedConstructor<Y> wrapConstructor(AnnotatedConstructor<Y> delegate, Annotation... annotations)
   {
      return new AnnotatedConstructorWrapper<Y>(delegate, false, annotations);
   }

   private <Y> AnnotatedField<Y> wrapField(AnnotatedField<Y> delegate, Annotation... annotations)
   {
      return new AnnotatedFieldWrapper<Y>(delegate, false, annotations);
   }

   private <Y> AnnotatedField<Y> wrapFruitField(AnnotatedField<Y> delegate, Annotation... annotations)
   {
      return new AnnotatedFieldWrapper<Y>(delegate, true, annotations)
      {
         @Override
         public Type getBaseType()
         {
            getBaseTypeOfFruitFieldUsed = true;
            return TropicalFruit.class;
         }

         @SuppressWarnings("serial")
         @Override
         public Set<Type> getTypeClosure()
         {
            return new HashSet<Type>() {
               {
                  add(Object.class);
                  add(Fruit.class);
                  add(TropicalFruit.class);
               }
            };
         }
         
         
      };
   }

   private <Y> AnnotatedMethodWrapper<Y> wrapMethod(AnnotatedMethod<Y> delegate, boolean keepOriginalAnnotations, Annotation... annotations)
   {
      return new AnnotatedMethodWrapper<Y>(delegate, keepOriginalAnnotations, annotations);
   }

   /**
    * This method allows you to add a set of Annotations to every method
    * parameter. Note that the method will remove all method-level annotations.
    */
   private <Y> AnnotatedMethodWrapper<Y> wrapMethodParameters(AnnotatedMethod<Y> delegate, final boolean keepOriginalAnnotations, final Annotation[]... annotations)
   {
      return new AnnotatedMethodWrapper<Y>(delegate, true)
      {
         @Override
         public List<AnnotatedParameter<Y>> getParameters()
         {
            List<AnnotatedParameter<Y>> parameters = new ArrayList<AnnotatedParameter<Y>>();
            for (AnnotatedParameter<Y> parameter : super.getParameters())
            {
               parameters.add(new AnnotatedParameterWrapper<Y>(parameter, keepOriginalAnnotations, annotations[parameter.getPosition()]));
            }
            return parameters;
         }
      };
   }

   public static boolean isGetBaseTypeOfFruitFieldUsed()
   {
      return getBaseTypeOfFruitFieldUsed;
   }

   public static boolean isGetTypeClosureUsed()
   {
      return getTypeClosureUsed;
   }
}
