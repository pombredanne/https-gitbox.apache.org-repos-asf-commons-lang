/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.commons.lang.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
/**
 * <p><code>HashCode</code> generation routines.</p>
 *
 * <p> This class enables a good hashcode to be built for any class. It
 * follows the rules laid out in the book
 * <a href="http://java.sun.com/docs/books/effective/index.html">Effective Java</a>
 * , by Joshua Bloch. Writing a good <code>hashCode</code> is actually quite
 * difficult. This class aims to simplify the process.</p>
 *
 * <p> All relevant fields from the object should be included in the
 * <code>hashCode</code>. Derived fields may be excluded. In general, any
 * field used in the equals method must be used in the <code>hashCode</code>
 * method.</p>
 *
 * <p>To use this class write code as follows:</p>
 * <pre>
 * public class Person {
 *   String name;
 *   int age;
 *   boolean isSmoker;
 *   ...
 * 
 *   public int hashCode() {
 *     // you pick a hard-coded, randomly chosen, non-zero, odd number
 *     // ideally different for each class
 *     return new HashCodeBuilder(17, 37).   
 *       append(name).
 *       append(age).
 *       append(smoker).
 *       toHashCode();
 *   }
 * }
 * </pre>
 *
 * <p>Alternatively, there is a method that uses reflection to determine
 * the fields to test. Because these fields are usually private, the method, 
 * <code>reflectionHashCode</code>, uses <code>Field.setAccessible</code> to
 * change the visibility of the fields. This will fail under a security manager, 
 * unless the appropriate permissions are set up correctly. It is also slower
 * than testing explicitly.</p>
 *
 * <p>A typical invocation for this method would look like:</p>
 * <pre>
 * public boolean hashCode(Object o) {
 *   return HashCodeBuilder.reflectionHashCode(this);
 * }
 * </pre>
 * 
 * @author <a href="mailto:scolebourne@joda.org">Stephen Colebourne</a>
 * @version $Id: HashCodeBuilder.java,v 1.5 2002/11/17 21:46:42 scolebourne Exp $
 */
public class HashCodeBuilder {
    
    /**
     * Constant to use in building the hashCode
     */
    private final int iConstant;
    /**
     * Running total of the hashCode
     */
    private int iTotal = 0;
    
    /**
     * <p>Constructor for HashCodeBuilder.</p>
     *
     * <p>This constructor uses two hard coded choices for the constants
     * needed to build a <code>hashCode</code>.</p>
     */
    public HashCodeBuilder() {
        super();
        iConstant = 37;
        iTotal = 17;
    }
    
    /**
     * <p>Constructor for <code>HashCodeBuilder</code>.</p>
     *
     * <p>Two randomly chosen, non-zero, odd numbers must be passed in.
     * Ideally these should be different for each class, however this is
     * not vital.</p>
     *
     * <p>Prime numbers are preferred, especially for the multiplier.</p>
     * 
     * @param initialNonZeroOddNumber  a non-zero, odd number used as the initial value
     * @param multiplierNonZeroOddNumber  a non-zero, odd number used as the multiplier
     * @throws IllegalArgumentException if the number is zero or even
     */
    public HashCodeBuilder(int initialNonZeroOddNumber, int multiplierNonZeroOddNumber) {
        super();
        if (initialNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero initial value");
        }
        if (initialNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd initial value");
        }
        if (multiplierNonZeroOddNumber == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires a non zero multiplier");
        }
        if (multiplierNonZeroOddNumber % 2 == 0) {
            throw new IllegalArgumentException("HashCodeBuilder requires an odd multiplier");
        }
        iConstant = multiplierNonZeroOddNumber;
        iTotal = initialNonZeroOddNumber;
    }

    //-------------------------------------------------------------------------
    
    /**
     * <p>This method uses reflection to build a valid hash code.</p>
     *
     * <p>This constructor uses two hard coded choices for the constants
     * needed to build a hash code.</p>
     *
     * <p>It uses <code>Field.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manger, if the permissions are not set up correctly. It is
     * also not as efficient as testing explicitly.</p>
     *
     * <p>Transient members will be not be used, as they are likely derived
     * fields, and not part of the value of the Object.</p>
     *
     * <p>Static fields will not be tested.</p>
     *
     * @param object  the Object to create a <code>hashCode</code> for
     * @return int hash code
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static int reflectionHashCode(Object object) {
        return reflectionHashCode(object, false);
    }

    /**
     * <p>This method uses reflection to build a valid hash code.</p>
     *
     * <p>This constructor uses two hard coded choices for the constants needed
     * to build a hash code.</p>
     *
     * <p> It uses <code>Field.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manger, if the permissions are not set up correctly. It is
     * also not as efficient as testing explicitly.</p>
     *
     * <P>If the TestTransients parameter is set to <code>true</code>, transient
     * members will be tested, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the Object.</p>
     *
     * <p>Static fields will not be tested.</p>
     *
     * @param object  the Object to create a <code>hashCode</code> for
     * @param testTransients  whether to include transient fields
     * @return int hash code
     * @throws IllegalArgumentException if the object is <code>null</code>
     */
    public static int reflectionHashCode(Object object, boolean testTransients) {
        return reflectionHashCode(17, 37, object, testTransients);
    }
        
