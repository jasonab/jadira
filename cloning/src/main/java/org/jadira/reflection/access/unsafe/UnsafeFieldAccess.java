/*
 *  Copyright 2013 Christopher Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.reflection.access.unsafe;

import java.lang.reflect.Field;

import org.jadira.reflection.access.api.FieldAccess;

/**
 * FieldAccess implementation based on sun.misc.Unsafe
 * @param <C> The Class containing the Field to be accessed
 */
public class UnsafeFieldAccess<C> implements FieldAccess<C> {

	private static final UnsafeOperations UNSAFE_OPERATIONS = UnsafeOperations.getUnsafeOperations();

	private Field field;
	private Class<C> declaringClass;
	private Class<?> type;
	private long fieldOffset;

	@SuppressWarnings("unchecked")
	private UnsafeFieldAccess(Field f) {
		this.field = f;
		this.declaringClass = (Class<C>) f.getDeclaringClass();
		this.type = (Class<?>) f.getType();
		this.fieldOffset = UNSAFE_OPERATIONS.getObjectFieldOffset(f);
	}

	@Override
	public Class<C> declaringClass() {
		return declaringClass;
	}
	
	@Override
	public Class<?> fieldClass() {
		return type;
	}
	
	@Override
	public Field field() {
		return field;
	}
	
	/**
	 * Get the offset position (in bytes) for this field
	 * @return Offset as a long
	 */
	public long fieldOffset() {
		return fieldOffset;
	}
	
	@Override
	public Object getValue(C parent) {
		return UNSAFE_OPERATIONS.getObject(parent, fieldOffset);
	}

	@Override
	public boolean getBooleanValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public byte getByteValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public char getCharValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public short getShortValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public int getIntValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public long getLongValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public float getFloatValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public double getDoubleValue(C parent) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putValue(C parent, Object newFieldValue) {
		if (newFieldValue == null) {
			UNSAFE_OPERATIONS.putNullObject(parent, fieldOffset);
		} else {
			UNSAFE_OPERATIONS.putObject(parent, fieldOffset, newFieldValue);
		}
	}
	
