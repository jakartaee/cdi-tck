package org.jboss.jsr299.tck.tests.extensions.annotated;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.AnnotationLiteral;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedParameter;
import javax.enterprise.inject.spi.AnnotatedType;

import org.jboss.jsr299.tck.literals.InjectLiteral;

public class GroceryAnnotatedType<X> extends TestAnnotatedType<X>
{
   private final Set<Type> typeClosure = new HashSet<Type>();
   private static boolean getBaseTypeOfFruitFieldUsed = false;
   private static boolean getTypeClosureUsed = false;

   public GroceryAnnotatedType(AnnotatedType<X> delegate)
   {
      super(delegate, new AnnotationLiteral<RequestScoped>()
      {
      }, new CheapLiteral(), new AnnotationLiteral<NamedStereotype>()
      {
      }, new AnnotationLiteral<GroceryInterceptorBinding>()
      {
      });
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
   public Set<AnnotatedConstructor<X>> getConstructors()
   {
      Set<AnnotatedConstructor<X>> constructors = new HashSet<AnnotatedConstructor<X>>();
      for (AnnotatedConstructor<X> constructor : super.getConstructors())
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
   public Set<AnnotatedField<? super X>> getFields()
   {
      Set<AnnotatedField<? super X>> fields = new HashSet<AnnotatedField<? super X>>();
      for (AnnotatedField<? super X> field : super.getFields())
      {
         if (field.getBaseType().equals(Vegetables.class))
         {
            fields.add(wrapField(field, new InjectLiteral()));
         }
         else if (field.getJavaMember().getName().equals("fruit"))
         {
            fields.add(wrapFruitField(field, new CheapLiteral(), new InjectLiteral()));
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
   public Set<AnnotatedMethod<? super X>> getMethods()
   {
      Set<AnnotatedMethod<? super X>> methods = new HashSet<AnnotatedMethod<? super X>>();
      for (AnnotatedMethod<? super X> method : super.getMethods())
      {
         if (method.getJavaMember().getName().equals("getMilk"))
         {
            methods.add(wrapMethod(method, new AnnotationLiteral<Produces>()
            {
            }));
         }
         else if (method.getJavaMember().getName().equals("getYogurt"))
         {
            methods.add(wrapMethodAndAnnotateEveryParameterWithCheap(method, new ExpensiveLiteral(), new AnnotationLiteral<Produces>()
            {
            }));
         }
         else if (method.getJavaMember().getName().equals("nonInjectAnnotatedInitializer"))
         {
            methods.add(wrapMethod(method, new InjectLiteral()));
         }
         else if (method.getJavaMember().getName().equals("initializer"))
         {
            methods.add(wrapMethodAndAnnotateEveryParameterWithCheap(method, new InjectLiteral()));
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
      return new TestAnnotatedConstructor<Y>(delegate, annotations);
   }

   private <Y> AnnotatedField<Y> wrapField(AnnotatedField<Y> delegate, Annotation... annotations)
   {
      return new TestAnnotatedField<Y>(delegate, annotations);
   }
   
   private <Y> AnnotatedField<Y> wrapFruitField(AnnotatedField<Y> delegate, Annotation... annotations)
   {
      return new TestAnnotatedField<Y>(delegate, annotations){
         @Override
         public Type getBaseType()
         {
            getBaseTypeOfFruitFieldUsed = true;
            return TropicalFruit.class;
         }
      };
   }

   private <Y> TestAnnotatedMethod<Y> wrapMethod(AnnotatedMethod<Y> delegate, Annotation... annotations)
   {
      return new TestAnnotatedMethod<Y>(delegate, annotations);
   }

   private <Y> TestAnnotatedMethod<Y> wrapMethodAndAnnotateEveryParameterWithCheap(AnnotatedMethod<Y> delegate, Annotation... annotations)
   {
      return new TestAnnotatedMethod<Y>(delegate, annotations)
      {
         @Override
         public List<AnnotatedParameter<Y>> getParameters()
         {
            List<AnnotatedParameter<Y>> parameters = new ArrayList<AnnotatedParameter<Y>>();
            for (AnnotatedParameter<Y> parameter : super.getParameters())
            {
               parameters.add(new TestAnnotatedParameter<Y>(parameter, new CheapLiteral()));
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
