/**
 * Copyright (c) 2015 See AUTHORS file
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the mini2Dx nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.mini2Dx.ios.di;

import org.mini2Dx.core.di.ComponentScanner;
import org.mini2Dx.core.di.annotation.Prototype;
import org.mini2Dx.core.di.annotation.Singleton;
import org.mini2Dx.gdx.utils.Array;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Scanner;
import java.util.Set;

/**
 * iOS implementation of {@link ComponentScanner}
 * 
 * @author Thomas Cashman
 */
public class IOSComponentScanner implements ComponentScanner {
    private Array<Class<?>> singletonClasses;
    private Array<Class<?>> prototypeClasses;

    /**
     * Constructor
     */
    public IOSComponentScanner() {
        singletonClasses = new Array<Class<?>>();
        prototypeClasses = new Array<Class<?>>();
    }

    /**
     * Scans multiple packages recursively for {@link Singleton} and
     * {@link Prototype} annotated classes
     * 
     * @param packageNames
     *            The package name to scan through, e.g. org.mini2Dx.component
     * @throws IOException
     */
    public void scan(String[] packageNames) throws IOException {
        for (String packageName : packageNames) {
            scan(packageName);
        }
    }

    /**
     * Scans a package recursively for {@link Singleton} and {@link Prototype}
     * annotated classes
     * 
     * @param packageName
     *            The package name to scan through, e.g. org.mini2Dx.component
     * @throws IOException
     */
    private void scan(String packageName) throws IOException {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> singletons = reflections
                .getTypesAnnotatedWith(Singleton.class);
        for(Class<?> clazz : singletons) {
            singletonClasses.add(clazz);
        }

        Set<Class<?>> prototypes = reflections
                .getTypesAnnotatedWith(Prototype.class);
        for(Class<?> clazz : prototypes) {
            prototypeClasses.addAll(clazz);
        }
    }

    @Override
    public void saveTo(Writer writer) {
        final PrintWriter printWriter = new PrintWriter(writer);

        printWriter.println("--- Singletons ---");
        for(int i = 0; i < singletonClasses.size; i++) {
            printWriter.println(singletonClasses.get(i).getName());
        }
        printWriter.println("--- Prototypes ---");
        for(int i = 0; i < prototypeClasses.size; i++) {
            printWriter.println(prototypeClasses.get(i).getName());
        }

        printWriter.flush();
        printWriter.close();
    }

    @Override
    public void restoreFrom(Reader reader) throws ClassNotFoundException {
        final Scanner scanner = new Scanner(reader);
        boolean singletons = true;

        scanner.nextLine();
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            if(line.startsWith("---")) {
                singletons = false;
            } else if(singletons) {
                singletonClasses.add(Class.forName(line));
            } else {
                prototypeClasses.add(Class.forName(line));
            }
        }
        scanner.close();
    }

    public Array<Class<?>> getSingletonClasses() {
        return singletonClasses;
    }

    public Array<Class<?>> getPrototypeClasses() {
        return prototypeClasses;
    }
}