	@Override
	public void putBooleanValue(C parent, boolean newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putByteValue(C parent, byte newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putCharValue(C parent, char newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putShortValue(C parent, short newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putIntValue(C parent, int newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putLongValue(C parent, long newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putFloatValue(C parent, float newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	@Override
	public void putDoubleValue(C parent, double newFieldValue) {
		throw new UnsupportedOperationException("Not supported for this field type");
	}
	
	/**
	 * Get a new instance that can access the given Field
	 * @param f Field to be accessed
	 * @param <C> The type of class containing the field to be accessed
	 * @return New UnsafeFieldAccess instance
	 */
	public static <C> UnsafeFieldAccess<C> get(Field f) {
		
		Class<?> type = f.getType();
		
		if (type.isPrimitive()) {
			if (java.lang.Boolean.TYPE == type) {
				return new UnsafeBooleanFieldAccess<C>(f);
			} else if (java.lang.Byte.TYPE == type) {
				return new UnsafeByteFieldAccess<C>(f);
			} else if (java.lang.Character.TYPE == type) {
				return new UnsafeCharFieldAccess<C>(f);
			} else if (java.lang.Short.TYPE == type) {
				return new UnsafeShortFieldAccess<C>(f);
			} else if (java.lang.Integer.TYPE == type) {
				return new UnsafeIntFieldAccess<C>(f);
			} else if (java.lang.Long.TYPE == type) {
				return new UnsafeLongFieldAccess<C>(f);
			} else if (java.lang.Float.TYPE == type) {
				return new UnsafeFloatFieldAccess<C>(f);
			} else if (java.lang.Double.TYPE == type) {
				return new UnsafeDoubleFieldAccess<C>(f);
			}
		}
		return new UnsafeFieldAccess<C>(f);
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing boolean fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeBooleanFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeBooleanFieldAccess(Field f) {
			super(f);
		}

		@Override
		public Boolean getValue(C parent) {
			return Boolean.valueOf(getBooleanValue(parent));
		}
		
		@Override
		public boolean getBooleanValue(C parent) {
			return UNSAFE_OPERATIONS.getBoolean(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Boolean) {
				putBooleanValue(parent, ((Boolean)newFieldValue).booleanValue());
			} else {
				throw new IllegalArgumentException("Only a boolean value can be supplied to a boolean field");
			}
		}
		
		@Override
		public void putBooleanValue(C parent, boolean newFieldValue) {
			UNSAFE_OPERATIONS.putBoolean(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing byte fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeByteFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeByteFieldAccess(Field f) {
			super(f);
		}

		@Override
		public Byte getValue(C parent) {
			return Byte.valueOf(getByteValue(parent));
		}
		
		@Override
		public byte getByteValue(C parent) {
			return UNSAFE_OPERATIONS.getByte(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Byte) {
				putByteValue(parent, ((Byte)newFieldValue).byteValue());
			} else {
				throw new IllegalArgumentException("Only a byte value can be supplied to a byte field");
			}
		}
		
		@Override
		public void putByteValue(C parent, byte newFieldValue) {
			UNSAFE_OPERATIONS.putByte(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing char fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeCharFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeCharFieldAccess(Field f) {
			super(f);
		}
		
		@Override
		public Character getValue(C parent) {
			return Character.valueOf(getCharValue(parent));
		}

		@Override
		public char getCharValue(C parent) {
			return UNSAFE_OPERATIONS.getChar(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Character) {
				putCharValue(parent, ((Character)newFieldValue).charValue());
			} else {
				throw new IllegalArgumentException("Only a character value can be supplied to a char field");
			}
		}
		
		@Override
		public void putCharValue(C parent, char newFieldValue) {
			UNSAFE_OPERATIONS.putChar(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing short fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeShortFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeShortFieldAccess(Field f) {
			super(f);
		}

		@Override
		public Short getValue(C parent) {
			return Short.valueOf(getShortValue(parent));
		}
		
		@Override
		public short getShortValue(C parent) {
			return UNSAFE_OPERATIONS.getShort(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Short) {
				putShortValue(parent, ((Short)newFieldValue).shortValue());
			} else if (newFieldValue instanceof Byte) {
				putShortValue(parent, ((Byte)newFieldValue).shortValue());
			} else {
				throw new IllegalArgumentException("Only a short, or byte value can be supplied to a short field");
			}
		}
		
		@Override
		public void putShortValue(C parent, short newFieldValue) {
			UNSAFE_OPERATIONS.putShort(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing int fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeIntFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeIntFieldAccess(Field f) {
			super(f);
		}
		
		@Override
		public Integer getValue(C parent) {
			return Integer.valueOf(getIntValue(parent));
		}

		@Override
		public int getIntValue(C parent) {
			return UNSAFE_OPERATIONS.getInt(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Integer) {
				putIntValue(parent, ((Integer)newFieldValue).intValue());
			} else if (newFieldValue instanceof Short) {
				putIntValue(parent, ((Short)newFieldValue).intValue());
			} else if (newFieldValue instanceof Byte) {
				putIntValue(parent, ((Byte)newFieldValue).intValue());
			} else {
				throw new IllegalArgumentException("Only an integer, short, or byte value can be supplied to an int field");
			}
		}
		
		@Override
		public void putIntValue(C parent, int newFieldValue) {
			UNSAFE_OPERATIONS.putInt(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing long fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeLongFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeLongFieldAccess(Field f) {
			super(f);
		}

		@Override
		public Long getValue(C parent) {
			return Long.valueOf(getLongValue(parent));
		}
		
		@Override
		public long getLongValue(C parent) {
			return UNSAFE_OPERATIONS.getLong(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Long) {
				putLongValue(parent, ((Long)newFieldValue).longValue());
			} else if (newFieldValue instanceof Integer) {
				putLongValue(parent, ((Integer)newFieldValue).longValue());
			} else if (newFieldValue instanceof Short) {
				putLongValue(parent, ((Short)newFieldValue).longValue());
			} else if (newFieldValue instanceof Byte) {
				putLongValue(parent, ((Byte)newFieldValue).longValue());
			} else {
				throw new IllegalArgumentException("Only a long, integer, short, or byte value can be supplied to a long field");
			}
		}
		
		@Override
		public void putLongValue(C parent, long newFieldValue) {
			UNSAFE_OPERATIONS.putLong(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing float fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeFloatFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeFloatFieldAccess(Field f) {
			super(f);
		}
		
		@Override
		public Float getValue(C parent) {
			return Float.valueOf(getFloatValue(parent));
		}

		@Override
		public float getFloatValue(C parent) {
			return UNSAFE_OPERATIONS.getFloat(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Float) {
				putFloatValue(parent, ((Float)newFieldValue).floatValue());
			} else {
				throw new IllegalArgumentException("Only a float value can be supplied to a float field");
			}
		}		
		
		@Override
		public void putFloatValue(C parent, float newFieldValue) {
			UNSAFE_OPERATIONS.putFloat(parent, super.fieldOffset, newFieldValue);
		}
	}
	
	/**
	 * UnsafeFieldAccess implementation suitable for accessing double fields
	 * @param <C> The Class containing the Field to be accessed
	 */
	public static class UnsafeDoubleFieldAccess<C> extends UnsafeFieldAccess<C> {

		/**
		 * Construct a new instance for the given Field
		 * @param f The Field to be accessed
		 */
		public UnsafeDoubleFieldAccess(Field f) {
			super(f);
		}

		@Override
		public Double getValue(C parent) {
			return Double.valueOf(getDoubleValue(parent));
		}
		
		@Override
		public double getDoubleValue(C parent) {
			return UNSAFE_OPERATIONS.getDouble(parent, super.fieldOffset);
		}
		
		@Override
		public void putValue(C parent, Object newFieldValue) {
			if (newFieldValue instanceof Double) {
				putDoubleValue(parent, ((Double)newFieldValue).doubleValue());
			} else {
				throw new IllegalArgumentException("Only a double value can be supplied to a double field");
			}
		}
		
		@Override
		public void putDoubleValue(C parent, double newFieldValue) {
			UNSAFE_OPERATIONS.putDouble(parent, super.fieldOffset, newFieldValue);
		}
	}
}
