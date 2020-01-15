/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Preferences;

/**
 * An implementation of robot preferences.
 */
public class NRGPreferences {

    /**
     * An interface defining methods that allow a Visitor to perform an operation on
     * a Value
     */
    public interface IVisitableValue {
        /**
         * Called to allow a Visitor to perform an operation on a Value.
         * 
         * @param vistor The Visitor that performs an operation.
         */
        public void accept(IValueVisitor vistor);
    }

    /** An interface defining the types of Values a Visitor can consume. */
    public interface IValueVisitor {
        /**
         * Perform an operation on a StringValue.
         * 
         * @param value The StringValue.
         */
        public void visit(StringValue value);

        /**
         * Perform an operation on a IntegerValue.
         * 
         * @param value The IntegerValue.
         */
        public void visit(IntegerValue value);

        /**
         * Perform an operation on a DoubleValue.
         * 
         * @param value The DoubleValue
         */
        public void visit(DoubleValue value);

        /**
         * Perform an operation on a BooleanValue.
         * 
         * @param value The BooleanValue
         */
        public void visit(BooleanValue value);
    }

    /** The base class for all preferences value types. */
    public static abstract class ValueBase implements IVisitableValue {
        protected final String key;

        /**
         * Constructs an instance of this class.
         * 
         * @param key The preferences key.
         */
        protected ValueBase(final String key) {
            this.key = key;

            values.add(this);
        }

        /**
         * Returns the preferences key.
         * 
         * @return The preferences key.
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Writes the default value of this preference key to the preferences file.
         */
        public void writeDefaultValue() {
            accept(writeDefaultVisitor);
        }

        /**
         * Prints the preference key current value if it is not set to the default.
         */
        public void printIfNotDefault() {
            accept(printIfNotDefaultVisitor);
        }
    }

    /**
     * A base class for a preference value of the specified type.
     * 
     * @param <TValue> The preference value type.
     */
    public static abstract class Value<TValue> extends ValueBase {
        protected final TValue defaultValue;

        /**
         * Constructs an instance of this class.
         * 
         * @param key          The preferences key.
         * @param defaultValue The default value.
         */
        protected Value(final String key, final TValue defaultValue) {
            super(key);
            this.defaultValue = defaultValue;
        }

        /**
         * Returns the default value of the preferences key.
         * 
         * @return The default value.
         */
        public TValue getDefaultValue() {
            return this.defaultValue;
        }
    }

    /** A class implementing a preferences string value. */
    public static class StringValue extends Value<String> {

        /**
         * Constructs an instance of this class.
         * 
         * @param key          The preferences key.
         * @param defaultValue The default value.
         */
        public StringValue(final String key, final String defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IValueVisitor vistor) {
            vistor.visit(this);
        }

        /**
         * Returns the current value of the preferences key.
         * 
         * @return The current value.
         */
        public String getValue() {
            return preferences.getString(this.key, this.defaultValue);
        }
    }

    /** A class implementing a preferences integer value. */
    public static class IntegerValue extends Value<Integer> implements IVisitableValue {

        /**
         * Constructs an instance of this class.
         * 
         * @param key          The preferences key.
         * @param defaultValue The default value.
         */
        public IntegerValue(final String key, final int defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IValueVisitor vistor) {
            vistor.visit(this);
        }

        /**
         * Returns the current value of the preferences key.
         * 
         * @return The current value.
         */
        public int getValue() {
            return preferences.getInt(this.key, this.defaultValue.intValue());
        }

    }

    /** A class implementing a preferences double value. */
    public static class DoubleValue extends Value<Double> implements IVisitableValue {

        /**
         * Constructs an instance of this class.
         * 
         * @param key          The preferences key.
         * @param defaultValue The default value.
         */
        public DoubleValue(final String key, final double defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IValueVisitor vistor) {
            vistor.visit(this);
        }

        /**
         * Returns the current value of the preferences key.
         * 
         * @return The current value.
         */
        public double getValue() {
            return preferences.getDouble(this.key, this.defaultValue.doubleValue());
        }

    }

    /** A class implementing a preferences Boolean value. */
    public static class BooleanValue extends Value<Boolean> implements IVisitableValue {

        /**
         * Constructs an instance of this class.
         * 
         * @param key          The preferences key.
         * @param defaultValue The default value.
         */
        public BooleanValue(final String key, final boolean defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IValueVisitor vistor) {
            vistor.visit(this);
        }

        /**
         * Returns the current value of the preferences key.
         * 
         * @return The current value.
         */
        public boolean getValue() {
            return preferences.getBoolean(this.key, this.defaultValue.booleanValue());
        }
    }

    /** A Visitor implementation that writes the default value of the preferences key to the preferences file. */
    private static class WriteDefaultVistor implements IValueVisitor {

        @Override
        public void visit(final StringValue value) {
            preferences.putString(value.getKey(), value.getDefaultValue());
        }

        @Override
        public void visit(final IntegerValue value) {
            preferences.putInt(value.getKey(), value.getDefaultValue());
        }

        @Override
        public void visit(final DoubleValue value) {
            preferences.putDouble(value.getKey(), value.getDefaultValue());
        }

        @Override
        public void visit(final BooleanValue value) {
            preferences.putBoolean(value.getKey(), value.getDefaultValue());
        }

    }

    /** A Visitor implementation that prints the current value to the console if it is not set to the default value.  */
    private static class PrintIfNotDefaultVisitor implements IValueVisitor {

        private void printNonDefaultValue(String key, String value) {
            System.out.println("NON-DEFAULT PREFERENCE: " + key + " = " + value);
        }

        @Override
        public void visit(StringValue value) {
            String currentValue = value.getValue();

            if (currentValue.compareTo(value.getDefaultValue()) != 0) {
                printNonDefaultValue(value.getKey(), currentValue);
            }
        }

        @Override
        public void visit(IntegerValue value) {
            int currentValue = value.getValue();

            if (currentValue != value.getDefaultValue().intValue()) {
                printNonDefaultValue(value.getKey(), Integer.toString(currentValue));
            }
        }

        @Override
        public void visit(DoubleValue value) {
            double currentValue = value.getValue();

            if (currentValue != value.getDefaultValue().doubleValue()) {
                printNonDefaultValue(value.getKey(), Double.toString(currentValue));
            }
        }

        @Override
        public void visit(BooleanValue value) {
            boolean currentValue = value.getValue();

            if (currentValue != value.getDefaultValue().booleanValue()) {
                printNonDefaultValue(value.getKey(), Boolean.toString(currentValue));
            }
        }

    }

    private static final ArrayList<ValueBase> values = new ArrayList<ValueBase>();
    private static final BooleanValue WRITE_DEFAULT = new BooleanValue("WriteDefaultPrefs", true);

    private static final Preferences preferences = Preferences.getInstance();
    private static final WriteDefaultVistor writeDefaultVisitor = new WriteDefaultVistor();
    private static final PrintIfNotDefaultVisitor printIfNotDefaultVisitor = new PrintIfNotDefaultVisitor();

    /** Initializes the preferences, write default preferences if needed/requested. */
    public static void init() {
        if (WRITE_DEFAULT.getValue()) {
            values.stream().forEach(p -> p.writeDefaultValue());
            preferences.putBoolean(WRITE_DEFAULT.getKey(), false);
        } else {
            values.stream().forEach(p -> p.printIfNotDefault());
        }
    }
}
