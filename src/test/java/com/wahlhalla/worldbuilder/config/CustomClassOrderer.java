package com.wahlhalla.worldbuilder.config;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

public class CustomClassOrderer implements ClassOrderer {

  @Override
  public void orderClasses(ClassOrdererContext classOrdererContext) {
    classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(classDescriptor ->
    {
      if (classDescriptor.getTestClass().getName().contains("Auth")) {
        return 0;
      } else return 1;
    }));
  }
}