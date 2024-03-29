/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.stream.Stream;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import static org.reflections.scanners.Scanners.*;

import edu.wpi.first.wpilibj.Preferences;

/**
 * An implementation of robot preferences.
 */
public class RobotPreferences {

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
    public static abstract class Value implements IVisitableValue {
        protected final String key;

        /**
         * Constructs an instance of this class.
         * 
         * @param key The preferences key.
         */
        protected Value(final String key) {
            this.key = key;
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
         * Returns true if the preferences value exists.
         * 
         * @return Whether the value exists.
         */
        public boolean exists() {
            return preferences.containsKey(this.key);
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
    public static abstract class TypedValue<TValue> extends Value {
        protected final TValue defaultValue;

        /**
         * Constructs an instance of this class.
         * 
         * @param key          The preferences key.
         * @param defaultValue The default value.
         */
        protected TypedValue(final String key, final TValue defaultValue) {
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
    public static class StringValue extends TypedValue<String> {

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

        /**
         * Set the current value of the preferences key.
         * 
         * @param value The new value.
         */
        public void setValue(String value) {
            preferences.putString(this.key, value);
        }
    }

    /** A class implementing a preferences integer value. */
    public static class IntegerValue extends TypedValue<Integer> {

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
            return preferences.getInt(this.key, this.defaultValue);
        }

        /**
         * Set the current value of the preferences key.
         * 
         * @param value The new value.
         */
        public void setValue(int value) {
            preferences.putInt(this.key, value);
        }
    }

    /** A class implementing a preferences double value. */
    public static class DoubleValue extends TypedValue<Double> {

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
            return preferences.getDouble(this.key, this.defaultValue);
        }

        /**
         * Set the current value of the preferences key.
         * 
         * @param value The new value.
         */
        public void setValue(double value) {
            preferences.putDouble(this.key, value);
        }
    }

    /** A class implementing a preferences Boolean value. */
    public static class BooleanValue extends TypedValue<Boolean> {

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
            return preferences.getBoolean(this.key, this.defaultValue);
        }

        /**
         * Set the current value of the preferences key.
         * 
         * @param value The new value.
         */
        public void setValue(boolean value) {
            preferences.putBoolean(this.key, value);
        }
    }

    /**
     * A Visitor implementation that writes the default value of the preferences key
     * to the preferences file.
     */
    private static class WriteDefaultVistor implements IValueVisitor {

        @Override
        public void visit(final StringValue value) {
            value.setValue(value.getDefaultValue());
        }

        @Override
        public void visit(final IntegerValue value) {
            value.setValue(value.getDefaultValue());
        }

        @Override
        public void visit(final DoubleValue value) {
            value.setValue(value.getDefaultValue());
        }

        @Override
        public void visit(final BooleanValue value) {
            value.setValue(value.getDefaultValue());
        }

    }

    /**
     * A Visitor implementation that prints the current value to the console if it
     * is not set to the default value.
     */
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

            if (currentValue != value.getDefaultValue()) {
                printNonDefaultValue(value.getKey(), Integer.toString(currentValue));
            }
        }

        @Override
        public void visit(DoubleValue value) {
            double currentValue = value.getValue();

            if (currentValue != value.getDefaultValue()) {
                printNonDefaultValue(value.getKey(), Double.toString(currentValue));
            }
        }

        @Override
        public void visit(BooleanValue value) {
            boolean currentValue = value.getValue();

            if (currentValue != value.getDefaultValue()) {
                printNonDefaultValue(value.getKey(), Boolean.toString(currentValue));
            }
        }

    }

    @RobotPreferencesValue
    public static final BooleanValue WRITE_DEFAULT = new BooleanValue("WriteDefaultPrefs", true);

    private static final Preferences preferences = Preferences.getInstance();
    private static final WriteDefaultVistor writeDefaultVisitor = new WriteDefaultVistor();
    private static final PrintIfNotDefaultVisitor printIfNotDefaultVisitor = new PrintIfNotDefaultVisitor();

    /**
     * Initializes the preferences, write default preferences if needed/requested.
     */
    public static void init() {
        if (WRITE_DEFAULT.getValue()) {
            // Remove all keys, and write default values to the known ones.
            preferences.removeAll();
            getValues().forEach(p -> p.writeDefaultValue());
            WRITE_DEFAULT.setValue(false);
        } else {
            HashSet<String> validKeys = new HashSet<String>();

            // Print if non-default values and add keys not currently in the preferences.
            getValues().forEach(p -> {
                if (p.exists()) {
                    p.printIfNotDefault();
                } else {
                    p.writeDefaultValue();
                }
                validKeys.add(p.getKey());
            });

            // Remove unused preferences keys. (Keys with leading "." are internal to
            // the Shuffleboard implementation and should not be removed.)
            preferences.getKeys().stream()
                .filter(k -> !k.startsWith(".") && !validKeys.contains(k))
                .forEach(k -> {
                    System.out.println(String.format("REMOVING UNUSED KEY: %s", k));
                    preferences.remove(k);
                });
        }
    }

    /**
     * Returns all of the preferences values in the robot.
     * 
     * @return A stream providing access to all of the preferences values in the
     *         robot.
     */
    private static Stream<Value> getValues() {
        var config = new ConfigurationBuilder().forPackage("frc.robot").setScanners(FieldsAnnotated);
        var values = new Reflections(config).get(FieldsAnnotated.with(RobotPreferencesValue.class).as(Field.class));

        return values.stream().filter(f -> Modifier.isStatic(f.getModifiers())).map(f -> {
            try {
                return (Value) f.get(null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
