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
 * Add your docs here.
 */
public class NRGPreferences {

    public interface IVisitable {
        public void accept(IVisitor vistor);
    }

    public interface IVisitor {
        public void visit(StringValue value);
        public void visit(IntegerValue value);
        public void visit(DoubleValue value);
        public void visit(BooleanValue value);
    }

    public static abstract class ValueBase implements IVisitable {
        protected final String key;

        protected ValueBase(final String key) {
            this.key = key;

            values.add(this);
        }

        public String getKey() {
            return this.key;
        }

        public void writeDefaultValue() {
            accept(writeDefaultVisitor);
        }

        public void printIfNotDefault() {
            accept(printIfNotDefaultVisitor);
        }
    }

    public static abstract class Value<TValue> extends ValueBase {
        protected final TValue defaultValue;

        protected Value(final String key, final TValue defaultValue) {
            super(key);
            this.defaultValue = defaultValue;
        }

        public TValue getDefaultValue() {
            return this.defaultValue;
        }
    }

    public static class StringValue extends Value<String> {

        public StringValue(final String key, final String defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IVisitor vistor) {
            vistor.visit(this);
        }

        public String getValue() {
            return preferences.getString(this.key, this.defaultValue);
        }
    }

    public static class IntegerValue extends Value<Integer> implements IVisitable {

        public IntegerValue(final String key, final int defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IVisitor vistor) {
            vistor.visit(this);
        }

        public int getValue() {
            return preferences.getInt(this.key, this.defaultValue.intValue());
        }

    }

    public static class DoubleValue extends Value<Double> implements IVisitable {

        public DoubleValue(final String key, final double defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IVisitor vistor) {
            vistor.visit(this);
        }

        public double getValue() {
            return preferences.getDouble(this.key, this.defaultValue.doubleValue());
        }

    }

    public static class BooleanValue extends Value<Boolean> implements IVisitable {

        public BooleanValue(final String key, final boolean defaultValue) {
            super(key, defaultValue);
        }

        @Override
        public void accept(final IVisitor vistor) {
            vistor.visit(this);
        }

        public boolean getValue() {
            return preferences.getBoolean(this.key, this.defaultValue.booleanValue());
        }
    }

    private static class WriteDefaultVistor implements IVisitor {

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

    private static class PrintIfNotDefaultVisitor implements IVisitor {

        @Override
        public void visit(StringValue value) {
            String currentValue = value.getValue();

            if (currentValue.compareTo(value.getDefaultValue()) != 0) {
                System.out.println("NON-DEFAULT PREFERENCE: " + value.getKey() + " = " + currentValue);
            }
        }

        @Override
        public void visit(IntegerValue value) {
            int currentValue = value.getValue();

            if (currentValue != value.getDefaultValue().intValue()) {
                System.out.println("NON-DEFAULT PREFERENCE: " + value.getKey() + " = " + currentValue);
            }
        }

        @Override
        public void visit(DoubleValue currentValue) {
            double value2 = currentValue.getValue();

            if (value2 != currentValue.getDefaultValue().doubleValue()) {
                System.out.println("NON-DEFAULT PREFERENCE: " + currentValue.getKey() + " = " + value2);
            }
        }

        @Override
        public void visit(BooleanValue currentValue) {
            boolean value2 = currentValue.getValue();

            if (value2 != currentValue.getDefaultValue().booleanValue()) {
                System.out.println("NON-DEFAULT PREFERENCE: " + currentValue.getKey() + " = " + value2);
            }
        }

    }

    private static final ArrayList<ValueBase> values = new ArrayList<ValueBase>();
    private static final BooleanValue WRITE_DEFAULT = new BooleanValue("WriteDefaultPrefs", true);

    private static final Preferences preferences = Preferences.getInstance();
    private static final WriteDefaultVistor writeDefaultVisitor = new WriteDefaultVistor();
    private static final PrintIfNotDefaultVisitor printIfNotDefaultVisitor = new PrintIfNotDefaultVisitor();

    public static void init() {
        if (WRITE_DEFAULT.getValue()) {
            values.stream().forEach(p -> p.writeDefaultValue());
            preferences.putBoolean(WRITE_DEFAULT.getKey(), false);
        } else {
            values.stream().forEach(p -> p.printIfNotDefault());
        }
    }
}