    /**
     * <p>This method uses reflection to build a valid hash code.</p>
     *
     * <p>It uses <code>Field.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manger, if the permissions are not set up correctly. It is
     * also not as efficient as testing explicitly.</p>
     *
     * <p>Transient members will be not be used, as they are likely derived
     * fields, and not part of the value of the Object.</p>
     *
     * <p>Static fields will not be tested.</p>
     *
     * <p>Two randomly chosen, non-zero, odd numbers must be passed in. Ideally
     * these should be different for each class, however this is not vital.
     * Prime numbers are preferred, especially for the multiplier.</p>
     * 
     * @param initialNonZeroOddNumber  a non-zero, odd number used as the initial value
     * @param multiplierNonZeroOddNumber  a non-zero, odd number used as the multiplier
     * @param object  the Object to create a <code>hashCode</code> for
     * @return int hash code
     * @throws IllegalArgumentException if the Object is <code>null</code>
     * @throws IllegalArgumentException if the number is zero or even
     */
    public static int reflectionHashCode(
            int initialNonZeroOddNumber, int multiplierNonZeroOddNumber, 
            Object object) {
        return reflectionHashCode(initialNonZeroOddNumber, multiplierNonZeroOddNumber, object, false);
    }
    
    /**
     * <p>This method uses reflection to build a valid hash code.</p>
     *
     * <p>It uses <code>Field.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manger, if the permissions are not set up correctly. It is also
     * not as efficient as testing explicitly.</p>
     *
     * <p>If the TestTransients parameter is set to <code>true</code>, transient
     * members will be tested, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the Object.</p>
     *
     * <p>Static fields will not be tested.</p>
     *
     * <p>Two randomly chosen, non-zero, odd numbers must be passed in. Ideally
     * these should be different for each class, however this is not vital.
     * Prime numbers are preferred, especially for the multiplier.</p>
     * 
     * @param initialNonZeroOddNumber
     * @param multiplierNonZeroOddNumber
     * @param object  the Object to create a <code>hashCode</code> for
     * @param testTransients  whether to include transient fields
     * @return int hash code
     * @throws IllegalArgumentException if the Object is <code>null</code>
     * @throws IllegalArgumentException if the number is zero or even
     */
    public static int reflectionHashCode(
            int initialNonZeroOddNumber, int multiplierNonZeroOddNumber,
            Object object, boolean testTransients) {
                
        if (object == null) {
            throw new IllegalArgumentException("The object to build a hash code for must not be null");
        }
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder(initialNonZeroOddNumber, multiplierNonZeroOddNumber);
        Field[] fields = object.getClass().getDeclaredFields();
        Field.setAccessible(fields, true);
        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];
            if (testTransients || !Modifier.isTransient(f.getModifiers())) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    try {
                        hashCodeBuilder.append(f.get(object));
                    } catch (IllegalAccessException e) {
                        //this can't happen. Would get a Security exception instead
                        //throw a runtime exception in case the impossible happens.
                        throw new InternalError("Unexpected IllegalAccessException");
                    }
                }
            }
        }
        return hashCodeBuilder.toHashCode();
    }

    //-------------------------------------------------------------------------
    
    /**
     * <p>Append a <code>hashCode</code> for an <code>Object</code>.</p>
     *
     * @param object  the Object to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(Object object) {
        if (object == null) {
            iTotal = iTotal * iConstant;
            
        } else {
            if (object.getClass().isArray() == false) {
                //the simple case, not an array, just the element 
                iTotal = iTotal * iConstant + object.hashCode();
                
            } else {
                //'Switch' on type of array, to dispatch to the correct handler
                // This handles multi dimensional arrays
                if (object instanceof long[]) {
                    append((long[]) object);
                } else if (object instanceof int[]) {
                    append((int[]) object);
                } else if (object instanceof short[]) {
                    append((short[]) object);
                } else if (object instanceof char[]) {
                    append((char[]) object);
                } else if (object instanceof byte[]) {
                    append((byte[]) object);
                } else if (object instanceof double[]) {
                    append((double[]) object);
                } else if (object instanceof float[]) {
                    append((float[]) object);
                } else if (object instanceof boolean[]) {
                    append((boolean[]) object);
                } else { 
                    // Not an array of primitives
                    append((Object[]) object);
                }
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>long</code>.</p>
     *
     * @param value  the long to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(long value) {
        iTotal = iTotal * iConstant + ((int) (value ^ (value >> 32)));
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for an <code>int</code>.</p>
     *
     * @param value  the int to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(int value) {
        iTotal = iTotal * iConstant + value;
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>short</code>.</p>
     *
     * @param value  the short to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(short value) {
        iTotal = iTotal * iConstant + (int) value;
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>char</code>.</p>
     *
     * @param value  the char to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(char value) {
        iTotal = iTotal * iConstant + (int) value;
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>byte</code>.</p>
     *
     * @param value  the byte to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(byte value) {
        iTotal = iTotal * iConstant + (int) value;
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>double</code>.</p>
     *
     * @param value  the double to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(double value) {
        return append(Double.doubleToLongBits(value));
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>float</code>.</p>
     *
     * @param value  the float to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(float value) {
        iTotal = iTotal * iConstant + Float.floatToIntBits(value);
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>long</code>.</p>
     *
     * @param value  the long to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(boolean value) {
        iTotal = iTotal * iConstant + (value ? 0 : 1);
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for an <code>Object</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(Object[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>long</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(long[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for an <code>int</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(int[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>short</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(short[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>char</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(char[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>byte</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(byte[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>double</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(double[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>float</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(float[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Append a <code>hashCode</code> for a <code>boolean</code> array.</p>
     *
     * @param array  the array to add to the <code>hashCode</code>
     * @return this
     */
    public HashCodeBuilder append(boolean[] array) {
        if (array == null) {
            iTotal = iTotal * iConstant;
        } else {
            for (int i = 0; i < array.length; i++) {
                append(array[i]);
            }
        }
        return this;
    }

    /**
     * <p>Return the computed <code>hashCode</code>.</p>
     * 
     * @return <code>hashCode</code> based on the fields appended
     */    
    public int toHashCode() {
        return iTotal;
    }

}
