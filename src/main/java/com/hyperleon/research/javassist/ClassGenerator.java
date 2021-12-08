package com.hyperleon.research.javassist;

import com.hyperleon.research.javassist.template.Bar;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;

/**
 * @author leon
 * @date 2021-12-08 19:26
 **/
public class ClassGenerator {

    public static void main(String[] args) throws Exception {
        //classPool
        ClassPool classPool = ClassPool.getDefault();

        CtClass interfaceCtClass = classPool.get("com.hyperleon.research.javassist.template.Bar");

        //ctClass
        CtClass ctClass = classPool.makeClass("CoolBar");

        //ctField
        CtField meta = new CtField(classPool.get("java.lang.String"), "meta", ctClass);
        meta.setModifiers(Modifier.PRIVATE);
        ctClass.addField(meta);

        //ctMethod
        CtMethod obtain = new CtMethod(
                classPool.get("java.lang.String"),
                "obtainAnything",
                new CtClass[]{classPool.get("java.util.function.Supplier")},
                ctClass);

        obtain.setBody("{return (String)$1.get();}");
        ctClass.addMethod(obtain);

        //set interfaces
        ctClass.setInterfaces(new CtClass[]{interfaceCtClass});

        //write class outputStream to file
        ctClass.writeFile();

        Bar bar = (Bar)ctClass.toClass().newInstance();
        System.out.println(bar.obtainAnything(() -> "javassist rule"));

    }
}
